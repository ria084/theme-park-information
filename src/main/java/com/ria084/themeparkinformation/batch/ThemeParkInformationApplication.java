package com.ria084.themeparkinformation.batch;

import com.ria084.themeparkinformation.batch.service.ThemeParkInfomationService;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
@AllArgsConstructor
public class ThemeParkInformationApplication implements CommandLineRunner {

    private final ThemeParkInfomationService infoService;

    public static void main(String[] args) {
        SpringApplication.run(ThemeParkInformationApplication.class, args);
    }

    @Override
    public void run(String... args) {
        infoService.run(args);
    }
}
