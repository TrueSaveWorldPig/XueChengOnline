package com.xuecheng.rabbitmq;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * rabbitMQ的入门程序
 *
 * @author 裴路明
 * @create 2020/3/21 22:00
 */
public class Producer04_topic {
    //队列
    private static final String QUEUE_INFORM_EMAIL = "queue_inform_email";
    private static final String QUEUE_INFORM_SMS = "queue_inform_sms";
    //交换机
    private static final String EXCHANGE_TOPICS_INFORM = "exchange_topics_inform";
    //routingKey
    private static final String ROUTINGKEY_EMAIL = "inform.#.email.#";
    private static final String ROUTINGKEY_SMS = "inform.#.sms.#";

    public static void main(String[] args) {
        //通过连接工厂创建新的连接来和mq建立连接
        ConnectionFactory connectionFactory = new ConnectionFactory();
        //设置参数
        connectionFactory.setHost("127.0.0.1");//ip
        connectionFactory.setPort(5672);//端口
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        //设置虚拟机一个MQ的服务可以设置多个虚拟机,每个虚拟机就相当于一个MQ
        connectionFactory.setVirtualHost("/");
        Connection connection = null;
        Channel channel = null;
        try {
            //建立新连接
            connection = connectionFactory.newConnection();
            //创建与交换机的通道,每个通道代表一个会话
            channel = connection.createChannel();
            //声明交换机String exchange, BuiltinExchangeType type
            //参数名称:
            // 1.交换机名称
            // 2.交换机类型，
            // fanout:对应的工作模式:Publish/Subscribe
            // topic:对应的工作模式:Topics
            // direct:对应的工作模式:Routing
            // headers:对应的工作模式:Header
            channel.exchangeDeclare(EXCHANGE_TOPICS_INFORM, BuiltinExchangeType.TOPIC);
            //声明队列String queue, boolean durable, boolean exclusive, boolean autoDelete, Map<String, Object> arguments
            //参数名称:
            // 1.队列名称
            // 2.是否持久化mq重启后队列还在
            // 3.是否独占连接,队列只允许在该连接中访问,如果连接关闭后就会自动删除了,设置true可用于临时队列的创建
            // 4.自动删除,队列不在使用时就自动删除,如果将此参数和exclusive参数设置为true时,就可以实现临时队列
            // 5.参数,可以设置一个队列的扩展参数,比如可以设置队列存活时间
            channel.queueDeclare(QUEUE_INFORM_SMS, true, false, false, null);
            channel.queueDeclare(QUEUE_INFORM_EMAIL, true, false, false, null);
            //进行交换机队列的绑定
            //参数:String queue, String exchange, String routingKey
            // 1. 队列名称
            // 2. 交换机名称
            // 3. 路由key
            channel.queueBind(QUEUE_INFORM_EMAIL, EXCHANGE_TOPICS_INFORM, ROUTINGKEY_EMAIL);
            channel.queueBind(QUEUE_INFORM_SMS, EXCHANGE_TOPICS_INFORM, ROUTINGKEY_SMS);
            // 发送消息String exchange, String routingKey, boolean mandatory, BasicProperties props, byte[] body
            for (int i = 0; i < 10; i++) {
                String message = "inform to user" + i;
                //向交换机发送消息String exchange, String routingKey, boolean mandatory, BasicProperties props, byte[] body
                //参数名称:
                // 1.交换机,如果不指定将会使用mq默认交换机
                // 2.路由key,交换机根据路由key来将消息转发至指定的队列,如果使用默认的交换机,routingKey设置为队列名称
                // 3.消息属性
                // 4.消息内容
                channel.basicPublish(EXCHANGE_TOPICS_INFORM, "inform.email", null, message.getBytes());
                System.out.println("Send to email: " + message);
                channel.basicPublish(EXCHANGE_TOPICS_INFORM, "inform.email.sms", null, message.getBytes());
                System.out.println("Send to email and sms: " + "      " + message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (channel != null) {
                    channel.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (IOException | TimeoutException e) {
                e.printStackTrace();
            }
        }
    }
}
