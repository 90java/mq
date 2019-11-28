package com.nojava.rabbitmq._05topics;

import com.nojava.rabbitmq.MqConnectUtil;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;

public class mq_topics_c2 {

    private static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] argv) throws Exception {
        Connection connection = MqConnectUtil.getConnection();
        Channel channel = connection.createChannel();
        //设置交换机名称和交换机类型
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);

        //不向queueDeclare（）提供任何参数  创建一个非持久的，排他的，自动删除的队列
        String queueName = channel.queueDeclare().getQueue();

        if (argv.length < 1) {
            System.err.println("Usage: ReceiveLogsTopic [binding_key]...");
            System.exit(1);
        }

        for (String bindingKey  : argv) {
            //告诉交换机将消息发送到我们的队列 交换和队列之间的关系称为绑定。 bind   severity绑定关系（bindingkey）
            // 这个只和routingkey相同  这里的队列时随机所以绑定也是随机的。 我们做的时候应该将队列名自定义？这样就可以知道那个队列解决那中类型消息
            channel.queueBind(queueName, EXCHANGE_NAME, bindingKey);
        }
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [x] Received '" + delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
        };
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> { });
    }

}
