package de.mq.odesolver.result.support;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import de.mq.odesolver.support.OdeSessionModelRepository;

@Controller
class ResultController {

	static final String I18N_RESULT_EMPTY = "result.empty";
	static final String ATTRIBUTE_RESULTS_TITLE = "resultsTitle";
	static final String ATTRIBUTE_RESULTS_LIST = "results";
	static final String REDIRECT_VIEW_PATTERN = "redirect:%s";
	static final String ATTRIBUTE_RESULT = "result";
	static final String RESULT_VIEW = ATTRIBUTE_RESULT;
	private final OdeSessionModelRepository odeSessionModelRepository;
	private final ResultsExcelView resultsExcelView;
	private final ResultsGraphView resultsGraphView;
	private final MessageSource messageSource;

	@Autowired
	ResultController(final OdeSessionModelRepository odeSessionModelRepository, final MessageSource messageSource, final ResultsExcelView resultsExcelView, final ResultsGraphView resultsGraphView) {
		this.resultsExcelView = resultsExcelView;
		this.odeSessionModelRepository = odeSessionModelRepository;
		this.resultsGraphView = resultsGraphView;
		this.messageSource = messageSource;
	}

	@GetMapping("/" + RESULT_VIEW)
	String result(final Model model) {
		final ResultModel result = odeSessionModelRepository.odeSessionModel().getResult();
		model.addAttribute(ATTRIBUTE_RESULT, result);
		return RESULT_VIEW;
	}

	@PostMapping(value = "/" + RESULT_VIEW, params = "back")
	String backSubmit() {
		return String.format(REDIRECT_VIEW_PATTERN, odeSessionModelRepository.odeSessionModel().getResult().getBack());
	}

	@PostMapping(value = "/"+ RESULT_VIEW, params = "valueTable")
	ModelAndView excelSubmit(@ModelAttribute(ATTRIBUTE_RESULT) final ResultModel resultModel, final BindingResult bindingResult, final Model model, final Locale locale) {
		return successSubmit(resultsExcelView, bindingResult, model, locale);

	}

	private ModelAndView successSubmit(final View successView, final BindingResult bindingResult, final Model model, final Locale locale) {
		final ResultModel result = odeSessionModelRepository.odeSessionModel().getResult();
		if (CollectionUtils.isEmpty(result.getResults())) {
			bindingResult.addError(new ObjectError(ATTRIBUTE_RESULT, messageSource.getMessage(I18N_RESULT_EMPTY, null, "empty", locale)));
			return new ModelAndView(RESULT_VIEW);
		} else {
			model.addAttribute(ATTRIBUTE_RESULTS_LIST, result.getResults());
			model.addAttribute(ATTRIBUTE_RESULTS_TITLE, result.getTitle());
			return new ModelAndView(successView);
		}
	}

	@PostMapping(value = "/" + RESULT_VIEW, params = "graph")
	ModelAndView graphSubmit(@ModelAttribute(ATTRIBUTE_RESULT) final ResultModel resultModel, final BindingResult bindingResult, final Model model, final Locale locale) {
		return successSubmit(resultsGraphView, bindingResult, model, locale);
	}

}
