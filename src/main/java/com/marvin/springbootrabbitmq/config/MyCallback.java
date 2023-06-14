package com.marvin.springbootrabbitmq.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * @TODO:
 * @author: dengbin
 * @create: 2023-06-13 16:01
 **/
@Component
@Slf4j
public class MyCallback implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnsCallback {

    // 注入
    @Resource
    RabbitTemplate rabbitTemplate;

    /*
    @PostConstruct：
    1. Java中该注解的说明：@PostConstruct该注解被用来修饰一个非静态的void（）方法。
    2. 被@PostConstruct修饰的方法会在服务器加载Servlet的时候运行，并且只会被服务器执行一次。
    3. PostConstruct在构造函数之后执行，init（）方法之前执行。

    Constructor(构造方法) -> @Autowired(依赖注入) -> @PostConstruct(注释的方法)

    实例：
        如果想在生成对象时完成某些初始化操作，而偏偏这些初始化操作又依赖于依赖注入，那么就无法在构造函数中实现。
        为此，可以使用@PostConstruct注解一个方法来完成初始化，@PostConstruct注解的方法将会在依赖注入完成后被自动调用。
     */
    @PostConstruct
    public void setConfirmCallback() {
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnsCallback(this);
    }

    /*
     * @Description: TODO 交换机确认回调的方法
     * @Author: dengbin
     * @Date: 13/6/23 16:01
     * @param correlationData:correlationData里面保存回调消息的ID及相关消息
     * @param b: 交换机是否收到消息 --> true表示接受到了，false表示没接受到
     * @param s: 交换机接受不到的原因
     * @return: void
     **/
    @Override
        public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        String id = correlationData.getId() != null ? correlationData.getId() : null;
        if (ack) {
            log.info("交换机已经收到了消息，id为{}", id);
        } else {
            log.error("交换没有收到消息，原因是{}", cause);
        }
    }


    /*
     * @Description: TODO 回退消息
     * @Author: dengbin
     * @Date: 13/6/23 16:36
     * @param returnedMessage:
     * @return: void
     **/
    @Override
    public void returnedMessage(ReturnedMessage returnedMessage) {

        log.error("消息：{},被交换机：{}退回，退回原因：{},路由：{}", new String(returnedMessage.getMessage().getBody()), returnedMessage.getExchange(), returnedMessage.getReplyText(), returnedMessage.getRoutingKey());

    }
}
