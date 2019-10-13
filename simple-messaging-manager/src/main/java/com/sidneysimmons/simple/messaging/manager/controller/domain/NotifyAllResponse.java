package com.sidneysimmons.simple.messaging.manager.controller.domain;

import java.time.Instant;
import lombok.Data;

/**
 * Response from notifying all workers.
 * 
 * @author Sidney Simmons
 */
@Data
public class NotifyAllResponse {

    private String message;

    private Instant dateTimeSent;

}
