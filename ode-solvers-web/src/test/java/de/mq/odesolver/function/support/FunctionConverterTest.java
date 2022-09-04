package de.mq.odesolver.function.support;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.DefaultConversionService;

import org.springframework.util.StringUtils;

import de.mq.odesolver.support.OdeFunctionUtil.Language;
import de.mq.odesolver.support.OdeSessionModel;
import de.mq.odesolver.support.OdeSessionModelRepository;

class FunctionConverterTest {

	private final OdeSessionModelRepository odeSessionModelRepository = Mockito.mock(OdeSessionModelRepository.class);
	
	private final Converter<FunctionModel, Function> converter = new FunctionConverter(new DefaultConversionService(), odeSessionModelRepository);

	private FunctionModel functionModel = new FunctionModel();

	private final OdeSessionModel odeSessionModel = new OdeSessionModel();
	private final double start = 1;
	private final double stop = 2;
	private final int steps = 10000;
	private final double[] k = { 1, 2 };

	@BeforeEach
	void init() {
		Mockito.when(odeSessionModelRepository.odeSessionModel()).thenReturn(odeSessionModel);
		functionModel.setFunction("1/2*x**4 + k[0]*X**2 + k[1]*x**3");
		functionModel.setStart("" + start);
		functionModel.setStop("" + stop);
		functionModel.setSteps("" + steps);
		functionModel.setK(StringUtils.collectionToCommaDelimitedString(Arrays.asList(k)));
	}

	@Test
	void convert() {
		final var function = converter.convert(functionModel);
		assertEquals(functionModel.getFunction().replaceAll("[ ]+", ""), function.function());
		assertEquals(start, function.start());
		assertEquals(stop, function.stop());
		assertEquals(steps, function.steps());
		assertArrayEquals(k, function.k());
		assertEquals(Language.Groovy, function.language());
	}

	@ParameterizedTest()
	@NullSource()
	@ValueSource(strings = { "", " " })
	void convertEmpty(final String value) {
		functionModel.setK(value);
		final var function = converter.convert(functionModel);
		assertArrayEquals(new double[] {}, function.k());
	}
	
	@ParameterizedTest()
	@EnumSource
	void convertEmpty(final Language language) {
		odeSessionModel.getSettings().setScriptLanguage(language.name());
		Mockito.when(odeSessionModelRepository.odeSessionModel()).thenReturn(odeSessionModel);
		final var function = converter.convert(functionModel);
		assertEquals(language, function.language());
	}

}
