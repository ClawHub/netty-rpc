package com.clawhub.nettyrpc.demo;

import com.clawhub.nettyrpc.core.ClassInfo;
import com.clawhub.nettyrpc.core.NettyTCPClient;

/**
 * <Description>调用者<br>
 *
 * @author LiZhiming<br>
 * @version 1.0<br>
 * @taskId <br>
 * @CreateDate 2018/11/9 11:19 <br>
 */
public class ApiConsumer {

    public static void main(String[] args) {
        NettyTCPClient client = new NettyTCPClient();
        client.init();
        ClassInfo classInfo = new ClassInfo();
        classInfo.setClassName("com.clawhub.nettyrpc.demo.Api");
        classInfo.setMethodName("test");
        Class<?>[] types = {String.class};
        classInfo.setTypes(types);
        Object[] objects = {"ok"};
        classInfo.setObjects(objects);
        try {
            client.sendClassInfo(classInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}