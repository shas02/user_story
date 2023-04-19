package com.user_story.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
public class UserStoryApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserStoryApplication.class, args);
	}

}
