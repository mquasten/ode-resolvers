package de.mq.odesolver.function.support;

import static org.apache.commons.lang3.Validate.isTrue;
import static org.apache.commons.lang3.Validate.notBlank;
import static org.apache.commons.lang3.Validate.notNull;
import de.mq.odesolver.function.Function;
import de.mq.odesolver.support.OdeFunctionUtil.Language;

class FunctionImpl implements Function {

	private final Language language;
	private final String function;
	private final double start;
	private final double stop;
	private final int steps;
	private final double[] k;

	FunctionImpl(final Language language, final String function, final double start, final double stop, final int steps, final double[] k) {
		notNull(language, "Language is mandatory.");
		notBlank(function, "Function is madatory.");
		isTrue(steps > 0, "Steps must be > 0.");
		this.language = language;
		this.function = function;
		this.k = k;
		this.start = start;
		this.stop = stop;
		this.steps = steps;
	}

	@Override
	public final boolean checkStartBeforeStop() {
		return start < stop;
	}

	@Override
	public final String function() {
		return function;
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
	public final double[] k() {
		return k;
	}

	@Override
	public final Language language() {
		return language;
	}
}
