package de.mq.odesolver.solve.support;

import de.mq.odesolver.solve.OdeSolver;
import de.mq.odesolver.solve.support.OdeFunctionUtil.Language;

public class OdeResolverImplWithRungeKutta4CalculatorImplAndJRubyTest extends AbstractTestOdeResolver {

	@Override
	OdeSolver newOdeResolver(final TestDgl testDgl) {
		return new OdeSolverImpl(new RungeKutta4CalculatorImpl(new OdeFunctionUtil(Language.JRuby),testDgl.functionAsString()));
	}
}
