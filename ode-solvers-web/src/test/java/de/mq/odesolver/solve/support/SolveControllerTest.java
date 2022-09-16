package de.mq.odesolver.solve.support;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.context.MessageSource;
import org.springframework.core.convert.converter.Converter;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import de.mq.odesolver.solve.Ode;
import de.mq.odesolver.solve.OdeResult;
import de.mq.odesolver.solve.OdeSolverService;
import de.mq.odesolver.solve.OdeSolverService.Algorithm;
import de.mq.odesolver.support.OdeSessionModel;
import de.mq.odesolver.support.OdeSessionModelRepository;

class SolveControllerTest {

	private final OdeSolverService odeSolverService = Mockito.mock(OdeSolverService.class);
	private final OdeSessionModelRepository odeSessionModelRepository = Mockito.mock(OdeSessionModelRepository.class);
	private final MessageSource messageSource = Mockito.mock(MessageSource.class);

	@SuppressWarnings("unchecked")
	private final Converter<OdeModel,Ode> converter = Mockito.mock(Converter.class);
	private final SolveController solveController = new SolveController(odeSolverService, odeSessionModelRepository, converter, messageSource);

	private final Model model = Mockito.mock(Model.class);

	private final Map<String, Object> attributes = new HashMap<>();

	private final OdeSessionModel odeSessionModel = new OdeSessionModel();

	@BeforeEach
	void setup() {
		Mockito.when(odeSessionModelRepository.odeSessionModel()).thenReturn(odeSessionModel);
		Mockito.doAnswer(a -> a.getArguments()[0]).when(messageSource).getMessage(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any());
		Mockito.doAnswer(a -> attributes.put(a.getArgument(0, String.class), a.getArgument(1))).when(model).addAttribute(Mockito.anyString(), Mockito.any());

	}

	@Test
	void solve() {
		assertNotNull(odeSessionModel.getOdeModel());
		assertNotNull(odeSessionModel.getSettings());
		assertNotNull(odeSessionModel.getSettings().getScriptLanguage());

		assertEquals(SolveController.SOLVE_VIEW, solveController.solve(model));

		assertEquals(3, attributes.size());
		assertEquals(odeSessionModel.getOdeModel(), attributes.get(SolveController.ATTRIBUTE_ODE));
		assertInitModelAttributes();
	}
	
	@Test
	void solveSubmit() {
		
		final  List<OdeResult> results = Arrays.asList(new OdeResultImpl(new double[] {0}, 0),new OdeResultImpl( new double[] {1}, 1) );
		final var odeModel = Mockito.mock(OdeModel.class);
		Mockito.when(odeModel.getOrder()).thenReturn(1);
		final BindingResult  bindingResult = Mockito.mock(BindingResult.class);
		final Ode ode = Mockito.mock(Ode.class);
		Mockito.when(ode.checkOrder(odeModel.getOrder())).thenReturn(true);
		Mockito.when(ode.beautifiedOde()).thenReturn("y'=y+x");
		Mockito.when(converter.convert(odeModel)).thenReturn(ode);
		Mockito.when(ode.checkStartBeforeStop()).thenReturn(true);
		Mockito.when(odeSolverService.solve(ode)).thenReturn(results);
		
		assertEquals(SolveController.REDIRECT_RESULT_VIEW, solveController.solveSubmit(odeModel,  bindingResult, model, Locale.GERMAN));
		
		
		Mockito.verify(odeSolverService).validateRightSide(ode);
		Mockito.verify(odeSolverService).solve(ode);
		
		
		assertEquals(results,odeSessionModel.getResult().getResults());
		assertEquals(ode.beautifiedOde(), odeSessionModel.getResult().getTitle());
		assertEquals(SolveController.SOLVE_VIEW, odeSessionModel.getResult().getBack());
		assertInitModelAttributes();
	}

	private void assertInitModelAttributes() {
		assertEquals(odeSessionModel.getSettings().getScriptLanguage(), attributes.get(SolveController.ATTRIBUTE_SCRIPT_LANGUAGE));
		@SuppressWarnings("unchecked")
		final List<Entry<String, String>> algorithms = (List<Entry<String, String>>) attributes.get(SolveController.ATTRIBUTE_ALGORITHMS);
		assertEquals(3, algorithms.size());
		assertEquals(Algorithm.RungeKutta4thOrder.name(), algorithms.get(0).getKey());
		assertEquals(Algorithm.RungeKutta4thOrder.name(), algorithms.get(0).getValue());
		assertEquals(Algorithm.RungeKutta2ndOrder.name(), algorithms.get(1).getKey());
		assertEquals(Algorithm.RungeKutta2ndOrder.name(), algorithms.get(1).getValue());
		assertEquals(Algorithm.EulerPolygonal.name(), algorithms.get(2).getKey());
		assertEquals(Algorithm.EulerPolygonal.name(), algorithms.get(2).getValue());
	}
	
	
	@Test
	void solveSubmitFieldErrors() {
		final var odeModel = Mockito.mock(OdeModel.class);
		final BindingResult  bindingResult = Mockito.mock(BindingResult.class);
		Mockito.when(bindingResult.hasFieldErrors()).thenReturn(true);
		
		assertEquals(SolveController.SOLVE_VIEW, solveController.solveSubmit(odeModel,  bindingResult, model, Locale.GERMAN));
		
		assertInitModelAttributes();
	}
	
	@Test
	void solveSubmitWrongOrderAndStartNotBeforeStop() {
		final var odeModel = Mockito.mock(OdeModel.class);
		final BindingResult  bindingResult = Mockito.mock(BindingResult.class);
		final Ode ode = Mockito.mock(Ode.class);
		Mockito.when(converter.convert(odeModel)).thenReturn(ode);
		Mockito.when(bindingResult.hasGlobalErrors()).thenReturn(true);
		assertEquals(SolveController.SOLVE_VIEW, solveController.solveSubmit(odeModel,  bindingResult, model, Locale.GERMAN));
		
		final var errorCaptor = ArgumentCaptor.forClass(ObjectError.class);
		
		Mockito.verify(bindingResult, Mockito.times(2)).addError(errorCaptor.capture());
		final var wrongOrder = errorCaptor.getAllValues().get(0);
		assertEquals(SolveController.ATTRIBUTE_ODE, wrongOrder.getObjectName());
		assertEquals(SolveController.I18N_WRONG_NUMBER_INITIAL_VALUES, wrongOrder.getDefaultMessage());
		
		final var startNotBeforeStop = errorCaptor.getAllValues().get(1);
		assertEquals(SolveController.ATTRIBUTE_ODE, startNotBeforeStop.getObjectName());
		assertEquals(SolveController.I18N_START_LESS_THAN_STOP, startNotBeforeStop.getDefaultMessage());
		
		assertInitModelAttributes();
	}

}
