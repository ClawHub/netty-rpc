package com.clawhub.nettyrpc.core.codec;

import io.netty.channel.ChannelHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <Description>编解码上下文<br>
 *
 * @author LiZhiming<br>
 * @version 1.0<br>
 * @taskId <br>
 * @CreateDate 2018/11/7 11:39 <br>
 */
@Component
public class CodecContext {
    /**
     * The Codec strategy map.
     */
    private final Map<String, CodecStrategy> codecStrategyMap = new ConcurrentHashMap<>();

    /**
     * 注入所有实现 CodecStrategy接口的类
     *
     * @param codecStrategyMap the codec strategy map
     */
    @Autowired
    public CodecContext(Map<String, CodecStrategy> codecStrategyMap) {
        this.codecStrategyMap.clear();
        //jdk8 ::
        codecStrategyMap.forEach(this.codecStrategyMap::put);
    }

    /**
     * 获取解码器
     *
     * @param codec 类型
     * @return 解码器
     */
    public ChannelHandler getDecode(String codec) {
        return codecStrategyMap.get(codec).getDecode();
    }

    /**
     * 获取编码器
     *
     * @param codec 类型
     * @return 编码器
     */
    public ChannelHandler getEncode(String codec) {
        return codecStrategyMap.get(codec).getEncode();
    }
}