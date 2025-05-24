package me.kruase.mipt;

import org.springframework.boot.SpringApplication;

public class TestMiptApplication {
    public static void main(String[] args) {
        SpringApplication
                .from(MiptApplication::main)
                .run(args);
    }
}
