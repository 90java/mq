package com.nojava.rabbitmq._01helloworld;

import com.nojava.rabbitmq.MqConnectUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class mq_helloworld_c {

    private final static String QUEUE_NAME ="test";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = MqConnectUtil.getConnection();
        // 从连接中创建通道
        Channel channel = connection.createChannel();
        // 声明队列【参数说明：参数一：队列名称，参数二：是否持久化；参数三：是否独占模式；参数四：消费者断开连接时是否删除队列；参数五：消息其他参数】
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [x] Received '" + message + "'");
        };
        // 创建订阅器，并接受消息
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> { });

    }

}
