package com.sidneysimmons.simple.messaging.manager.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import com.sidneysimmons.simple.messaging.manager.controller.domain.NotifyAllRequest;
import com.sidneysimmons.simple.messaging.manager.controller.domain.SubmitJobsRequest;
import com.sidneysimmons.simple.messaging.manager.service.domain.Job;
import com.sidneysimmons.simple.messaging.manager.service.domain.NotifyAllMessage;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * Messaging service.
 * 
 * @author Sidney Simmons
 */
@Service("messagingService")
public class MessagingService {

    @Resource(name = "exampleChannel")
    private Channel channel;

    @Resource(name = "objectMapper")
    private ObjectMapper objectMapper;

    private static final String FANOUT_EXCHANGE_NAME = "fanout-exchange";

    private static final String WORK_QUEUE_NAME = "work-queue";

    /**
     * Send a notification to the fanout exchange.
     * 
     * @param request the notification request
     * @return the message that was sent
     * @throws IOException thrown if there is a problem publishing the message
     */
    public NotifyAllMessage sendToFanoutExchange(NotifyAllRequest request) throws IOException {
        // Create the message
        NotifyAllMessage message = new NotifyAllMessage();
        message.setMessage(request.getMessage());
        message.setDateTimeSent(Instant.now());

        // Send it
        channel.exchangeDeclare(FANOUT_EXCHANGE_NAME, "fanout");
        channel.basicPublish(FANOUT_EXCHANGE_NAME, "", null, objectMapper.writeValueAsBytes(message));
        return message;
    }

    /**
     * Send jobs to the work queue.
     * 
     * @param request the submit jobs request
     * @return the list of jobs that were sent
     * @throws UnsupportedEncodingException thrown if there is a problem publishing the jobs
     * @throws IOException thrown if there is a problem publishing the jobs
     */
    public List<Job> sendToWorkQueue(SubmitJobsRequest request) throws UnsupportedEncodingException, IOException {
        channel.queueDeclare(WORK_QUEUE_NAME, true, false, false, null);

        List<Job> submittedJobs = new ArrayList<>();
        for (int i = 0; i < request.getNumberOfJobs(); i++) {
            // Create the job
            Job job = new Job();
            job.setNumber(i + 1);
            job.setDateTimeSent(Instant.now());

            // Send the job
            channel.basicPublish("", WORK_QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN,
                    objectMapper.writeValueAsBytes(job));
            submittedJobs.add(job);
        }
        return submittedJobs;
    }

}
