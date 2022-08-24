package de.mq.odesolver.support;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import de.mq.odesolver.support.OdeFunctionUtil.Language;

@Configuration
class OdeFunctionUtilConfiguration {
	
	@Bean
	OdeFunctionUtil odeFunctionUtilRightSide() {
		return new OdeFunctionUtilImpl(Language.Groovy);
	}
	
	@Bean
	OdeFunctionUtil odeFunctionUtilParameterVector() {
		return new OdeFunctionUtilImpl(Language.Groovy,"k");
	}

}
