package com.example.harmony;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@EnableJpaAuditing
@SpringBootApplication
public class HarmonyApplication {

    public static void main(String[] args) {
        SpringApplication.run(HarmonyApplication.class, args);
    }

    @PostConstruct
    public void postConstruct() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
    }
}
