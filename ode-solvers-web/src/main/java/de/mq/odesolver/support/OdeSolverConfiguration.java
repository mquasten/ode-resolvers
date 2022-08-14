package de.mq.odesolver.support;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import de.mq.odesolver.OdeSolverService;


@Configuration
class OdeSolverConfiguration {
	
	@Bean
	OdeSolverService odeSolverService () {
		return new OdeSolverServiceImpl(new OdeFunctionUtil());
	}

}
