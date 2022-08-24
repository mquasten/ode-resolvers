package de.mq.odesolver.function.support;

import javax.script.Invocable;

import de.mq.odesolver.function.FunctionService;
import de.mq.odesolver.function.FunctionSolver;
import de.mq.odesolver.support.OdeFunctionUtil;



class FunctionServiceImpl implements FunctionService {
	
	private final OdeFunctionUtil odeFunctionUtil;
	
	
	FunctionServiceImpl(final OdeFunctionUtil odeFunctionUtil) {
		this.odeFunctionUtil = odeFunctionUtil;
	}


	@Override
	public final FunctionSolver functionSolver(final String function) {

		try {
			return  new FunctionSolverImpl(odeFunctionUtil, function);
				
		} catch (Exception exception) {
			throw new IllegalStateException(exception.getCause());
		}

	}
	
	@Override
	public final double validateValue(final String function,final double x0, final double k[]) {
		final Invocable invocable = odeFunctionUtil.prepareFunction(function);
		return odeFunctionUtil.invokeFunction(invocable, k, x0);
	}

}
