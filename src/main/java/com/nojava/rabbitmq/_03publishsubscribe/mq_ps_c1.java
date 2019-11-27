package com.nojava.rabbitmq._03publishsubscribe;

import com.nojava.rabbitmq.CommonUtils;
import com.nojava.rabbitmq.MqConnectUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class mq_ps_c1 {

    private static final String EXCHANGE_NAME = "logs";

    public static void main(String[] argv) throws Exception {
        Connection connection = MqConnectUtil.getConnection();
        Channel channel = connection.createChannel();
        //设置交换机名称和交换机类型
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
        //不向queueDeclare（）提供任何参数  创建一个非持久的，排他的，自动删除的队列
        String queueName = channel.queueDeclare().getQueue();
        //告诉交换机将消息发送到我们的队列 交换和队列之间的关系称为绑定。 bind
        channel.queueBind(queueName, EXCHANGE_NAME, "");

        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [x] Received '" + message + "'");
        };
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> { });
    }

}
