package com.example.rabbitmq.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MsgProducer implements RabbitTemplate.ConfirmCallback{

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    public static Boolean isRun=new Boolean("false");

    //由于rabbitTemplate的scope属性设置为ConfigurableBeanFactory.SCOPE_PROTOTYPE，所以不能自动注入
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 构造方法注入rabbitTemplate
     */
    public MsgProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        rabbitTemplate.setConfirmCallback(this); //rabbitTemplate如果为单例的话，那回调就是最后设置的内容
    }

    int j = 0;
    int i = 1;
    @Scheduled(fixedDelay=3000)//3s执行1次此方法;
    public void sendMsg() {
        if(isRun.booleanValue()){
            System.out.println("发送");
            j+=i;
            //把消息放入ROUTINGKEY_A对应的队列当中去，对应的是队列A
            rabbitTemplate.convertAndSend(RabbitmqConfig.EXCHANGE_B, RabbitmqConfig.ROUTINGKEY_B, "RabbitMQ----B--"+j);
            rabbitTemplate.convertAndSend(RabbitmqConfig.EXCHANGE_A, RabbitmqConfig.ROUTINGKEY_A, "RabbitMQ----A--"+j);
        }else{
            System.out.println("不发送");
        }
    }

    /**
     * 回调
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        logger.info(" 回调id:" + correlationData);
        if (ack) {
            logger.info("消息成功消费");
        } else {
            logger.info("消息消费失败:" + cause);
        }
    }

    public static Boolean getIsRun() {
        return isRun;
    }

    public static void setIsRun(Boolean isRun) {
        MsgProducer.isRun = isRun;
    }
}
