package com.sidneysimmons.simple.messaging.manager.controller.domain;

import lombok.Data;

/**
 * Request to submit a given number of jobs.
 * 
 * @author Sidney Simmons
 */
@Data
public class SubmitJobsRequest {

    private Integer numberOfJobs;

}
