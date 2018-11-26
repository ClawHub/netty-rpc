package com.clawhub.nettyrpc.core.codec;

import io.netty.channel.ChannelHandler;

/**
 * <Description>编解码接口<br>
 *
 * @author LiZhiming<br>
 * @version 1.0<br>
 * @taskId <br>
 * @CreateDate 2018/11/7 11:39 <br>
 */
public interface CodecStrategy {
    /**
     * 获取解码器
     *
     * @return 解码器
     */
    ChannelHandler getDecode();

    /**
     * 获取编码器
     *
     * @return 编码器
     */
    ChannelHandler getEncode();
}
