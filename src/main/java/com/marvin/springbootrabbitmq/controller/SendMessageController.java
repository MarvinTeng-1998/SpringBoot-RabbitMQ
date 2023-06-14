package com.marvin.springbootrabbitmq.controller;

import com.marvin.springbootrabbitmq.config.DelayedQueueConfig;
import com.marvin.springbootrabbitmq.config.PriorityQueueConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;
import java.util.StringTokenizer;

/**
 * @TODO: 发送延迟消息的Controller
 * @author: dengbin
 * @create: 2023-06-12 21:43
 **/
@Slf4j
@RestController
@RequestMapping("/ttl")
public class SendMessageController {

    @Resource
    private RabbitTemplate rabbitTemplate;

    // 开始发消息
    @GetMapping("/sendMsg/{message}")
    public void sendMsg(@PathVariable String message) {
        log.info("当前时间:{},发一条信息给两个ttl队列:{}", new Date().toString(), message);

        rabbitTemplate.convertAndSend("X", "XA", "消息来自ttl为10s的队列:" + message);
        rabbitTemplate.convertAndSend("X", "XB", "消息来自ttl为40s的队列:" + message);
    }

    // 开始发消息 TTL
    @GetMapping("/sendExpirationMsg/{message}/{ttlTime}")
    public void sendExpirationMsg(@PathVariable String message,
                                   @PathVariable String ttlTime) {
        log.info("当前时间:{},发一条时长为{}ms的信息给一个ttl队列:{}", new Date().toString(), ttlTime, message);
        rabbitTemplate.convertAndSend("X", "XC", message, msg -> {
            // 设置发送消息的延迟时长
            msg.getMessageProperties().setExpiration(ttlTime);
            return msg;
        });
    }

    // 开始发消息 基于插件的，消息及延时时间
    @GetMapping("/sendDelayMessage/{message}/{delayTime}")
    public void sendDelayMessage(@PathVariable String message,
                                 @PathVariable Integer delayTime) {
        log.info("当前时间:{},发一条时长为{}ms的信息给一个delayedQueue队列:{}", new Date().toString(), delayTime, message);
        rabbitTemplate.convertAndSend(DelayedQueueConfig.DELAYED_EXCHANGE_NAME, DelayedQueueConfig.DELAYED_ROUTING_KEY, message, msg -> {
            msg.getMessageProperties().setDelay(delayTime);
            return msg;
        });
    }

    // 发送具有优先级的消息
    @GetMapping("/sendPriorityMessage/{message}/{priority}")
    public void sendPriorityMessage(@PathVariable String message,@PathVariable String priority){
        CorrelationData correlationData1 = new CorrelationData("1");
        log.info("当前时间：{}，发送一条优先级为{}的信息给到一个priorityQueue：{}",new Date().toString(),priority,message);
        rabbitTemplate.convertAndSend(PriorityQueueConfig.PRIORITY_EXCHANGE,PriorityQueueConfig.ROUTING_QUEUE,message,msg ->{
            msg.getMessageProperties().setPriority(Integer.valueOf(priority));
            return msg;
        },correlationData1);
    }
}
