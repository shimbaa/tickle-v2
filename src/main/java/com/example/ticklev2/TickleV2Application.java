package com.example.ticklev2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TickleV2Application {

    public static void main(String[] args) {
        SpringApplication.run(TickleV2Application.class, args);
    }

}
