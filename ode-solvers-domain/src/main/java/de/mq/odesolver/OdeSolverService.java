package de.mq.odesolver;


public interface OdeSolverService {
	
	enum Algorithm {
		EulerPolygonal,
		RungeKutta2ndOrder,
		RungeKutta4ndOrder;
	}
	
	OdeResolver odeResolver(final Algorithm algorithm, final String function);
	
	double validateRightSide(final String function, final OdeResult start);

}