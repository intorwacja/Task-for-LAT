package org.example.task_for_lat;

import org.example.task_for_lat.entity.PromoCode;
import org.example.task_for_lat.entity.PromoCodeType;
import org.example.task_for_lat.services.PromoCodeService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import java.sql.Date;
import java.util.Arrays;

@SpringBootApplication
public class TaskForLatApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaskForLatApplication.class, args);
    }

}
