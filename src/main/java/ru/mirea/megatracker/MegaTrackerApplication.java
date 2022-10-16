package ru.mirea.megatracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MegaTrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(MegaTrackerApplication.class, args);
	}
}
