package de.mq.odesolver.function.support;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.core.convert.converter.Converter;
import org.springframework.validation.BindingResult;

import de.mq.odesolver.Result;
import de.mq.odesolver.function.Function;
import de.mq.odesolver.function.FunctionService;
import de.mq.odesolver.support.BasicMockitoControllerTest;

class FunctionControllerTest extends BasicMockitoControllerTest {
	
	private final FunctionService functionService = Mockito.mock(FunctionService.class);
	
    @SuppressWarnings("unchecked")
	private final Converter<FunctionModel,Function> converter = Mockito.mock(Converter.class);
	
	private final FunctionController functionController = new FunctionController(functionService, odeSessionModelRepository(), converter, messageSource());
	
	@Test
	void solve() {
		assertNotNull(odeSessionModel().getOdeModel());
		assertNotNull(odeSessionModel().getSettings());
		assertNotNull(odeSessionModel().getSettings().getScriptLanguage());
		
		assertEquals(FunctionController.FUNCTION_VIEW, functionController.solve(model()));
		
		assertEquals(2, attributes().size());
		assertEquals(odeSessionModel().getFunctionModel(), attributes().get(FunctionController.ATTRIBUTE_FUNCTION));
		assertInitModelAttributes();
	}

	private void assertInitModelAttributes() {
		assertEquals(odeSessionModel().getSettings().getScriptLanguage(), attributes().get(FunctionController.ATTRIBUTE_SCRIPT_LANGUAGE));
	}
	
	@Test
	void solveSubmit() {
		final  List<Result> results = Arrays.asList(new FunctionResultImpl(0, 0), new FunctionResultImpl(0, 0) );
		final var functionModel = Mockito.mock(FunctionModel.class);
		final BindingResult  bindingResult = Mockito.mock(BindingResult.class);
		final Function function = Mockito.mock(Function.class);
		Mockito.when(converter.convert(functionModel)).thenReturn(function);
		Mockito.when(function.checkStartBeforeStop()).thenReturn(true);
		Mockito.when(function.function()).thenReturn("Y**2");
		Mockito.when(functionService.solve(function)).thenReturn(results);
		
		assertEquals(FunctionController.REDIRECT_RESULT_VIEW, functionController.solveSubmit(functionModel,  bindingResult, model(), locale()));
		
		Mockito.verify(functionService).validate(function);
		Mockito.verify(functionService).solve(function);
		
		assertEquals(results,odeSessionModel().getResult().getResults());
		assertEquals("y=" + function.function(), odeSessionModel().getResult().getTitle());
		assertEquals(FunctionController.FUNCTION_VIEW, odeSessionModel().getResult().getBack());
		assertInitModelAttributes();
		
	}
	
	@Test
	void solveSubmitFieldErrors() {
		final var functionModel = Mockito.mock(FunctionModel.class);
		Mockito.when(bindingResult().hasFieldErrors()).thenReturn(true);
		
		assertEquals(FunctionController.FUNCTION_VIEW, functionController.solveSubmit(functionModel,  bindingResult(), model(), locale()));
		
		assertInitModelAttributes();
	}

}
