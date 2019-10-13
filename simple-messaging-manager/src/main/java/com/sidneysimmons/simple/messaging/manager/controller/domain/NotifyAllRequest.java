package com.sidneysimmons.simple.messaging.manager.controller.domain;

import lombok.Data;

/**
 * Request to notify all workers.
 * 
 * @author Sidney Simmons
 */
@Data
public class NotifyAllRequest {

    private String message;

}
