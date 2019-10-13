package com.sidneysimmons.simple.messaging.manager.service.domain;

import java.time.Instant;
import lombok.Data;

/**
 * A message to send to all workers.
 * 
 * @author Sidney Simmons
 */
@Data
public class NotifyAllMessage {

    private String message;

    private Instant dateTimeSent;

}
