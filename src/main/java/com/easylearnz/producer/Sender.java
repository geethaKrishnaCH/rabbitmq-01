package com.easylearnz.producer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class Sender {

    private static final String QUEUE_NAME = "greeting";

    public static void main(String[] args) {
        // 1. Create a ConnectionFactory
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("user");
        connectionFactory.setPassword("password");

        try (
                // 2. Establish a Connection
                Connection connection = connectionFactory.newConnection();
                // 3. Create a Channel
                Channel channel = connection.createChannel();
        ) {
            // 4. Declare a Queue
            // queueDeclare(queue, durable, exclusive, autoDelete, arguments)
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);

            // The message payload
            String message = "Hello, Jhansi";

            // 5. Publish the Message
            // basicPublish(exchange, routingKey, props, body)
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes(StandardCharsets.UTF_8));
        } catch (IOException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }
}
