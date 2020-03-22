package com.xuecheng.test.rabbitmq.mq;

import com.rabbitmq.client.Channel;
import com.xuecheng.test.rabbitmq.config.RabbitMQConfig;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author 裴路明
 * @create 2020/3/22 20:04
 */
@Component
public class ReceiveHandler {
    // 监听email队列
    @RabbitListener(queues = {RabbitMQConfig.QUEUE_INFORM_EMAIL})
    public void receiveEmail(String msg, Message message, Channel channel) {
        System.out.println("receive message is :" + msg);
    }

    // 监听sms队列
    @RabbitListener(queues = {RabbitMQConfig.QUEUE_INFORM_SMS})
    public void receiveSms(String msg, Message message, Channel channel) {
        System.out.println("receive message is :" + msg);
    }
}
