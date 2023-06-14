package com.marvin.springbootrabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @TODO: TTL队列，配置文件类代码
 * @author: dengbin
 * @create: 2023-06-12 21:27
 **/
@Configuration
public class TTLQueueConfig {

    public static final String NORMAL_EXCHANGE_X = "X";
    public static final String DEAD_EXCHANGE_Y = "Y";
    public static final String NORMAL_QUEUE_A = "QA";
    public static final String NORMAL_QUEUE_B = "QB";
    public static final String DEAD_QUEUE_D = "QD";
    public static final String NORMAL_QUEUE_C = "QC";


    @Bean("xExchange")
    public DirectExchange xExchange(){
        return new DirectExchange(NORMAL_EXCHANGE_X);
    }

    @Bean("yExchange")
    public DirectExchange yExchange(){
        return new DirectExchange(DEAD_EXCHANGE_Y);
    }

    // 声明普通队列，TTL-10s
    @Bean("queueA")
    public Queue queueA(){
        Map<String,Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange",DEAD_EXCHANGE_Y);
        arguments.put("x-dead-letter-routing-key","YD");
        arguments.put("x-message-ttl",1000);
        return QueueBuilder.durable(NORMAL_QUEUE_A).withArguments(arguments).build();
    }

    // 声明普通队列，TTL-40s
    @Bean("queueB")
    public Queue queueB(){
        Map<String,Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange",DEAD_EXCHANGE_Y);
        arguments.put("x-dead-letter-routing-key","YD");
        arguments.put("x-message-ttl",4000);
        return QueueBuilder.durable(NORMAL_QUEUE_B).withArguments(arguments).build();
    }

    @Bean("queueC")
    public Queue queueC(){
        Map<String,Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange",DEAD_EXCHANGE_Y);
        arguments.put("x-dead-letter-routing-key","YD");
        return QueueBuilder.durable(NORMAL_QUEUE_C).withArguments(arguments).build();
    }

    // 声明死信队列
    @Bean("queueD")
    public Queue queueD(){
        return QueueBuilder.durable(DEAD_QUEUE_D).build();
    }

    @Bean
    public Binding queueABindingExchangeX(@Qualifier("queueA") Queue queueA,
                                  @Qualifier("xExchange") DirectExchange xExchange){
        return BindingBuilder.bind(queueA).to(xExchange).with("XA");
    }

    @Bean
    public Binding queueBBindingExchangeX(@Qualifier("queueB") Queue queueB,
                                          @Qualifier("xExchange") DirectExchange xExchange){
        return BindingBuilder.bind(queueB).to(xExchange).with("XB");
    }

    @Bean
    public Binding queueCBindingExchangeX(@Qualifier("queueC") Queue queueC,
                                          @Qualifier("xExchange") DirectExchange xExchange){
        return BindingBuilder.bind(queueC).to(xExchange).with("XC");
    }

    @Bean
    public Binding queueDBindingExchangeY(@Qualifier("queueD") Queue queueD,
                                          @Qualifier("yExchange") DirectExchange yExchange){
        return BindingBuilder.bind(queueD).to(yExchange).with("YD");
    }


}
