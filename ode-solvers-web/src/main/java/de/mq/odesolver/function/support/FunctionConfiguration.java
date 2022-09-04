package de.mq.odesolver.function.support;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import de.mq.odesolver.function.FunctionService;

@Configuration
class FunctionConfiguration {
	
	@Bean
	FunctionService functionService() {
		return new FunctionServiceImpl();
		
	}

}
