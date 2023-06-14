package com.marvin.springbootrabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @TODO:优先级队列的使用
 * @author: dengbin
 * @create: 2023-06-14 11:13
 **/
@Configuration
public class PriorityQueueConfig {


    public static final String PRIORITY_EXCHANGE = "priority-exchange";
    public static final String PRIORITY_QUEUE = "priority-queue";
    public static final String ROUTING_QUEUE = "priority.queue.#";

    @Bean("priorityQueue")
    public Queue priorityQueue(){
        Map<String,Object> map = new HashMap<>();
        map.put("x-max-priority",10);
        return QueueBuilder.durable(PRIORITY_QUEUE).withArguments(map).build();
    }

    @Bean("priorityExchange")
    public TopicExchange priorityExchange(){
        return ExchangeBuilder.topicExchange(PRIORITY_EXCHANGE).build();
    }

    @Bean
    public Binding priorityExchangeBindingQueue(@Qualifier("priorityQueue") Queue priorityQueue,
                                                @Qualifier("priorityExchange") TopicExchange topicExchange){
        return BindingBuilder.bind(priorityQueue).to(topicExchange).with(ROUTING_QUEUE);
    }

}
