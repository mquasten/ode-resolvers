package de.mq.odesolver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public abstract class  ServingWebContentApplication {

	public static  void main(final String[] args) {
		SpringApplication.run(ServingWebContentApplication.class, args);
	}

}
