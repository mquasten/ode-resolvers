
package de.mq.odesolver.solve.support;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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
import de.mq.odesolver.result.support.ResultModel;
import de.mq.odesolver.solve.OdeSolver;
import de.mq.odesolver.solve.OdeSolverService;
import de.mq.odesolver.solve.OdeSolverService.Algorithm;
import de.mq.odesolver.support.OdeSessionModel;

@Controller
abstract class SolveController {

	private final OdeSolverService odeSolverService;

	private final Converter<OdeModel, Ode> odeConverter;

	private final String solveModelAndView = "solve";

	private final MessageSource messageSource;

	private final Collection<Entry<String, String>> algorithms = Arrays.asList(Algorithm.values()).stream()
			.sorted((a1, a2) -> a2.order() - a1.order())
			.map(a -> new SimpleImmutableEntry<String, String>(a.name(), a.name())).collect(Collectors.toList());

	@Autowired
	SolveController(final OdeSolverService odeSolverService, final Converter<OdeModel, Ode> odeConverter,
			final MessageSource messageSource) {
		this.odeSolverService = odeSolverService;
		this.odeConverter = odeConverter;
		this.messageSource = messageSource;
	}

	@GetMapping("/solve")
	String solve(final Model model) {
		model.addAttribute("algorithms", algorithms);
		model.addAttribute("ode", odeSessionModel().getOdeModel());

		return solveModelAndView;
	}

	// Die Reihenfolge der Parameter ist wichtig, sonst funktioniert Beanvalidation
	// nicht (Errorpage wird angezeigt) !!!
	@PostMapping(value = "/solve", params = "submit")
	String solveSubmit(@ModelAttribute("ode") @Valid final OdeModel odeModel, final BindingResult bindingResult,
			final Model model, final Locale locale) {

		model.addAttribute("algorithms", algorithms);

		if (bindingResult.hasFieldErrors()) {
			return solveModelAndView;
		}

		final Ode ode = odeConverter.convert(odeModel);

		if (!validate(ode, odeModel.getOrder(), bindingResult, locale)) {
			return solveModelAndView;
		}

		odeSessionModel().setOdeModel(odeModel);

		if (!calculate(ode, model, bindingResult)) {
			return solveModelAndView;
		}

		return "redirect:result";

	}

	@PostMapping(value = "/solve", params = "reset")
	String solveReset(final Model model) {
		model.addAttribute("algorithms", algorithms);
		odeSessionModel().setOdeModel(new OdeModel());
		return "redirect:" + solveModelAndView;
	}

	private boolean calculate(final Ode ode, final Model model, final BindingResult bindingResult) {
		try {
			final OdeSolver odeSolver = odeSolverService.odeSolver(ode.algorithm(), ode.ode());
			final List<? extends Result> results = odeSolver.solve(ode.y(), ode.start(), ode.stop(), ode.steps());

			final OdeSessionModel odeSessionModel = odeSessionModel();
			odeSessionModel.setResult(new ResultModel(results, ode.beautifiedOde()));

			return true;
		} catch (final Exception exception) {
			bindingResult.addError(new ObjectError("ode", exception.getMessage()));
			return false;
		}

	}

	private boolean validate(final Ode ode, final int order, final BindingResult bindingResult, final Locale locale) {

		if (!ode.checkOrder(order)) {
			bindingResult.addError(new ObjectError("ode", messageSource
					.getMessage("function.wrong-number-initial-vales", null, "wrong-number-initial-vales", locale)));
		}

		if (!ode.checkStartBeforeStop()) {
			bindingResult.addError(new ObjectError("ode",
					messageSource.getMessage("start-less-than-stop", null, "start-less-than-stop", locale)));
		}

		try {

			odeSolverService.validateRightSide(ode.ode(), ode.y(), ode.start());
		} catch (final Exception exception) {
			bindingResult.addError(new ObjectError("ode", exception.getMessage()));
		}

		return !bindingResult.hasGlobalErrors();
	}

	@Lookup
	abstract OdeSessionModel odeSessionModel();

}
