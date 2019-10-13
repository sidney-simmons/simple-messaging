package com.sidneysimmons.simple.messaging.manager.controller;

import com.sidneysimmons.simple.messaging.manager.controller.domain.NotifyAllRequest;
import com.sidneysimmons.simple.messaging.manager.controller.domain.NotifyAllResponse;
import com.sidneysimmons.simple.messaging.manager.controller.domain.SubmitJobsRequest;
import com.sidneysimmons.simple.messaging.manager.controller.domain.SubmitJobsResponse;
import com.sidneysimmons.simple.messaging.manager.service.MessagingService;
import com.sidneysimmons.simple.messaging.manager.service.domain.Job;
import com.sidneysimmons.simple.messaging.manager.service.domain.NotifyAllMessage;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for interacting with a manager.
 * 
 * @author Sidney Simmons
 */
@RestController
@RequestMapping(value = "/manager", produces = MediaType.APPLICATION_JSON_VALUE)
public class ManagerController {

    @Resource(name = "messagingService")
    private MessagingService messagingService;

    /**
     * Notify all workers.
     * 
     * @param request the request
     * @return the response
     * @throws IOException thrown if there is a problem notifying all the workers
     */
    @PostMapping(value = "/notify-all")
    public NotifyAllResponse notifyAll(@RequestBody NotifyAllRequest request) throws IOException {
        // Send the request
        NotifyAllMessage sentMessage = messagingService.sendToFanoutExchange(request);

        // Return the details in a response
        NotifyAllResponse response = new NotifyAllResponse();
        response.setMessage(sentMessage.getMessage());
        response.setDateTimeSent(sentMessage.getDateTimeSent());
        return response;
    }

    /**
     * Submit a given number of jobs.
     * 
     * @param request the request
     * @return the response
     * @throws UnsupportedEncodingException thrown if there is a problem submitting the jobs
     * @throws IOException thrown if there is a problem submitting the jobs
     */
    @PostMapping(value = "/submit-jobs")
    public SubmitJobsResponse submitJobs(@RequestBody SubmitJobsRequest request)
            throws UnsupportedEncodingException, IOException {
        // Submit the jobs
        List<Job> submittedJobs = messagingService.sendToWorkQueue(request);

        // Return the details in a response
        SubmitJobsResponse response = new SubmitJobsResponse();
        response.setNumberOfJobs(submittedJobs.size());
        return response;
    }

}
