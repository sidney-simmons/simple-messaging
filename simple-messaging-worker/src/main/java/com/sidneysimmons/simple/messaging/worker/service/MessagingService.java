package com.sidneysimmons.simple.messaging.worker.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.sidneysimmons.simple.messaging.worker.service.domain.Job;
import com.sidneysimmons.simple.messaging.worker.service.domain.NotifyAllMessage;
import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import javax.annotation.Resource;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.stereotype.Service;

/**
 * Messaging service.
 * 
 * @author Sidney Simmons
 */
@CommonsLog
@Service("messagingService")
public class MessagingService {

    @Resource(name = "exampleChannel")
    private Channel channel;

    @Resource(name = "objectMapper")
    private ObjectMapper objectMapper;

    private static final String FANOUT_EXCHANGE_NAME = "fanout-exchange";

    private static final String WORK_QUEUE_NAME = "work-queue";

    /**
     * Listen to the fanout exchange.
     * 
     * @throws IOException thrown if there is a problem listening to the fanout exchange
     */
    public void listenToFanoutExchange() throws IOException {
        // Declare the exchange
        channel.exchangeDeclare(FANOUT_EXCHANGE_NAME, "fanout");

        // Create a temporary queue to connect to the exchange
        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, FANOUT_EXCHANGE_NAME, "");

        // Listen for messages
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            NotifyAllMessage message = objectMapper.readValue(delivery.getBody(), NotifyAllMessage.class);
            ZonedDateTime dateTimeSent = message.getDateTimeSent().atZone(ZoneId.systemDefault());
            String formattedDateTimeSent = dateTimeSent.format(DateTimeFormatter.ISO_DATE_TIME);
            log.info("Received fanout: " + message.getMessage() + " - " + formattedDateTimeSent);
        };
        CancelCallback cancelCallback = (consumerTag) -> {

        };
        channel.basicConsume(queueName, true, deliverCallback, cancelCallback);
    }

    /**
     * Listen to the work queue.
     * 
     * @throws IOException thrown if there is a problem listening to the work queue
     */
    public void listenToWorkQueue() throws IOException {
        // Declare the queue
        channel.queueDeclare(WORK_QUEUE_NAME, true, false, false, null);

        // Only accept one message at a time
        channel.basicQos(1);

        // Listen for jobs
        Random random = new Random();
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            Job job = objectMapper.readValue(delivery.getBody(), Job.class);
            try {
                Thread.sleep(random.nextInt(2500) + 250);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                log.info(job.getNumber() + " -> Completed!");
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            }
        };
        CancelCallback cancelCallback = (consumerTag) -> {

        };
        channel.basicConsume(WORK_QUEUE_NAME, false, deliverCallback, cancelCallback);
    }

}
