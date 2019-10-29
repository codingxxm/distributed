package com.xxm.fanout;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author CodingXXM
 * @desc
 * @date 2019/10/28 23:32
 **/
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-test.xml"})
public class Producter {

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

    @Value("${fanout.exchange}")
    private String exchangeName;


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
    public void test() throws IOException, TimeoutException {
        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();

        //不需要routingKey，exchange会进行路由，发送到绑定的队列上
        for (int i = 0; i < 5; i++) {
            String message = "test fanout msg " + i;
            channel.basicPublish(exchangeName, "", null, message.getBytes());
        }

        channel.close();
        connection.close();
    }

}
