package com.clawhub.nettyrpc.core;

import com.clawhub.nettyrpc.demo.ApiProvider;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * <Description>rpc服务提供者加载<br>
 *
 * @author LiZhiming<br>
 * @version 1.0<br>
 * @taskId <br>
 * @CreateDate 2018/11/9 11:28 <br>
 */
@Component
public class RpcServiceLoader {
    /**
     * Load.
     */
    @PostConstruct
    public void load() {
        System.out.println("===1==load");
//        RpcServicePool.rpcService.put("com.clawhub.nettyrpc.demo.Api", new ApiProvider());
    }

}