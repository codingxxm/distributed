package com.xxm.direct;

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

    @Value("${direct.exchange}")
    private String exchangeName;

    @Value("${direct.routingkey}")
    private String routingKey;

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

        //使用direct routingKey来发送消息，根据routingKey,exchange会进行路由，发送到真正的queue里
        for (int i = 0; i < 5; i++) {
            String message = "test msg " + i;
            channel.basicPublish(exchangeName, routingKey, null, message.getBytes());
        }

        channel.close();
        connection.close();
    }

}
