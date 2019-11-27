package com.nojava.rabbitmq._02workqueues;

import com.nojava.rabbitmq.MqConnectUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.MessageProperties;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class mq_workqueues_p {

//    private final static String QUEUE_NAME ="workqueue01";
    private final static String QUEUE_NAME ="task_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        for(int i=1;i<=10;i++){
            Connection connection = MqConnectUtil.getConnection();
            // 从连接中创建通道
            Channel channel = connection.createChannel();
            // 声明队列【参数说明：参数一：队列名称，参数二：是否持久化；参数三：是否独占模式；参数四：消费者断开连接时是否删除队列；参数五：消息其他参数】
//        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            boolean durable = true;
            channel.queueDeclare(QUEUE_NAME, durable, false, false, null);
            String message = "Hello World!"+i;
            // 发送内容【参数说明：参数一：交换机名称；参数二：队列名称，参数三：消息的其他属性-routing headers，此属性为MessageProperties.PERSISTENT_TEXT_PLAIN用于设置纯文本消息存储到硬盘；参数四：消息主体】
            channel.basicPublish("", QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes("UTF-8"));
            System.out.println(" [x] Sent '" + message + "'");
            channel.close();
            connection.close();
        }
    }
}
