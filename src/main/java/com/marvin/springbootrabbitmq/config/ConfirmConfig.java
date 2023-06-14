package com.marvin.springbootrabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @TODO:
 * @author: dengbin
 * @create: 2023-06-13 15:37
 **/
@Configuration
public class ConfirmConfig {

    // 交换机
    public static final String CONFIRM_EXCHANGE_NAME = "confirm.exchange";
    //队列
    public static final String CONFIRM_QUEUE_NAME = "confirm.queue";
    //routing key
    public static final String CONFIRM_ROUTING_KEY = "key1";
    // 备份交换机
    public static final String BACKUP_EXCHANGE_NAME = "backup.exchange";
    // 备份队列
    public static final String BACKUP_QUEUE_NAME = "backup.queue";
    // 报警队列
    public static final String WARNING_QUEUE_NAME = "warning.queue";

    @Bean("confirmExchange")
    public DirectExchange confirmExchange(){

        // 主要交换机的创建方式，并写上了备份交换机
        return ExchangeBuilder.directExchange(CONFIRM_EXCHANGE_NAME).durable(true).withArgument("alternate-exchange",BACKUP_EXCHANGE_NAME).build();
    }
    @Bean("confirmQueue")
    public Queue confirmQueue(){
        return QueueBuilder.durable(CONFIRM_QUEUE_NAME).build();
    }
    @Bean
    public Binding confirmExchangeBindingConfirmQueue(@Qualifier("confirmExchange") DirectExchange directExchange,
                                                      @Qualifier("confirmQueue") Queue confirmQueue){
        return BindingBuilder.bind(confirmQueue).to(directExchange).with(CONFIRM_ROUTING_KEY);
    }

    @Bean("backupExchange")
    public FanoutExchange backupExchange() { return new FanoutExchange(BACKUP_EXCHANGE_NAME);};

    @Bean("backupQueue")
    public Queue backupQueue() { return QueueBuilder.durable(BACKUP_QUEUE_NAME).build();}

    @Bean("warningQueue")
    public Queue warningQueue() { return QueueBuilder.durable(WARNING_QUEUE_NAME).build();}
    @Bean
    public Binding backupExchangeBindingBackupQueue(@Qualifier("backupExchange") FanoutExchange fanoutExchange,
                                                    @Qualifier("backupQueue") Queue backupQueue){
        return BindingBuilder.bind(backupQueue).to(fanoutExchange);
    }
    @Bean
    public Binding backupExchangeBindingWarningQueue(@Qualifier("backupExchange") FanoutExchange fanoutExchange,
                                                     @Qualifier("warningQueue") Queue warningQueue){
        return BindingBuilder.bind(warningQueue).to(fanoutExchange);
    }
}
