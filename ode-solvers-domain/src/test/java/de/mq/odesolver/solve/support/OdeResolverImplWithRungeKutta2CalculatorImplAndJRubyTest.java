package de.mq.odesolver.solve.support;

import de.mq.odesolver.solve.OdeSolver;
import de.mq.odesolver.solve.support.OdeFunctionUtil.Language;

public class OdeResolverImplWithRungeKutta2CalculatorImplAndJRubyTest
		extends OdeResolverImplWithRungeKutta2CalculatorImplAndLamdasTest {

	@Override
	OdeSolver newOdeResolver(final TestDgl testDgl) {
		return new OdeSolverImpl(
				new RungeKutta2CalculatorImpl(new OdeFunctionUtil(Language.JRuby), testDgl.functionAsString()));
	}

}
