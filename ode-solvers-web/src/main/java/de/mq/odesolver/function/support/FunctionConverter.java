package de.mq.odesolver.function.support;

import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import de.mq.odesolver.validator.DoubleArrayValidator;

@Component
class FunctionConverter implements Converter<FunctionModel, Function> {

	private static final double[] EMPTY_ARRAY = new double[] {};
	private final ConversionService conversionService;

	public FunctionConverter(final ConversionService conversionService) {
		this.conversionService = conversionService;
	}

	@Override
	public Function convert(final FunctionModel functionModel) {
		final var function = StringUtils.trimAllWhitespace(functionModel.getFunction());
		final var start = conversionService.convert(functionModel.getStart(), double.class);
		final var stop = conversionService.convert(functionModel.getStop(), double.class);
		final var steps = conversionService.convert(functionModel.getSteps(), int.class);
		final var k = StringUtils.hasText(functionModel.getK()) ? conversionService.convert(
				functionModel.getK().replaceAll(DoubleArrayValidator.REGEX_SPLIT_DOUBLE_VECTOR, ","), double[].class)
				: EMPTY_ARRAY;

		return new Function(function, start, stop, steps, k);
	}

}
