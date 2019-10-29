package com.xxm.diyprop;

import com.rabbitmq.client.AMQP;
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
import java.util.HashMap;
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

        //声明一个队列
        String queueName = "queue-diyprop";

        //构建自定义属性消息
        HashMap<String, Object> map = new HashMap<>();
        map.put("H1", "V1");
        map.put("H2", "V2");

        AMQP.BasicProperties basicProperties = new AMQP.BasicProperties.Builder()
                .deliveryMode(2) //2持久化，1不持久化
                .appId("test appid")
                .clusterId("test cluster id")
                .contentType("application/json")
                .contentEncoding("UTF-8")
                .headers(map)
                .build();

        //使用direct routingKey来发送消息，根据routingKey,exchange会进行路由，发送到真正的queue里
        for (int i = 0; i < 5; i++) {
            String message = "test diy prop msg " + i;
            channel.basicPublish("", queueName, basicProperties, message.getBytes());
        }

        channel.close();
        connection.close();
    }

}
