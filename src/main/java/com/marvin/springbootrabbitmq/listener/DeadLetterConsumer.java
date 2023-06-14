package com.marvin.springbootrabbitmq.listener;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @TODO: TTL-消费者
 * @author: dengbin
 * @create: 2023-06-12 21:48
 **/
@Component
@Slf4j
public class DeadLetterConsumer {
    // 接受消息
    @RabbitListener(queues = "QD")
    public void receiveD(Message message, Channel channel){
        String msg = new String(message.getBody());
        log.info("当前时间：{},收到死信队列的消息：{}",new Date().toString(),msg);
    }
}
