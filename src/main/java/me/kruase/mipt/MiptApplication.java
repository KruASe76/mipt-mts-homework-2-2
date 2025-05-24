package me.kruase.mipt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication()
@EnableAsync
public class MiptApplication {
    public static void main(String[] args) {
        SpringApplication.run(MiptApplication.class, args);
    }
}
