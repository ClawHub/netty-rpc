package com.clawhub.nettyrpc.core;

import com.clawhub.nettyrpc.core.codec.NettyObjectCodec;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * <Description>TCP短链接 客户端<br>
 *
 * @author LiZhiming<br>
 * @version 1.0<br>
 * @taskId <br>
 * @CreateDate 2018/11/3 10:48 <br>
 */
public class NettyTCPClient {
    /**
     * 日志记录器
     */
    private Logger logger = LoggerFactory.getLogger(NettyTCPClient.class);


    /**
     * The Bootstrap.
     */
    private Bootstrap bootstrap;

    /**
     * Init.
     */
    public void init() {
        NettyObjectCodec nettyObjectCodec = new NettyObjectCodec();
        EventLoopGroup group = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(group).channel(NioSocketChannel.class);
        bootstrap.handler(new ChannelInitializer<Channel>() {
            @Override
            protected void initChannel(Channel ch) {
                ChannelPipeline pipeline = ch.pipeline();

                //编码
                pipeline.addLast(nettyObjectCodec.getEncode());
                //解码
                pipeline.addLast(nettyObjectCodec.getDecode());
                //TCP粘包
                pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
                pipeline.addLast(new LengthFieldPrepender(4));
                //逻辑处理
                pipeline.addLast(new TcpClientHandler());
            }
        });
//      bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
    }


    /**
     * Gets channel.
     *
     * @return the channel
     */
    private Channel getChannel() {
        Channel channel = null;
        try {
            channel = bootstrap.connect("172.16.9.44", 9997).sync().channel();
        } catch (Exception e) {
            logger.error(String.format("连接Server(IP[%s],PORT[%s])失败", "172.16.9.44", 9997), e);
        }
        return channel;
    }

    /**
     * Send msg.
     *
     * @param msg the msg
     * @throws Exception the exception
     */
    public void sendMsg(String msg) throws Exception {
        Channel channel = getChannel();
        if (channel == null) {
            logger.warn("消息发送失败,连接尚未建立!");
            return;
        }
        byte[] value = msg.getBytes("UTF-8");
        ByteBufAllocator alloc = channel.alloc();
        ByteBuf buf = alloc.buffer(value.length);
        buf.writeBytes(value);
        channel.writeAndFlush(buf).sync();
    }

    public void sendClassInfo(ClassInfo classInfo) throws Exception {
        Channel channel = getChannel();
        if (channel == null) {
            logger.warn("消息发送失败,连接尚未建立!");
            return;
        }
        channel.writeAndFlush(classInfo).sync();
    }
}
