package com.clawhub.nettyrpc.core;

import com.clawhub.nettyrpc.core.codec.CodecContext;
import com.clawhub.nettyrpc.demo.ApiProvider;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * <Description>RPC服务端<br>
 *
 * @author LiZhiming<br>
 * @version 1.0<br>
 * @taskId <br>
 * @CreateDate 2018/11/7 11:33 <br>
 */
@Component
public class RPCServer {

    /**
     * 编解码上下文
     */
    @Autowired
    private CodecContext codecContext;
    /**
     * 服务端启动监听端口
     */
    @Value("${rpc.server.port}")
    private int port;
    /**
     * 服务端启动IP
     */
    @Value("${rpc.netty.server.ip}")
    private String ip;
    /**
     * 编解码类型
     */
    @Value("${rpc.netty.codec}")
    private String codec;
    /**
     * 用于分配处理业务线程的线程组个数
     */
    @Value("${rpc.netty.server.boss.group.size}")
    private int bossGroupSize;
    /**
     * 业务出现线程大小
     */
    @Value("${rpc.netty.server.worker.group.size}")
    private int workerGroupSize;
    /**
     * BACKLOG用于构造服务端套接字ServerSocket对象，
     * 标识当服务器请求处理线程全满时，用于临时存放已完成三次握手的请求的队列的最大长度。
     * 如果未设置或所设置的值小于1，Java将使用默认值50。
     */
    @Value("${rpc.netty.server.so.backlog}")
    private int soBacklog;
    /**
     * Boss线程：由这个线程池提供的线程是boss种类的，用于创建、连接、绑定socket， （有点像门卫）然后把这些socket传给worker线程池。
     * 在服务器端每个监听的socket都有一个boss线程来处理。在客户端，只有一个boss线程来处理所有的socket。
     */
    private EventLoopGroup bossGroup = new NioEventLoopGroup(bossGroupSize);
    /**
     * Worker线程：Worker线程执行所有的异步I/O，即处理操作
     */
    private EventLoopGroup workerGroup = new NioEventLoopGroup(workerGroupSize);

    /**
     * 系统启动时执行
     */
    @PostConstruct
    public void start() {
        System.out.println("-----------netty-----------");
        RpcServicePool.rpcService.put("com.clawhub.nettyrpc.demo.Api", new ApiProvider());
        try {
            // ServerBootstrap 启动NIO服务的辅助启动类,负责初始话netty服务器，并且开始监听端口的socket请求
            ServerBootstrap serverBootstrap = new ServerBootstrap()
                    .group(bossGroup, workerGroup)
                    // 设置非阻塞,用它来建立新accept的连接,用于构造serversocketchannel的工厂类
                    .channel(NioServerSocketChannel.class)
                    //设置绑定的IP 端口
                    .localAddress(ip, port)
                    //增加日志,handler服务端启动就执行
                    .handler(new LoggingHandler(LogLevel.INFO))
                    //业务数据处理，childHandler只有客户端连接才执行
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel sc) throws Exception {
                            ChannelPipeline pipeline = sc.pipeline();
                            //编码
                            pipeline.addLast(codecContext.getEncode(codec));
                            //解码
                            pipeline.addLast(codecContext.getDecode(codec));
                            //TCP 粘包处理
                            //lengthFieldOffset = 0；//长度字段的偏差
                            //lengthFieldLength = 4；//长度字段占的字节数
                            //lengthAdjustment = 0；//添加到长度字段的补偿值
                            //initialBytesToStrip = 4; //从解码帧中第一次去除的字节数
                            //通用TCP黏包解决方案
                            pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
                            pipeline.addLast(new LengthFieldPrepender(4));
                            //业务处理
                            pipeline.addLast(new InvokerHandler());

                        }
                    })
                    //  BACKLOG用于构造服务端套接字ServerSocket对象，标识当服务器请求处理线程全满时，用于临时存放已完成三次握手的请求的队列的最大长度。如果未设置或所设置的值小于1，Java将使用默认值50。
                    //option()是提供给NioServerSocketChannel用来接收进来的连接,也就是boss线程
                    .option(ChannelOption.SO_BACKLOG, soBacklog)
                    //是否启用心跳保活机制。在双方TCP套接字建立连接后（即都进入ESTABLISHED状态）并且在两个小时左右上层没有任何数据传输的情况下，这套机制才会被激活。
                    //childOption()是提供给由父管道ServerChannel接收到的连接，也就是worker线程
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            //启动同步
            ChannelFuture future = serverBootstrap.bind().sync();
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    /**
     * Shutdown.
     */
    public void shutdown() {
        workerGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();
    }
}
