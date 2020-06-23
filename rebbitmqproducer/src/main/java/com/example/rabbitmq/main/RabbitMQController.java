package com.example.rabbitmq.main;

import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Set;


@RestController
@RequestMapping("rabbitmq/listener")
public class RabbitMQController {

    @Resource
    private RabbitListenerEndpointRegistry rabbitListenerEndpointRegistry;

    @RequestMapping("stop")





    public String stop(){
        rabbitListenerEndpointRegistry.stop();
        MsgProducer.setIsRun(new Boolean("false"));
        System.out.println("停止MQ监听");
        return "success";
    }

    @RequestMapping("start")
    public String start(){
        MsgProducer.setIsRun(new Boolean("true"));
        rabbitListenerEndpointRegistry.start();
        System.out.println("启动MQ监听");
        return "success";
    }

    @RequestMapping("setup")
    public String setup(int consumer, int maxConsumer){
        Set<String> containerIds = rabbitListenerEndpointRegistry.getListenerContainerIds();
        SimpleMessageListenerContainer container = null;
        for(String id : containerIds){
            container = (SimpleMessageListenerContainer) rabbitListenerEndpointRegistry.getListenerContainer(id);
            if(container != null){
                container.setConcurrentConsumers(consumer);
                container.setMaxConcurrentConsumers(maxConsumer);
            }
        }
        System.out.println("设置");
        return "success";
    }
}

