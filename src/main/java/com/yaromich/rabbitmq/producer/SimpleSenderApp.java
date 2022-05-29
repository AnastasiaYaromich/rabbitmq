package com.yaromich.rabbitmq.producer;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

public class SimpleSenderApp {
    private final static String QUEUE_NAME = "blogMessages";
    private final static String EXCHANGE_NAME = "itExchanger";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {

             channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
             channel.queueDeclare(QUEUE_NAME, false, false, false, null);
             channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "python");


            Scanner scanner = new Scanner(System.in);
            while (true) {
                String message = scanner.nextLine();
                int i = message.indexOf(" ");
                String programmingLang = message.substring(0, i);
                message = message.substring(i + 1);
                channel.basicPublish(EXCHANGE_NAME, programmingLang, null, message.getBytes());
            }
        }
        }
}
