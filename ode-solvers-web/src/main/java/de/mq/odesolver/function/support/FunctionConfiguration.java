package de.mq.odesolver.function.support;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import de.mq.odesolver.function.FunctionService;
import de.mq.odesolver.support.OdeFunctionUtil;

@Configuration
class FunctionConfiguration {
	
	@Bean
	FunctionService functionService(@Qualifier("odeFunctionUtilParameterVector") final OdeFunctionUtil odeFunctionUtil) {
		return new FunctionServiceImpl(odeFunctionUtil);
		
	}

}
