package com.nojava.rabbitmq._02workqueues;

import com.nojava.rabbitmq.CommonUtils;
import com.nojava.rabbitmq.MqConnectUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class mq_workqueues_c1 {

//    private final static String QUEUE_NAME ="workqueue01";
    private final static String QUEUE_NAME ="task_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = MqConnectUtil.getConnection();
        // 从连接中创建通道
        Channel channel = connection.createChannel();
        // 声明队列【参数说明：参数一：队列名称，参数二：是否持久化；参数三：是否独占模式；参数四：消费者断开连接时是否删除队列；参数五：消息其他参数】
//        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        boolean durable = true;
        channel.queueDeclare(QUEUE_NAME, durable, false, false, null);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
        channel.basicQos(1); //一次仅接受一条未经确认的消息（请参见下文）
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [x] Received '" + message + "'");
            try {
                CommonUtils.doWork(message);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                System.out.println(" [x] Done");
                //消息确认
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            }
        };

//        boolean autoAck = true; // acknowledgment is covered below  private boolean noAck = false; 默认 com.rabbitmq.client.AMQP.Basic.Consume.Builder
        boolean autoAck = false; //手动消息确认
        // 创建订阅器，并接受消息
        channel.basicConsume(QUEUE_NAME, autoAck, deliverCallback, consumerTag -> { });

    }


}
