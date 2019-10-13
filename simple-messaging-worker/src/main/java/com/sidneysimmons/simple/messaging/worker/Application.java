package com.sidneysimmons.simple.messaging.worker;

import com.sidneysimmons.simple.messaging.worker.service.MessagingService;
import javax.annotation.Resource;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point to the application!
 * 
 * @author Sidney Simmons
 */
@SpringBootApplication
public class Application implements CommandLineRunner {

    @Resource(name = "messagingService")
    private MessagingService messagingService;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // Start listening to the fanout exchange
        messagingService.listenToFanoutExchange();

        // Start listening to the work queue
        messagingService.listenToWorkQueue();
    }

}
