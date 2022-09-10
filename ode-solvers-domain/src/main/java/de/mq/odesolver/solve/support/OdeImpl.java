package de.mq.odesolver.solve.support;

import static org.apache.commons.lang3.Validate.isTrue;
import static org.apache.commons.lang3.Validate.notBlank;
import static org.apache.commons.lang3.Validate.notNull;

import org.apache.commons.lang3.Validate;

import de.mq.odesolver.solve.Ode;
import de.mq.odesolver.solve.OdeSolverService.Algorithm;
import de.mq.odesolver.support.OdeFunctionUtil.Language;

/**
 * Input Values from solve valid and with correct types
 * 
 * @author mq
 *
 */
class OdeImpl implements Ode {

	private final String REGEX_Y_DERIVATIVE = "y\\[\\s*%s\\s*\\]";

	private final Language language;

	private final String ode;

	private final Algorithm algorithm;

	private final double[] y;

	private final double start;

	private final double stop;

	private final int steps;

	OdeImpl(final Language language, String ode, final Algorithm algorithm, final double[] y, final double start, final double stop, final int steps) {
		notNull(language, "Language is mandatory.");
		notBlank(ode, "Ode is madatory.");
		notNull(algorithm, "Algorithm is madatory.");
		notNull(y, "Y is mandatory.");
		isTrue(y.length > 0, "At least one y element required.");
		isTrue(steps > 0, "Steps must be > 0.");

		this.language = language;
		this.ode = ode;
		this.algorithm = algorithm;
		this.y = y;
		this.start = start;
		this.stop = stop;
		this.steps = steps;
	}

	@Override
	public final boolean checkOrder(final int order) {
		Validate.isTrue(order > 0);
		return y.length == order;
	}

	@Override
	public final boolean checkStartBeforeStop() {
		return start < stop;
	}

	@Override
	public final double[] y() {
		return y;
	}

	@Override
	public final double start() {
		return start;
	}

	@Override
	public final double stop() {
		return stop;
	}

	@Override
	public final int steps() {
		return steps;
	}

	@Override
	public final Algorithm algorithm() {
		return algorithm;
	}

	@Override
	public final String ode() {
		return ode;
	}

	@Override
	public final Language language() {
		return language;
	}

	@Override
	public final String beautifiedOde() {
		final String prefix = y.length == 1 ? "y'=" : "y''=";
		return prefix + ode.replaceAll(String.format(REGEX_Y_DERIVATIVE, 0), "y").replaceAll(String.format(REGEX_Y_DERIVATIVE, 1), "y'");

	}

}
