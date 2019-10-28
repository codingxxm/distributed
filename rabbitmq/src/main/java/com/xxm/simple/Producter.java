package com.xxm.simple;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author CodingXXM
 * @desc
 * @date 2019/10/28 23:32
 **/
public class Producter {

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();

        connectionFactory.setHost("192.168.1.11");
        connectionFactory.setPort(5672);

        connectionFactory.setVirtualHost("/codingxxm");
        connectionFactory.setUsername("scott");
        connectionFactory.setPassword("tiger");

        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();

        for (int i = 0; i < 5; i++) {
            String message = "test msg " + i;
            channel.basicPublish("", "test-queue-1", null, message.getBytes());
        }

        channel.close();
        connection.close();

    }
}
