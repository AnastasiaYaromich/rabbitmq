package com.yaromich.rabbitmq.consumer;

import com.rabbitmq.client.*;

import java.util.Scanner;

public class SimpleReceiverApp {

    private final static String QUEUE_NAME = "blogMessages";
    private final static String EXCHANGE_NAME = "itExchanger";


    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();


        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        Scanner sc = new Scanner(System.in);
        System.out.println(" [*] Waiting for messages");
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(Thread.currentThread().getName() + " [x] Received '" + message + "'" + " about " + delivery.getEnvelope().getRoutingKey());
        };

        channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> {
        });


        while (true) {
            String command = sc.nextLine();
            int i= command.indexOf(" ");

            String programmingLang = command.substring(i + 1);
            command = command.substring(0, i);
            if (command.equals("set_topic")) {
                channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, programmingLang);
            } else if (command.equals("delete_topic")) {
                channel.queueUnbind(QUEUE_NAME, EXCHANGE_NAME, programmingLang);
            }
        }
    }
}
