package com.example.nats.config;
import io.nats.client.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

@Configuration
public class NatsConfiguration {

    private Connection natsConnection;

    @Bean
    public Connection natsConnection() throws IOException, InterruptedException {
        this.natsConnection = Nats.connect("nats://localhost:4222");
        return natsConnection;
    }

    @PreDestroy
    public void cleanUp() throws InterruptedException {
        if (natsConnection != null) {
            natsConnection.close();
        }
    }
}
