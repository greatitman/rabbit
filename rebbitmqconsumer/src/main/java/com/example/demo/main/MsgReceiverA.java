package com.example.demo.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class MsgReceiverA {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @RabbitListener(queues = {"${ldd.queue1}"})
    public void process(String content) {
        logger.info("处理器接收处理队列当中的消息： " + content);
    }
}
