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
import de.mq.odesolver.support.OdeSessionModelRepository;

@Controller
class FunctionController {
	
	static final String I18N_START_LESS_THAN_STOP = "start-less-than-stop";
	static final String REDIRECT_RESULT_VIEW = "redirect:result";
	static final String FUNCTION_VIEW = "function";
	static final String ATTRIBUTE_FUNCTION = "function";
	static final String ATTRIBUTE_SCRIPT_LANGUAGE = "scriptLanguage";

	//private final String functionModelAndView = "function";
	private final Converter<FunctionModel, Function> converter;
	private final MessageSource messageSource;

	private final FunctionService functionService;
	private final OdeSessionModelRepository odeSessionModelRepository;

	@Autowired
	FunctionController(final FunctionService functionService, final OdeSessionModelRepository odeSessionModelRepository, final Converter<FunctionModel, Function> converter, final MessageSource messageSource) {
		this.functionService = functionService;
		this.odeSessionModelRepository = odeSessionModelRepository;
		this.converter = converter;
		this.messageSource = messageSource;
	}

	@GetMapping("/"+FUNCTION_VIEW)
	String function(final Model model) {
		model.addAttribute(ATTRIBUTE_FUNCTION, odeSessionModelRepository.odeSessionModel().getFunctionModel());
		initModel(model);
		return FUNCTION_VIEW;
	}

	private void initModel(final Model model) {
		model.addAttribute(ATTRIBUTE_SCRIPT_LANGUAGE, odeSessionModelRepository.odeSessionModel().getSettings().getScriptLanguage());
	}

	@PostMapping(value = "/" +FUNCTION_VIEW, params = "submit")
	String functionSubmit(@ModelAttribute(ATTRIBUTE_FUNCTION) @Valid final FunctionModel functionModel, final BindingResult bindingResult, final Model model, final Locale locale) {
		initModel(model);
		if (bindingResult.hasFieldErrors()) {
			return FUNCTION_VIEW;
		}

		final Function function = converter.convert(functionModel);

		if (!validate(function, bindingResult, locale)) {
			return FUNCTION_VIEW;
		}

		odeSessionModelRepository.odeSessionModel().setFunctionModel(functionModel);

		if (!calculate(function, model, bindingResult, locale)) {
			return FUNCTION_VIEW;
		}

		return REDIRECT_RESULT_VIEW;

	}

	@PostMapping(value = "/"+FUNCTION_VIEW, params = "reset")
	String functionReset(final Model model) {
		odeSessionModelRepository.odeSessionModel().setFunctionModel(new FunctionModel());
		initModel(model);
		return "redirect:" + FUNCTION_VIEW;
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
			bindingResult.addError(new ObjectError("function", messageSource.getMessage(I18N_START_LESS_THAN_STOP, null, I18N_START_LESS_THAN_STOP, locale)));
		}

		try {
			functionService.validate(function);
		} catch (final Exception exception) {
			exception2Bindingresult(exception, bindingResult, locale);
		}

		return !bindingResult.hasGlobalErrors();
	}

	private void exception2Bindingresult(final Exception exception, final BindingResult bindingResult, final Locale locale) {
		bindingResult.addError(new ObjectError("function", defaultIfBlank(exception.getMessage(), messageSource.getMessage("error-execute-function", null, "error-execute-function", locale))));
	}

}
