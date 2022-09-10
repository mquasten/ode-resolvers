package de.mq.odesolver.solve;

import de.mq.odesolver.solve.OdeSolverService.Algorithm;
import de.mq.odesolver.support.OdeFunctionUtil.Language;

public interface Ode {

	boolean checkOrder(int order);

	boolean checkStartBeforeStop();

	double[] y();

	double start();

	double stop();

	int steps();

	Algorithm algorithm();

	String ode();

	Language language();

	String beautifiedOde();

}