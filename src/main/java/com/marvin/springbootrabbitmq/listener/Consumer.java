package com.marvin.springbootrabbitmq.listener;

import com.marvin.springbootrabbitmq.config.ConfirmConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @TODO:
 * @author: dengbin
 * @create: 2023-06-13 15:46
 **/
@Component
@Slf4j
public class Consumer {
    @RabbitListener(queues = ConfirmConfig.CONFIRM_QUEUE_NAME)
    public void receiveConfirmMessage(Message message){
        String msg = new String(message.getBody());
        log.info("接收到的队列confirm.queue的消息{}",msg);
    }
}
