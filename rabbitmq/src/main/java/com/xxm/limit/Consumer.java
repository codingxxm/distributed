package com.xxm.limit;

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
    public void test() throws IOException, TimeoutException, InterruptedException {
        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();
        String exchangeType = "direct";
        //声明一个交换机
        //名称，类型，持久化，是否自动删除，其他参数
        channel.exchangeDeclare(exchangeName, exchangeType, true, false, null);

        //声明一个队列
        String queueName = "direct-queue";
        channel.queueDeclare(queueName, true, false, false, null);

        //队列和交换机绑定
        channel.queueBind(queueName, exchangeName, routingKey);

        //uint prefetchSize ：指定的是设定消息的大小(rabbitmq还没有该功能，所以一般是填写0表示不限制)
        //ushort perfetchCount ：表示设置消息的阈值，每次过来几条消息(一般是填写1 一条 一条的处理消息)
        //gloabl设置为ture 那么就是channel级别的限流，若为false 就是consumer级别的限制流量
        channel.basicQos(0, 1, false);

        //一次处理一条,关闭自动签收
        channel.basicConsume(queueName, false, new MyConsumer(channel));
    }

}
