package de.mq.odesolver.solve;

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

	OdeSolver odeSolver(final Algorithm algorithm, final String function);

	double validateRightSide(final String function, final double y0[], final double x0);

}