package com.easylearnz.consumer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class Receiver {
    private static final String QUEUE_NAME = "greeting";

    public static void main(String[] args) {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("user");
        connectionFactory.setPassword("password");

        try (Connection connection = connectionFactory.newConnection();
             Channel channel = connection.createChannel()) {

            // Declare the queue here as well, it's good practice for both producer and consumer
            // to declare the queue, ensuring it exists regardless of which starts first
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            System.out.println("[*] Waiting for messages, to exit press Ctrl+C");

            // Define callback when a message is received
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                System.out.println("[*] Received - " + message);

                // Manual acknowledgment
                // channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            };


            // start consuming messages
            // basicConsume(queue, autoAck, deliverCallback, cancelCallback)
            channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> {
            });
            // Setting autoAck to true here automatically acknowledges messages once received
            // For reliable processing, set autoAck to false and use channel.basicAck() after processing.

            // Keep the main thread alive to continue listening for messages
            Thread.currentThread().join();
        } catch (IOException | TimeoutException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
