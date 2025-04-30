package com.ecotrack.ecotrack;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EcotrackApplication {

	public static void main(String[] args) {
		SpringApplication application = new SpringApplication(EcotrackApplication.class);

		String env = System.getenv("SPRING_PROFILES_ACTIVE");
		if (env == null) {
			env = "dev";
		}
		application.setAdditionalProfiles(env);

		application.run(args);
	}

}
