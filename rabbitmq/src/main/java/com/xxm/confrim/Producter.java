package com.xxm.confrim;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
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
    public void test() throws IOException, TimeoutException, InterruptedException {
        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();

        //开启confirm确认机制
        channel.confirmSelect();
        //设置confirm监听
        channel.addConfirmListener(new MyConfirmListener());
/*        channel.addConfirmListener(new ConfirmListener() {
            @Override
            public void handleAck(long l, boolean b) throws IOException {
                System.out.println("handleAck : " + l + "ack");
            }

            @Override
            public void handleNack(long l, boolean b) throws IOException {
                System.out.println("handleAck : " + l + "no ack");
            }
        });*/

        //使用direct routingKey来发送消息，根据routingKey,exchange会进行路由，发送到真正的queue里
        for (int i = 0; i < 5; i++) {
            String message = "test msg " + i;
            channel.basicPublish(exchangeName, routingKey, null, message.getBytes());
        }

        //主线程睡眠，等待ack回来
        //TimeUnit.SECONDS.sleep(1000);

        /*channel.close();
        connection.close();*/
    }

}
