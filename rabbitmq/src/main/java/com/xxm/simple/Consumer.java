package com.xxm.simple;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author CodingXXM
 * @desc
 * @date 2019/10/28 23:32
 **/
public class Consumer {
    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();

        connectionFactory.setHost("192.168.1.11");
        connectionFactory.setPort(5672);

        connectionFactory.setVirtualHost("/codingxxm");
        connectionFactory.setUsername("scott");
        connectionFactory.setPassword("tiger");

        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();

        String queueName = "test-queue-1";
        channel.queueDeclare(queueName, true, false, true, null);

        DefaultConsumer defaultConsumer = new DefaultConsumer(channel);
        channel.basicConsume(queueName, true, defaultConsumer);

        while (true) {

        }
    }
}
