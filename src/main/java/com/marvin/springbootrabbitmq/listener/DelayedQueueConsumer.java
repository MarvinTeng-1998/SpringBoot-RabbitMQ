package com.marvin.springbootrabbitmq.listener;

import com.marvin.springbootrabbitmq.config.DelayedQueueConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @TODO: 消费者 消费基于插件的延迟消息
 * @author: dengbin
 * @create: 2023-06-13 14:46
 **/
@Component
@Slf4j
public class DelayedQueueConsumer {

    //监听消息
    @RabbitListener(queues = DelayedQueueConfig.DELAYED_QUEUE_NAME)
    public void receiveDelayQueue(Message message){
        String msg = new String(message.getBody());
        log.info("当前时间：{}，收到延迟队列的消息是:{}",new Date().toString(),msg);
    }

}
