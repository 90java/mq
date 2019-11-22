package com.nojava.rabbitmq.helloworld;

import com.nojava.rabbitmq.MqConnectUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class mq_helloworld_p {

    private final static String QUEUE_NAME ="test";


    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = MqConnectUtil.getConnection();
        // 从连接中创建通道
        Channel channel = connection.createChannel();
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        String message = "Hello World!";
        channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
        System.out.println(" [x] Sent '" + message + "'");
        channel.close();
        connection.close();
    }
}
