package de.mq.odesolver.solve;


public interface OdeSolverService {
	
	enum Algorithm {
		EulerPolygonal,
		RungeKutta2ndOrder,
		RungeKutta4ndOrder;
	}
	
	OdeSolver odeResolver(final Algorithm algorithm, final String function);
	
	double validateRightSide(final String function, final double y0[], final double x0 );

}