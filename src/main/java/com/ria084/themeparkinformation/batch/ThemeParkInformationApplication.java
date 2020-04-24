package com.ria084.themeparkinformation.batch;

import com.ria084.themeparkinformation.batch.service.ThemeParkInfomationService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class ThemeParkInformationApplication implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(ThemeParkInformationApplication.class, args);
    }

    @Override
    public void run(String... args) {
        try {
            ThemeParkInfomationService.run(args);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
