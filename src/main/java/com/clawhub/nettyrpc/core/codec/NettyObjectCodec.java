package com.clawhub.nettyrpc.core.codec;

import io.netty.channel.ChannelHandler;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import org.springframework.stereotype.Component;

/**
 * <Description> Netty 自带的编解码<br>
 *
 * @author LiZhiming<br>
 * @version 1.0<br>
 * @taskId <br>
 * @CreateDate 2018/11/7 13:48 <br>
 */
@Component("nettyObjectCodec")
public class NettyObjectCodec implements CodecStrategy {

    @Override
    public ChannelHandler getDecode() {
        return new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null));
    }

    @Override
    public ChannelHandler getEncode() {
        return new ObjectEncoder();
    }
}