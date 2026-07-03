package com.pen.taskmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvEntry;

@SpringBootApplication
public class TaskmanagementApplication {

	public static void main(String[] args) {

<<<<<<< HEAD
		Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
=======
		Dotenv dotenv = Dotenv.configure().ignoreIfMissing().systemProperties().load();
>>>>>>> feature/deploy
		for (DotenvEntry doten : dotenv.entries()) {
			System.setProperty(doten.getKey(), doten.getValue());
		}
		SpringApplication.run(TaskmanagementApplication.class, args);
	}

}
