package com.asparagus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

import com.asparagus.service.CliService;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(CliService cliService) {
        return args -> {
            if (args.length > 0) {
                String command = String.join(" ", args);
                cliService.executeCommand(command);
            } else {
                cliService.executeCommand("");
            }
        };
    }
} 