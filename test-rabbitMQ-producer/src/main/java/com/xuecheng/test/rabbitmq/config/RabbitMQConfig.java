package com.xuecheng.test.rabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 裴路明
 * @create 2020/3/22 18:09
 */
@Configuration
public class RabbitMQConfig {
    //队列
    public static final String QUEUE_INFORM_EMAIL = "queue_inform_email";
    public static final String QUEUE_INFORM_SMS = "queue_inform_sms";
    //交换机
    public static final String EXCHANGE_TOPICS_INFORM = "exchange_topics_inform";
    //routingKey
    public static final String ROUTINGKEY_EMAIL = "inform.#.email.#";
    public static final String ROUTINGKEY_SMS = "inform.#.sms.#";


    // 声明交换机
    @Bean(EXCHANGE_TOPICS_INFORM)
    public Exchange exchangeTopic() {
        // durble(true) 持久化,mq重启之后交换机还在
        return ExchangeBuilder.topicExchange(EXCHANGE_TOPICS_INFORM).durable(true).build();
    }

    // 声明队列
    @Bean(QUEUE_INFORM_EMAIL)
    public Queue queueEmail() {
        return new Queue(QUEUE_INFORM_EMAIL);
    }

    @Bean(QUEUE_INFORM_SMS)
    public Queue queueSms() {
        return new Queue(QUEUE_INFORM_SMS);
    }

    // 绑定交换机队列
    @Bean
    public Binding binding_email(@Qualifier(QUEUE_INFORM_EMAIL) Queue queue,
                                 @Qualifier(EXCHANGE_TOPICS_INFORM) Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTINGKEY_EMAIL).noargs();
    }

    @Bean
    public Binding binding_sms(@Qualifier(QUEUE_INFORM_SMS) Queue queue,
                               @Qualifier(EXCHANGE_TOPICS_INFORM) Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTINGKEY_SMS).noargs();
    }
}
