# mq
mq学习之路
mq:
主要解决应用解耦，异步消息，流量削锋等问题，实现高性能，高可用，可伸缩和最终一致性架构。
----------
管理界面:rabbitmq-plugins.bat enable rabbitmq_management
重启服务器：net stop RabbitMQ && net start RabbitMQ
https://www.rabbitmq.com/getstarted.html
https://www.rabbitmq.com/tutorials/tutorial-one-java.html
http://localhost:15672/#/
https://www.rabbitmq.com/confirms.html 消息确认

参考：
https://blog.csdn.net/hellozpc/article/details/81436980#4_172
https://blog.csdn.net/cairuojin/article/details/81912033   RabbitMQ入门学习
https://blog.csdn.net/HD243608836/article/details/80217591 消息队列mq总结（重点看，比较了主流消息队列框架）
https://blog.csdn.net/alinshen/article/details/80583214  一个用消息队列 的人，不知道为啥用 MQ，这就有点尴尬
https://www.cnblogs.com/weirdo-lenovo/p/10660671.html RabbitMQ原理图
https://blog.51cto.com/wyait/1977544 rabbitMQ消息队列原理
https://www.cnblogs.com/data2value/p/6220859.html 吞吐量（TPS）、QPS、并发数、响应时间（RT）概念

-----------
<dependency>
  <groupId>com.rabbitmq</groupId>
  <artifactId>amqp-client</artifactId>
  <version>5.7.3</version>
</dependency>

1 "Hello World!"

    一个生产 一个消费
2 work queues

    一个生产 多个消费
    轮询发给消费者

    消息确认   用于当消费这挂球了。 消息丢了怎么办。
    -为了确保消息永不丢失，RabbitMQ支持 消息确认。消费者发送回一个确认（告知），告知RabbitMQ特定的消息已被接收，处理，并且RabbitMQ可以自由删除它。
    -如果消费者死了（其通道已关闭，连接已关闭或TCP连接丢失）而没有发送确认，RabbitMQ将了解消息未完全处理，并将重新排队。如果同时有其他消费者在线，
    它将很快将其重新分发给另一个消费者。这样，您可以确保即使工人偶尔死亡也不会丢失任何消息。
    -没有任何消息超时；消费者死亡时，RabbitMQ将重新传递消息。即使处理一条消息花费非常非常长的时间也没关系。

    private boolean noAck = false; 默认 com.rabbitmq.client.AMQP.Basic.Consume.Builder
    默认是false 表示需要确认

    消息确认 必须发送和返回消息确认使用同一个通道

    Forgotten acknowledgment  没有basicAck   没有进行消息确认，会导致客户端关闭时，重新发送，RabbitMQ将消耗越来越多的内存，因为它将无法释放任何未确认的消息

    确保消息不会丢失 我们需要将队列和消息都标记为持久。
    队列持久
        RabbitMQ不允许您使用不同的参数重新定义现有队列。但是有一个快速的解决方法-让我们声明一个名称不同的队列，例如task_queue：
        此queueDeclare更改需要同时应用于生产者代码和使用者代码。
        这样确保了RabbitMQ重新启动，task_queue队列也不会丢失
    消息持久
        通过将MessageProperties（实现BasicProperties）设置为值PERSISTENT_TEXT_PLAIN
        channel.basicPublish("", QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes("UTF-8"));
        将消息标记为持久性并不能完全保证不会丢失消息。尽管它告诉RabbitMQ将消息保存到磁盘，但是RabbitMQ接受消息并且尚未保存消息时，还有很短的时间。
        而且，RabbitMQ不会对每条消息都执行fsync（2）－它可能只是保存到缓存中，而没有真正写入磁盘。持久性保证并不强，但是对于我们的简单任务队列而言，
        这已经绰绰有余了。如果您需要更强有力的保证，则可以使用 发布者确认。

     持久：队列持久+消息持久+消息确认

     Fair dispatch
        默认：
            RabbitMQ在消息进入队列时才调度消息。它不会查看使用者的未确认消息数。它只是盲目地将每第n条消息发送给第n个使用者。
        修改：
            int prefetchCount = 1 ;
            channel.basicQos（prefetchCount）;
            表示在处理并确认上一条消息之前，不要将新消息发送给工作人员。而是将其分派给不忙的下一个工作程序。
3 Publish/Subscribe  发布/订阅

    Exchanges
    消息模型中  生产者将消息发送给rabbitmq服务器，生成者经常不知道发送给那个队列， 生产者只能将消息发送给Exchange
    Exchange一方面接收生产者的消息 一方面将消息推到队列
    所以Exchange 必须知道如何处理收到的消息，是否应将其附加到特定队列？是否应该将其附加到许多队列中？还是应该丢弃它。
    规则由交换类型定义 。
    类型：direct topic headers fanout
    fanout exchange（广播交换机） 将接收的消息广播到它所有知道的队列，

    Exchanges 生产者和消费者一样

    根据消费程序中会创建一个自定义的mq



