package com.learntrack.course_engine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.learntrack.course_engine", "com.learntrack.course_engine.config"})
public class CourseEngineApplication {

	public static void main(String[] args) {
		SpringApplication.run(CourseEngineApplication.class, args);
	}

}
