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

	private static final String RESULT_VIEW = "result";
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

	@GetMapping("/result")
	String solve(final Model model) {
		final ResultModel result = odeSessionModelRepository.odeSessionModel().getResult();
		model.addAttribute("result", result);
		return RESULT_VIEW;
	}

	@PostMapping(value = "/result", params = "back")
	String backSubmit() {
		return String.format("redirect:%s", odeSessionModelRepository.odeSessionModel().getResult().getBack());
	}

	@PostMapping(value = "/result", params = "valueTable")
	ModelAndView excelSubmit(@ModelAttribute("result") final ResultModel resultModel, final BindingResult bindingResult, final Model model, final Locale locale) {
		return successSubmit(resultsExcelView, bindingResult, model, locale);

	}

	private ModelAndView successSubmit(final View successView, final BindingResult bindingResult, final Model model, final Locale locale) {
		final ResultModel result = odeSessionModelRepository.odeSessionModel().getResult();
		if (CollectionUtils.isEmpty(result.getResults())) {
			bindingResult.addError(new ObjectError("result", messageSource.getMessage("result.empty", null, "empty", locale)));
			return new ModelAndView(RESULT_VIEW);
		} else {
			model.addAttribute("results", result.getResults());
			model.addAttribute("resultsTitle", result.getTitle());
			return new ModelAndView(successView);
		}
	}

	@PostMapping(value = "/result", params = "graph")
	ModelAndView graphSubmit(@ModelAttribute("result") final ResultModel resultModel, final BindingResult bindingResult, final Model model, final Locale locale) {
		return successSubmit(resultsGraphView, bindingResult, model, locale);
	}

}
