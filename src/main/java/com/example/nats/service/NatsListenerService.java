package com.example.nats.service;
import io.nats.client.Connection;
import io.nats.client.Message;
import io.nats.client.Subscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

@Service
public class NatsListenerService {

    @Autowired
    private Connection natsConnection;

    @PostConstruct
    private void subscribeToSubjects() {
        Subscription sub = natsConnection.subscribe("greetings");

        new Thread(() -> {
            while (!Thread.interrupted()) {
                try {

                    Message msg = sub.nextMessage(Duration.ofSeconds(5));
                    if (msg != null) {

                        String message = new String(msg.getData(), StandardCharsets.UTF_8);
                        System.out.printf("Received message '%s' on subject '%s'%n", message, msg.getSubject());
                    }
                } catch (InterruptedException e) {

                    Thread.currentThread().interrupt();
                }
            }
        }).start();
    }
}
