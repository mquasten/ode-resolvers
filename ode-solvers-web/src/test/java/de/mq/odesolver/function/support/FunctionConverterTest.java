package de.mq.odesolver.function.support;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.DefaultConversionService;

import org.springframework.util.StringUtils;

class FunctionConverterTest {

	private final Converter<FunctionModel, Function> converter = new FunctionConverter(new DefaultConversionService());

	private FunctionModel functionModel = new FunctionModel();

	private final double start = 1;
	private final double stop = 2;
	private final int steps = 10000;
	private final double[] k = { 1, 2 };

	@BeforeEach
	void init() {
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
	}

	@ParameterizedTest()
	@NullSource()
	@ValueSource(strings = { "", " " })
	void convertEmpty(final String value) {
		functionModel.setK(value);
		final var function = converter.convert(functionModel);
		assertArrayEquals(new double[] {}, function.k());
	}

}
