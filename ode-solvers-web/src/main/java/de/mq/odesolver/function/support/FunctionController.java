package de.mq.odesolver.function.support;

import java.util.List;
import java.util.Locale;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Lookup;
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
import de.mq.odesolver.function.FunctionService;
import de.mq.odesolver.function.FunctionSolver;
import de.mq.odesolver.result.support.ResultModel;
import de.mq.odesolver.result.support.ResultsExcelView;
import de.mq.odesolver.result.support.ResultsGraphView;
import de.mq.odesolver.support.OdeSessionModel;

@Controller
abstract class FunctionController {

	private final String functionModelAndView = "function";
	private final Converter<FunctionModel, Function> converter;
	private final MessageSource messageSource;

	private final FunctionService functionService;

	FunctionController(final FunctionService functionService, final ResultsExcelView resultsExcelView,
			final ResultsGraphView resultsGraphView, final Converter<FunctionModel, Function> converter,
			final MessageSource messageSource) {
		this.functionService = functionService;
		this.converter = converter;
		this.messageSource = messageSource;
	}

	@GetMapping("/function")
	String solve(final Model model) {
		model.addAttribute("function", odeSessionModel().getFunctionModel());
		return functionModelAndView;
	}

	@PostMapping(value = "/function", params = "submit")
	String solveSubmit(@ModelAttribute("function") @Valid final FunctionModel functionModel,
			final BindingResult bindingResult, final Model model, final Locale locale) {
		if (bindingResult.hasFieldErrors()) {
			return functionModelAndView;
		}

		final Function function = converter.convert(functionModel);

		if (!validate(function, bindingResult, locale)) {
			return functionModelAndView;
		}

		odeSessionModel().setFunctionModel(functionModel);

		if (!calculate(function, model, bindingResult)) {
			return functionModelAndView;
		}

		return "redirect:result";

	}

	@PostMapping(value = "/function", params = "reset")
	String solveSubmit(final Model model) {
		odeSessionModel().setFunctionModel(new FunctionModel());
		return "redirect:" + functionModelAndView;
	}

	private boolean calculate(final Function function, final Model model, final BindingResult bindingResult) {
		try {
			final FunctionSolver functionSolver = functionService.functionSolver(function.function());

			final List<Result> results = functionSolver.solve(function.k(), function.start(), function.stop(),
					function.steps());

			odeSessionModel().setResult(new ResultModel(results, "y=" + function.function(), function.k()));
			return true;
		} catch (final Exception exception) {
			bindingResult.addError(new ObjectError("function", exception.getMessage()));
			return false;
		}

	}

	private boolean validate(final Function function, final BindingResult bindingResult, final Locale locale) {

		if (!function.checkStartBeforeStop()) {
			bindingResult.addError(new ObjectError("function",
					messageSource.getMessage("start-less-than-stop", null, "start-less-than-stop", locale)));
		}

		try {
			functionService.validateValue(function.function(), function.start(), function.k());
		} catch (final Exception exception) {
			bindingResult.addError(new ObjectError("function", exception.getMessage()));
		}

		return !bindingResult.hasGlobalErrors();
	}

	@Lookup
	abstract OdeSessionModel odeSessionModel();

}
