package com.sparta.nbcampspringpersonaltask;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class NbcampSpringPersonalTaskApplication {

    public static void main(String[] args) {
        SpringApplication.run(NbcampSpringPersonalTaskApplication.class, args);
    }

}
