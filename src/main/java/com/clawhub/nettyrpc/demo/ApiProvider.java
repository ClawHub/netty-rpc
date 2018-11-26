package com.clawhub.nettyrpc.demo;

import org.springframework.stereotype.Component;

/**
 * <Description>提供者<br>
 *
 * @author LiZhiming<br>
 * @version 1.0<br>
 * @taskId <br>
 * @CreateDate 2018/11/9 11:19 <br>
 */
@Component
public class ApiProvider implements Api {

    @Override
    public String test(String param) {
        if ("ok".equals(param)) {
            return "你的入参是：" + param;
        } else {
            return "你要输入“ok”才行！";
        }

    }
}