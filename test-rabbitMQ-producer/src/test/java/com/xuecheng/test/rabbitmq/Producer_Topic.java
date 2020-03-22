package com.xuecheng.test.rabbitmq;

import com.xuecheng.test.rabbitmq.config.RabbitMQConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author 裴路明
 * @create 2020/3/22 18:27
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class Producer_Topic {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    //使用rabbitTemplate发送消息
    @Test
    public void test() {
        String message = "send email message to exChane";
        for (int i = 0; i < 10; i++) {
            message = message + i;
            // 参数:
            //1.交换机名称
            //2.routingKey
            //3.消息内容
            rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_TOPICS_INFORM, "inform.email", message);
        }


    }
}
