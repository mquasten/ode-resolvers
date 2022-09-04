package de.mq.odesolver.solve.support;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import de.mq.odesolver.solve.OdeSolverService.Algorithm;
import de.mq.odesolver.support.OdeSessionModelRepository;
import de.mq.odesolver.support.OdeFunctionUtil.Language;
import de.mq.odesolver.validator.DoubleArrayValidator;

@Component
class OdeConverter implements Converter<OdeModel, Ode>{
	
	private final ConversionService conversionService;
	private final OdeSessionModelRepository odeSessionModelRepository;

	@Autowired
	OdeConverter(final ConversionService conversionService, final OdeSessionModelRepository odeSessionModelRepository) {
		this.conversionService=conversionService;
		this.odeSessionModelRepository=odeSessionModelRepository;
	}
	

	@Override
	public Ode convert(final OdeModel odeModel) {
		final String ode = StringUtils.trimAllWhitespace(odeModel.getOde());
		final Algorithm algorithm= conversionService.convert(odeModel.getSolver(), Algorithm.class);
		final double[] y = conversionService.convert(odeModel.getY().replaceAll(DoubleArrayValidator.REGEX_SPLIT_DOUBLE_VECTOR, ","), double[].class);
		final double start = conversionService.convert(odeModel.getStart(), double.class);
		final double stop = conversionService.convert(odeModel.getStop(), double.class);
		final int steps = conversionService.convert(odeModel.getSteps(), int.class);
		final var scriptLanguage = conversionService.convert(odeSessionModelRepository.odeSessionModel().getSettings().getScriptLanguage(), Language.class);
		return new Ode(scriptLanguage, ode, algorithm, y , start, stop, steps );

	}		

}
