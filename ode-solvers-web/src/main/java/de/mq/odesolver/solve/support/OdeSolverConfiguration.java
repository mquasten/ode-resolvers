package de.mq.odesolver.solve.support;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import de.mq.odesolver.solve.OdeSolverService;
import de.mq.odesolver.support.OdeFunctionUtil;


@Configuration
public class OdeSolverConfiguration {

	@Bean
	public OdeSolverService odeSolverService (@Qualifier("odeFunctionUtilRightSide") final OdeFunctionUtil odeFunctionUtil) {
		return new OdeSolverServiceImpl(odeFunctionUtil);
	}

	
}
