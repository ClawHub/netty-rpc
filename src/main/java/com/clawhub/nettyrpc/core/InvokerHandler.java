package com.clawhub.nettyrpc.core;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <Description>InvokerHandler <br>
 *
 * @author LiZhiming<br>
 * @version 1.0<br>
 * @taskId <br>
 * @CreateDate 2018/11/6 9:16 <br>
 */
public class InvokerHandler extends ChannelHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ClassInfo classInfo = (ClassInfo) msg;
        System.out.println(classInfo);
        if (RpcServicePool.rpcService.containsKey(classInfo.getClassName())) {
            Object clazz = RpcServicePool.rpcService.get(classInfo.getClassName());
            Method method = clazz.getClass().getMethod(classInfo.getMethodName(), classInfo.getTypes());
            Object result = method.invoke(clazz, classInfo.getObjects());
            ctx.write(result);
        } else {
            ctx.write("class not find");
        }

        ctx.flush();
        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}