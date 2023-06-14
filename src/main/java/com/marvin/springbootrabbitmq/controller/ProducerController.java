package com.marvin.springbootrabbitmq.controller;

import com.marvin.springbootrabbitmq.config.ConfirmConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @TODO:
 * @author: dengbin
 * @create: 2023-06-13 15:43
 **/
@RestController
@RequestMapping(("/confirm"))
@Slf4j
public class ProducerController {

    @Resource
    RabbitTemplate rabbitTemplate;

    //发布确认
    @GetMapping("/sendMessage/{message}")
    public void sendMessage(@PathVariable String message){
        // 消息确认对象
        CorrelationData correlationData1 = new CorrelationData("1");

        rabbitTemplate.convertAndSend(ConfirmConfig.CONFIRM_EXCHANGE_NAME ,ConfirmConfig.CONFIRM_ROUTING_KEY,message,correlationData1);
        log.info("发送消息的内容:{}",message);

        CorrelationData correlationData2 = new CorrelationData("2");

        rabbitTemplate.convertAndSend(ConfirmConfig.CONFIRM_EXCHANGE_NAME,ConfirmConfig.CONFIRM_ROUTING_KEY + "111",message,correlationData2);
        log.info("发送消息的内容:{}",message);
    }

}
