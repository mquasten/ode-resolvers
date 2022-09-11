package de.mq.odesolver.function.support;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import de.mq.odesolver.function.FunctionService;
import de.mq.odesolver.function.FunctionSolver;
import de.mq.odesolver.support.OdeFunctionUtil.Language;

class FunctionServiceImplTest {

	private static final int STEPS = 1000;
	private static final double[] K = new double[] {1,2};
	private static final double X0 = 1d/2;
	private static final String FUNCTION = "k[0]*x*x+k[1]*x";
	private final FunctionService functionService = new FunctionServiceImpl();

	@ParameterizedTest
	@EnumSource
	void functionSolver(final Language language) {
		assertTrue(functionService.functionSolver(language, FUNCTION) instanceof FunctionSolver);
	}
	
	@ParameterizedTest
	@EnumSource
	void solce(final Language language) {
		assertEquals(1+STEPS, functionService.solve(newFunction(language)).size());
		
	}
	
	@ParameterizedTest
	@EnumSource
	void functionSolverException(final Language language) {
		assertThrows(IllegalStateException.class, () -> functionService.functionSolver(language,  "k[0] x"));
	}
	
	@ParameterizedTest
	@EnumSource
	void validate(final Language language) {
		assertEquals(5d/4, functionService.validate(language, FUNCTION, X0, K));
	}
	
	@ParameterizedTest
	@EnumSource
	void validateFunction(final Language language) {
		assertEquals(5d/4,functionService.validate(newFunction(language)));
	}

	private FunctionImpl newFunction(final Language language) {
		return new FunctionImpl(language, FUNCTION, X0, 1, STEPS, K);
	}
	
	
	@ParameterizedTest
	@EnumSource
	void validateInvalid(final Language language) {
		assertThrows(IllegalArgumentException.class, () -> functionService.validate(language, "k[0]/x",0 ,K));
	}

}
