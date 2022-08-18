package de.mq.odesolver.solve.support;

import org.springframework.util.Assert;

import de.mq.odesolver.solve.OdeSolverService.Algorithm;

/**
 * Input Values from solve valid and with correct types
 * 
 * @author mq
 *
 */
class Ode {

	private final String REGEX_Y_DERIVATIVE = "y\\[\\s*%s\\s*\\]";;

	private final String ode;

	private final Algorithm algorithm;

	private final double[] y;

	private final double start;

	private final double stop;

	private final int steps;

	Ode(final String ode, final Algorithm algorithm, final double[] y, final double start, final double stop,
			final int steps) {
		Assert.hasText(ode, "Ode is madatory.");
		Assert.notNull(algorithm, "Algorithm is madatory.");
		Assert.notNull(y, "Y is mandatory");
		Assert.isTrue(y.length > 0, "At least one y element required");
		Assert.isTrue(steps > 0, "Steps must be > 0");
		this.ode = ode;
		this.algorithm = algorithm;
		this.y = y;
		this.start = start;
		this.stop = stop;
		this.steps = steps;
	}

	final boolean checkOrder(final int order) {
		Assert.isTrue(order > 0, "Order must be > 0");
		return y.length == order;
	}
	
	final boolean checkStartBeforeStop() {
		return start < stop;
	}

	final double[] y() {
		return y;
		// return
		// Arrays.asList(y.split(DoubleArrayValidator.REGEX_SPLIT_DOUBLE_VECTOR)).stream().mapToDouble(Double::parseDouble).toArray();
	}

	final double start() {
		return start;
		// return Double.valueOf(StringUtils.trimWhitespace(start));
	}

	final double stop() {
		return stop;

		// return Double.valueOf(StringUtils.trimWhitespace(stop));
	}

	final int steps() {
		return steps;
		// return Integer.parseInt(StringUtils.trimWhitespace(steps));
	}

	final Algorithm algorithm() {
		return algorithm;
		// return Algorithm.valueOf(solver);
	}

	final String ode() {
		return ode;
	}

	final String beautifiedOde() {
		final String prefix = y.length == 1 ? "y'=" : "y''=";
		return prefix + ode.replaceAll(String.format(REGEX_Y_DERIVATIVE, 0), "y")
				.replaceAll(String.format(REGEX_Y_DERIVATIVE, 1), "y'");

	}

}
