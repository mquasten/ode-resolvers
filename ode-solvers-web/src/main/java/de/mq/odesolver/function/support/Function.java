package de.mq.odesolver.function.support;

import org.springframework.util.Assert;

import de.mq.odesolver.support.OdeFunctionUtil.Language;

class Function {
	
	private final Language language;
	private final String function;
	private final double start;
	private final double stop;
	private final int steps;
	private final double[] k;
	
	
	Function(final Language language, final String function, final double start, final double stop, final int steps,  final double[] k) {
		Assert.notNull(language, "Language is mandatory.");
		Assert.hasText(function, "Ode is madatory.");
		Assert.isTrue(steps > 0, "Steps must be > 0");
		this.language=language;
		this.function = function;
		this.k = k;
		this.start = start;
		this.stop = stop;
		this.steps = steps;
	}
	
	final boolean checkStartBeforeStop() {
		return start < stop;
	}
	

	final String function() {
		return function;
	}
	
	final double start() {
		return start;
	}
	
	final double stop() {
		return stop;
	}
	
	final int steps() {
		return steps;
	}
	
	final double[] k() {
		return k;
	}
	
	final Language language() {
		return language;
	}
}
