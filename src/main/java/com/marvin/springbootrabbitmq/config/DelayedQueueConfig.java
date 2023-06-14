package com.marvin.springbootrabbitmq.config;

import com.rabbitmq.client.AMQP;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;


/**
 * @TODO:基于插件的延迟队列：主要是在交换机内做延迟
 * @author: dengbin
 * @create: 2023-06-13 14:28
 **/

@Configuration
public class DelayedQueueConfig {

    // 延迟交换机
    public static final String DELAYED_EXCHANGE_NAME = "delayed.exchange";

    // 延迟队列
    public static final String DELAYED_QUEUE_NAME = "delayed.queue";

    // routingKey
    public static final String DELAYED_ROUTING_KEY = "delayed.routingKey";

    @Bean
    public CustomExchange delayedExchange(){
        Map<String,Object> arguments = new HashMap<>();
        arguments.put("x-delayed-type","direct");
        return new CustomExchange(DELAYED_EXCHANGE_NAME,"x-delayed-message",true,false,arguments);
    }

    @Bean("delayedQueue")
    public Queue delayedQueue(){
        return QueueBuilder.durable(DELAYED_QUEUE_NAME).build();
    }

    @Bean
    public Binding delayedQueueBindingDelayedExchange(@Qualifier("delayedQueue") Queue delayedQueue,
                                                      @Qualifier("delayedExchange") CustomExchange delayedExchange){
        return BindingBuilder.bind(delayedQueue).to(delayedExchange).with(DELAYED_ROUTING_KEY).noargs();
    }
}
