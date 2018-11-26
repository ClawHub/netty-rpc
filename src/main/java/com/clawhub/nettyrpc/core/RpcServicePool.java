package com.clawhub.nettyrpc.core;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <Description>RPC服务接口定义、服务接口实现绑定关系池<br>
 *
 * @author LiZhiming<br>
 * @version 1.0<br>
 * @taskId <br>
 * @CreateDate 2018/11/9 11:26 <br>
 */
public class RpcServicePool {
    /**
     * The Rpc service.
     */
    public static Map<String, Object> rpcService = new ConcurrentHashMap<>();

}
