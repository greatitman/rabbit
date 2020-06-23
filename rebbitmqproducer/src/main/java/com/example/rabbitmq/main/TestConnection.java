package com.example.rabbitmq.main;

import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.sound.midi.Soundbank;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
@Component
public class TestConnection {

    @Value("${spring.rabbitmq.host}")
    String mqHost;
    @Value("${spring.rabbitmq.port}")
    int mqPort;
    private static boolean isRun=true;



    @Resource
    private RabbitListenerEndpointRegistry rabbitListenerEndpointRegistry;
    private boolean mqStatus=false;//MQ 状态，默认不启动

    @Scheduled(fixedDelay=1000)//1s执行1次此方法;
    public void testing() {
        //连接是否正常的变量
        boolean b=false;
        if (isRun){
            b = this.isHostConnectable(mqHost, mqPort);
        }
        //如果连接正常，将运行状态改为fasle，以后不再执行
        if(b){
            isRun=false;
        }

        //如果连接正常，并且MQ为启动，启动mq
        if(!isRun&&!mqStatus){
            mqStatus=true;
            System.out.println("开始连接MQ");
            System.out.println(rabbitListenerEndpointRegistry.isRunning());
            rabbitListenerEndpointRegistry.start();
            MsgProducer.setIsRun(new Boolean("true"));
        }
    }



    /**
     * 判断MQ服务器，能否正常连接
     * @param host
     * @param port
     * @return
     */
    public  boolean isHostConnectable(String host, int port) {
        Socket socket = new Socket();
        try {
            socket.connect(new InetSocketAddress(host, port),2000);
        } catch (IOException e) {
            //e.printStackTrace();
            System.out.println("MQ服务未启动，或服务异常");
            return false;
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                //e.printStackTrace();
                System.out.println("MQ服务未启动，或服务异常");
            }
        }
        return true;
    }
}
