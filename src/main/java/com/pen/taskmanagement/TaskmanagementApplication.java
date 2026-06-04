package com.pen.taskmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvEntry;

@SpringBootApplication
public class TaskmanagementApplication {

	public static void main(String[] args) {

		Dotenv dotenv = Dotenv.configure().load();
		for (DotenvEntry doten : dotenv.entries()) {
			System.setProperty(doten.getKey(), doten.getValue());
		}
		SpringApplication.run(TaskmanagementApplication.class, args);
	}

}
