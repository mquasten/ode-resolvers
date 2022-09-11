package de.mq.odesolver.function.support;

import static org.apache.commons.lang3.StringUtils.defaultIfBlank;

import java.util.List;
import java.util.Locale;

import javax.validation.Valid;

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
import de.mq.odesolver.function.Function;
import de.mq.odesolver.function.FunctionService;
import de.mq.odesolver.result.support.ResultModel;
import de.mq.odesolver.result.support.ResultsExcelView;
import de.mq.odesolver.result.support.ResultsGraphView;
import de.mq.odesolver.support.OdeSessionModelRepository;

@Controller
class FunctionController {

	private final String functionModelAndView = "function";
	private final Converter<FunctionModel, Function> converter;
	private final MessageSource messageSource;

	private final FunctionService functionService;
	private final OdeSessionModelRepository odeSessionModelRepository;

	@Autowired
	FunctionController(final FunctionService functionService, final OdeSessionModelRepository odeSessionModelRepository, final ResultsExcelView resultsExcelView,
			final ResultsGraphView resultsGraphView, final Converter<FunctionModel, Function> converter, final MessageSource messageSource) {
		this.functionService = functionService;
		this.odeSessionModelRepository = odeSessionModelRepository;
		this.converter = converter;
		this.messageSource = messageSource;
	}

	@GetMapping("/function")
	String solve(final Model model) {
		model.addAttribute("function", odeSessionModelRepository.odeSessionModel().getFunctionModel());
		initModel(model);
		return functionModelAndView;
	}

	private void initModel(final Model model) {
		model.addAttribute("scriptLanguage", odeSessionModelRepository.odeSessionModel().getSettings().getScriptLanguage());
	}

	@PostMapping(value = "/function", params = "submit")
	String solveSubmit(@ModelAttribute("function") @Valid final FunctionModel functionModel, final BindingResult bindingResult, final Model model, final Locale locale) {
		initModel(model);
		if (bindingResult.hasFieldErrors()) {
			return functionModelAndView;
		}

		final Function function = converter.convert(functionModel);

		if (!validate(function, bindingResult, locale)) {
			return functionModelAndView;
		}

		odeSessionModelRepository.odeSessionModel().setFunctionModel(functionModel);

		if (!calculate(function, model, bindingResult, locale)) {
			return functionModelAndView;
		}

		return "redirect:result";

	}

	@PostMapping(value = "/function", params = "reset")
	String solveSubmit(final Model model) {
		odeSessionModelRepository.odeSessionModel().setFunctionModel(new FunctionModel());
		initModel(model);
		return "redirect:" + functionModelAndView;
	}

	private boolean calculate(final Function function, final Model model, final BindingResult bindingResult, final Locale locale) {

		try {
			final List<Result> results = functionService.solve(function);
			odeSessionModelRepository.odeSessionModel().setResult(new ResultModel(results, "y=" + function.function(), function.k()));
			return true;
		} catch (final Exception exception) {
			exception2Bindingresult(exception, bindingResult, locale);
			return false;
		}

	}

	private boolean validate(final Function function, final BindingResult bindingResult, final Locale locale) {

		if (!function.checkStartBeforeStop()) {
			bindingResult.addError(new ObjectError("function", messageSource.getMessage("start-less-than-stop", null, "start-less-than-stop", locale)));
		}

		try {
			functionService.validate(function);
		} catch (final Exception exception) {
			exception2Bindingresult(exception, bindingResult, locale);
		}

		return !bindingResult.hasGlobalErrors();
	}

	private void exception2Bindingresult(final Exception exception, final BindingResult bindingResult, final Locale locale) {
		bindingResult.addError(new ObjectError("ode", defaultIfBlank(exception.getMessage(), messageSource.getMessage("error-execute-function", null, "error-execute-function", locale))));
	}

}
