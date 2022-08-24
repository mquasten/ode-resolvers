package de.mq.odesolver.function.support;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.IntStream;

import javax.script.Invocable;

import de.mq.odesolver.Result;
import de.mq.odesolver.function.FunctionSolver;
import de.mq.odesolver.support.OdeFunctionUtil;



class FunctionSolverImpl implements FunctionSolver {
	
	private final BiFunction<Double,double[], Double> function;
	
	FunctionSolverImpl(final BiFunction<Double,double[], Double> function) {

		this.function = function;
	}

	FunctionSolverImpl(final OdeFunctionUtil odeFunctionUtil, final String function) {
		final Invocable invocable = odeFunctionUtil.prepareFunction(function);
		this.function = (x,k) -> (Double) odeFunctionUtil.invokeFunction(invocable,k , x);
	}

	@Override
	public List<Result> solve(final double[] k, final double start, final double stop, final int steps) {
		final List<Result> results = new ArrayList<>();
		final double stepSize = (stop - start) / steps;
		IntStream.rangeClosed(0, steps).boxed().forEach(n -> {
			final double xi= start+ n*stepSize;
			results.add(new FunctionResultImpl(function.apply(xi, k), xi));
		});
		return results;
	}

}
