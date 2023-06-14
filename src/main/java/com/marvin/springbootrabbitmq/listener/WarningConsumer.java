package com.marvin.springbootrabbitmq.listener;

import com.marvin.springbootrabbitmq.config.ConfirmConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @TODO:
 * @author: dengbin
 * @create: 2023-06-13 17:12
 **/
@Component
@Slf4j
public class WarningConsumer {

    @RabbitListener(queues = ConfirmConfig.WARNING_QUEUE_NAME)
    public void receiveWarningMessage(Message message){
        String msg = new String(message.getBody());
        log.warn("报警发现不可路由消息：{}",msg);
    }

}
