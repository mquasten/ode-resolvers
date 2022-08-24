package de.mq.odesolver.solve.support;

import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

import javax.script.Invocable;

import de.mq.odesolver.solve.OdeResult;
import de.mq.odesolver.solve.OdeResultCalculator;
import de.mq.odesolver.support.OdeFunctionUtil;


public abstract class AbstractOdeCalculator implements OdeResultCalculator {

	private final Map<Integer, BiFunction<OdeResult, Double, double[]>> calculators = Map.of(1,
			(odeResult, stepSize) -> calculateFirstOrderOde(odeResult, stepSize), 2,
			(odeResult, stepSize) -> calculateSecondOrderOde(odeResult, stepSize));

	final Function<OdeResult, Double> odeFunction;

	AbstractOdeCalculator(final Function<OdeResult, Double> odeFunction) {

		this.odeFunction = odeFunction;
	}

	AbstractOdeCalculator(final OdeFunctionUtil odeStringUtil, final String function) {
		//private final OdeFunctionUtil odeStringUtil = new OdeFunctionUtil();
		final Invocable invocable = odeStringUtil.prepareFunction(function);
		this.odeFunction = firstOrderOdeFunction -> (Double) odeStringUtil.invokeFunction(invocable,
				firstOrderOdeFunction.yDerivatives(), firstOrderOdeFunction.x());
	}

	@Override
	public final double[] calculate(final OdeResult last, final double stepSize) {
		if (!calculators.containsKey(last.order())) {
			throw new IllegalArgumentException(
					String.format("Ode has wrong order: %s. Only first and second order odes supported", last.order()));
		}
		return calculators.get(last.order()).apply(last, stepSize);
	}
	
	@Override
	public final double errorEstimaion(final double y, final double y2h) {
		return (y - y2h) * quality();

	}
	

	
	abstract double quality(); 
	abstract double[] calculateFirstOrderOde(final OdeResult last, final double stepSize);
	abstract double[] calculateSecondOrderOde(final OdeResult last, final double stepSize);

}
