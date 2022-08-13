package de.mq.odesolver.impl;

import java.util.List;

import de.mq.odesolver.OdeResolver;
import de.mq.odesolver.OdeResult;


interface OdeResolverService {
	
	enum Algorithm {
		EulerPolygonal,
		RungeKutta2ndOrder,
		RungeKutta4ndOrder;
	}
	
	OdeResolver odeResolver(final Algorithm algorithm, final String function);
	
	List<OdeResult> solve(final OdeResolver odeResolver, final OdeResult start, final double end, final int steps ); 
	
	void validate(final OdeResolver odeResolver, final OdeResult start);

}