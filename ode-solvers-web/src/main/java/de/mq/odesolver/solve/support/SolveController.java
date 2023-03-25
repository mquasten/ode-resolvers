
package de.mq.odesolver.solve.support;

import static org.apache.commons.lang3.StringUtils.defaultIfBlank;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import de.mq.odesolver.Result;
import de.mq.odesolver.result.support.ResultModel;
import de.mq.odesolver.solve.Ode;
import de.mq.odesolver.solve.OdeSolverService;
import de.mq.odesolver.solve.OdeSolverService.Algorithm;
import de.mq.odesolver.support.OdeSessionModel;
import de.mq.odesolver.support.OdeSessionModelRepository;
import jakarta.validation.Valid;

@Controller
class SolveController {

	static final String I18N_START_LESS_THAN_STOP = "start-less-than-stop";

	static final String I18N_WRONG_NUMBER_INITIAL_VALUES = "solve.wrong-number-initial-values";

	static final String REDIRECT_RESULT_VIEW = "redirect:result";

	static final String ATTRIBUTE_ALGORITHMS = "algorithms";

	static final String ATTRIBUTE_SCRIPT_LANGUAGE = "scriptLanguage";

	static final String ATTRIBUTE_ODE = "ode";

	static final String SOLVE_VIEW = "solve";

	private final OdeSolverService odeSolverService;

	private final OdeSessionModelRepository odeSessionModelRepository;

	private final Converter<OdeModel, Ode> odeConverter;

	private final MessageSource messageSource;

	private final Collection<Entry<String, String>> algorithms = Arrays.asList(Algorithm.values()).stream().sorted((a1, a2) -> a2.order() - a1.order())
			.map(a -> new SimpleImmutableEntry<String, String>(a.name(), a.name())).collect(Collectors.toList());

	@Autowired
	SolveController(final OdeSolverService odeSolverService, final OdeSessionModelRepository odeSessionModelRepository, final Converter<OdeModel, Ode> odeConverter,
			final MessageSource messageSource) {
		this.odeSolverService = odeSolverService;
		this.odeSessionModelRepository = odeSessionModelRepository;
		this.odeConverter = odeConverter;
		this.messageSource = messageSource;
	}

	@GetMapping("/" + SOLVE_VIEW)
	String solve(final Model model) {
		initModel(model);
		model.addAttribute(ATTRIBUTE_ODE, odeSessionModelRepository.odeSessionModel().getOdeModel());

		return SOLVE_VIEW;
	}

	private void initModel(final Model model) {
		model.addAttribute(ATTRIBUTE_ALGORITHMS, algorithms);
		model.addAttribute(ATTRIBUTE_SCRIPT_LANGUAGE, odeSessionModelRepository.odeSessionModel().getSettings().getScriptLanguage());
	}

	// Die Reihenfolge der Parameter ist wichtig, sonst funktioniert Beanvalidation
	// nicht (Errorpage wird angezeigt) !!!
	@PostMapping(value = "/" + SOLVE_VIEW, params = "submit")
	String solveSubmit(@ModelAttribute(ATTRIBUTE_ODE) @Valid final OdeModel odeModel, final BindingResult bindingResult, final Model model, final Locale locale) {

		initModel(model);

		if (bindingResult.hasFieldErrors()) {
			return SOLVE_VIEW;
		}

		final Ode ode = odeConverter.convert(odeModel);

		if (!validate(ode, odeModel.getOrder(), bindingResult, locale)) {
			return SOLVE_VIEW;
		}

		odeSessionModelRepository.odeSessionModel().setOdeModel(odeModel);

		if (!calculate(ode, model, bindingResult, locale)) {
			return SOLVE_VIEW;
		}

		return REDIRECT_RESULT_VIEW;

	}

	@PostMapping(value = "/" + SOLVE_VIEW, params = "reset")
	String solveReset(final Model model) {
		initModel(model);
		odeSessionModelRepository.odeSessionModel().setOdeModel(new OdeModel());
		return "redirect:" + SOLVE_VIEW;
	}

	private boolean calculate(final Ode ode, final Model model, final BindingResult bindingResult, final Locale locale) {
		try {
			final List<? extends Result> results = odeSolverService.solve(ode);
			final OdeSessionModel odeSessionModel = odeSessionModelRepository.odeSessionModel();
			odeSessionModel.setResult(new ResultModel(results, ode.beautifiedOde()));
			return true;
		} catch (final Exception exception) {
			exception2Bindingresult(exception, bindingResult, locale);
			return false;
		}

	}

	private boolean validate(final Ode ode, final int order, final BindingResult bindingResult, final Locale locale) {

		if (!ode.checkOrder(order)) {
			bindingResult.addError(new ObjectError(ATTRIBUTE_ODE, messageSource.getMessage(I18N_WRONG_NUMBER_INITIAL_VALUES, null, I18N_WRONG_NUMBER_INITIAL_VALUES, locale)));
		}

		if (!ode.checkStartBeforeStop()) {
			bindingResult.addError(new ObjectError(ATTRIBUTE_ODE, messageSource.getMessage(I18N_START_LESS_THAN_STOP, null, I18N_START_LESS_THAN_STOP, locale)));
		}

		try {
			odeSolverService.validateRightSide(ode);
		} catch (final Exception exception) {
			exception2Bindingresult(exception, bindingResult, locale);
		}

		return !bindingResult.hasGlobalErrors();
	}

	private void exception2Bindingresult(final Exception exception, final BindingResult bindingResult, final Locale locale) {
		bindingResult.addError(new ObjectError(ATTRIBUTE_ODE, defaultIfBlank(exception.getMessage(), messageSource.getMessage("error-execute-function", null, "error-execute-function", locale))));
	}

}
