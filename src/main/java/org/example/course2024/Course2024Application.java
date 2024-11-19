package org.example.course2024;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class Course2024Application {

    public static void main(String[] args) {
        SpringApplication.run(Course2024Application.class, args);
    }

}
