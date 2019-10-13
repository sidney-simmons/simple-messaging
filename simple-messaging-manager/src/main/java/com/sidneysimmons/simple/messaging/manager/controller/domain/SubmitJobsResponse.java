package com.sidneysimmons.simple.messaging.manager.controller.domain;

import lombok.Data;

/**
 * Response from submitting a given number of jobs.
 * 
 * @author Sidney Simmons
 */
@Data
public class SubmitJobsResponse {

    private Integer numberOfJobs;

}
