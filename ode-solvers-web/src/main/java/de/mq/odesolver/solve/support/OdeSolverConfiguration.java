package de.mq.odesolver.solve.support;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import de.mq.odesolver.solve.OdeSolverService;
import de.mq.odesolver.solve.support.OdeFunctionUtil.Language;


@Configuration
class OdeSolverConfiguration {
	
	
	
	@Bean
	OdeSolverService odeSolverService () {
		return new OdeSolverServiceImpl(new OdeFunctionUtil(Language.Groovy));
	}
	
}
