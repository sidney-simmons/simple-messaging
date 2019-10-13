package com.sidneysimmons.simple.messaging.manager.service.domain;

import java.time.Instant;
import lombok.Data;

/**
 * A job.
 * 
 * @author Sidney Simmons
 */
@Data
public class Job {

    private Integer number;

    private Instant dateTimeSent;

}
