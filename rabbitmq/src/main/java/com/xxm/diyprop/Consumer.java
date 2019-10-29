package com.xxm.diyprop;

import com.rabbitmq.client.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;

/**
 * @author CodingXXM
 * @desc
 * @date 2019/10/28 23:32
 **/
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-test.xml"})
public class Consumer {

    @Value("${host}")
    private String host;

    @Value("${post}")
    private int port;

    @Value("${virtual.host}")
    private String virtualHost;

    @Value("${username}")
    private String username;

    @Value("${password}")
    private String password;

    ConnectionFactory connectionFactory = new ConnectionFactory();

    @Before
    public void init() {
        connectionFactory.setHost(host);
        connectionFactory.setPort(port);

        connectionFactory.setVirtualHost(virtualHost);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
    }

    @Test
    public void test() throws IOException, TimeoutException, InterruptedException {
        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();

        //声明一个队列
        String queueName = "queue-diyprop";
        channel.queueDeclare(queueName, true, false, true, null);

        QueueingConsumer queueingConsumer = new QueueingConsumer(channel);
        channel.basicConsume(queueName, true, queueingConsumer);


        while (true) {
            QueueingConsumer.Delivery delivery = queueingConsumer.nextDelivery();
            System.out.println("消费：" + new String(delivery.getBody()));
            System.out.println(delivery.getProperties());
            System.out.println(delivery.getProperties().getHeaders());
            System.out.println(delivery.getProperties().getAppId());
        }
    }

}
