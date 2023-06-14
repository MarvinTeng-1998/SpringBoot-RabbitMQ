package com.marvin.springbootrabbitmq.listener;

import com.marvin.springbootrabbitmq.config.ConfirmConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @TODO:
 * @author: dengbin
 * @create: 2023-06-13 17:17
 **/
@Component
@Slf4j
public class BackupConsumer {

    @RabbitListener(queues = ConfirmConfig.BACKUP_QUEUE_NAME)
    public void receiveBackupQueue(Message message){
        String msg = new String(message.getBody());
        log.info("备份队列已经收到消息了：{}",msg);
    }
}
