package de.mq.odesolver.solve.support;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.IntStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.Mockito;
import org.springframework.core.convert.support.DefaultConversionService;

import de.mq.odesolver.solve.OdeSolverService.Algorithm;
import de.mq.odesolver.support.OdeSessionModel;
import de.mq.odesolver.support.OdeSessionModelRepository;
import de.mq.odesolver.support.OdeFunctionUtil.Language;

class OdeConverterTest {
	
	private final OdeSessionModelRepository odeSessionModelRepository = Mockito.mock(OdeSessionModelRepository.class);
	private OdeConverter odeConverter = new OdeConverter(new DefaultConversionService(),odeSessionModelRepository);
	private final OdeSessionModel odeSessionModel = new OdeSessionModel();
	private final OdeModel odeModel = new OdeModel();

	private final double START = 1;
	private final double STOP = 2;

	private final int STEPS = 1000;

	@BeforeEach
	void setup() {
		Mockito.when(odeSessionModelRepository.odeSessionModel()).thenReturn(odeSessionModel);
		odeModel.setOde("y[1]   /  y[0]   + x ");
		odeModel.setSolver(Algorithm.EulerPolygonal.name());
		odeModel.setY("11,12;13 14");
		odeModel.setStart(String.valueOf(START));
		odeModel.setStop(String.valueOf(STOP));
		odeModel.setSteps(String.valueOf(STEPS));
	}

	@Test
	void convert() {
		final Ode ode = odeConverter.convert(odeModel);
		assertEquals("y[1]/y[0]+x", ode.ode());
		assertEquals(Algorithm.EulerPolygonal, ode.algorithm());
		assertEquals(4, ode.y().length);
		IntStream.range(0, 4).forEach(i -> assertEquals(10 + (i + 1), ode.y()[i]));
		assertEquals(START, ode.start());
		assertEquals(STOP, ode.stop());
		assertEquals(STEPS, ode.steps());
		assertEquals(Language.Groovy, ode.language());

	}
	
	@ParameterizedTest()
	@EnumSource
	void convertEmpty(final Language language) {
		odeSessionModel.getSettings().setScriptLanguage(language.name());
		Mockito.when(odeSessionModelRepository.odeSessionModel()).thenReturn(odeSessionModel);
		final var ode = odeConverter.convert(odeModel);
		assertEquals(language, ode.language());
	}

}
