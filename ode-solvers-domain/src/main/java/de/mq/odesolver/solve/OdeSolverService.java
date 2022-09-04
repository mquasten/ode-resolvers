package de.mq.odesolver.solve;

import de.mq.odesolver.support.OdeFunctionUtil.Language;

public interface OdeSolverService {

	public enum Algorithm {
		EulerPolygonal(1), RungeKutta2ndOrder(2), RungeKutta4thOrder(4);

		private final int order;

		Algorithm(final int order) {
			this.order = order;
		}

		public final int order() {
			return order;
		}

	}

	OdeSolver odeSolver(final Language language, final Algorithm algorithm, final String function);

	double validateRightSide(final Language language,final String function, final double y0[], final double x0);

}