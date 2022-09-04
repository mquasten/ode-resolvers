package de.mq.odesolver.support;

import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Component;

@Component
public interface OdeSessionModelRepository {
	
	@Lookup
	OdeSessionModel odeSessionModel();

}
