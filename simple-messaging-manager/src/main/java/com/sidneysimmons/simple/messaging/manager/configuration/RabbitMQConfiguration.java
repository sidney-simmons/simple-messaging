package com.sidneysimmons.simple.messaging.manager.configuration;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ configuration.
 * 
 * @author Sidney Simmons
 */
@CommonsLog
@Configuration
public class RabbitMQConfiguration {

    /**
     * Connection factory.
     * 
     * @return the connection factory
     */
    @Bean(name = "rabbitmqConnectionFactory")
    public ConnectionFactory rabbitmqConnectionFactory() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("rabbitmq");
        factory.setUsername("rabbitmq");
        factory.setPassword("password1");
        return factory;
    }

    /**
     * Connection.
     * 
     * @return the connection
     * @throws Exception thrown if the connection cannot be created
     */
    @Bean(name = "rabbitmqConnection")
    public Connection rabbitmqConnection() throws Exception {
        // Get the connection factory
        ConnectionFactory connectionFactory = rabbitmqConnectionFactory();

        // Try a few times to get the connection
        int attempt = 1;
        while (attempt <= 5) {
            log.info("Connecting to RabbitMQ server attempt " + attempt + "...");
            try {
                Connection connection = connectionFactory.newConnection();
                log.info("Connected to RabbitMQ server!");
                return connection;
            } catch (Exception e) {
                log.warn("Connection to RabbitMQ server failed! Will retry. " + e.getMessage());
                Thread.sleep(5000);
                attempt++;
            }
        }

        // Exit if we cannot connect to the server
        throw new IllegalStateException("Max number of failed RabbitMQ server connections reached. No more retries.");
    }

    /**
     * Channel.
     * 
     * @return the channel
     * @throws Exception thrown if the channel cannot be created
     */
    @Bean(name = "exampleChannel")
    public Channel exampleChannel() throws Exception {
        return rabbitmqConnection().createChannel();
    }

}
