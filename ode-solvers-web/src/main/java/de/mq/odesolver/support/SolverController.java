package de.mq.odesolver.support;

import java.util.List;


import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import de.mq.odesolver.OdeResult;
import de.mq.odesolver.OdeSolver;
import de.mq.odesolver.OdeSolverService;

@Controller
class SolverController {

	private final OdeSolverService odeSolverService;
	private final ResultsExcelView resultsExcelView;

	
	@Autowired
	SolverController(final OdeSolverService odeSolverService, final ResultsExcelView resultsExcelView) {
		this.odeSolverService = odeSolverService;
		this.resultsExcelView=resultsExcelView;
	}

	@GetMapping("/solve")
	public String solve(@RequestParam(name = "name", required = false, defaultValue = "World") String name,
			Model model) {
		model.addAttribute("name", name);
		model.addAttribute("ode", new Ode());
		return "solve";
	}

	// https://spring.io/guides/gs/handling-form-submission/
	// https://www.codejava.net/frameworks/spring-boot/spring-boot-thymeleaf-form-handling-tutorial
	// Die Reihenfolge der Parameter ist wichtig, sonst funktioniert Beanvalidation
	// nicht (Errorpage wird angezeigt) !!!
	@PostMapping(value = "/solve")
	public ModelAndView solveSubmit(@RequestParam(name = "command") final String command,
			@ModelAttribute("ode") @Valid final Ode ode, final BindingResult bindingResult, final Model model) {
	
		if (bindingResult.hasFieldErrors()) {
			return new ModelAndView("solve");
		}

		if ( ! validate(ode, bindingResult) ) {
			return new ModelAndView("solve");
		}

		if(command.equals("valueTable")) {
		   calculate(ode, model, bindingResult);
		    return new ModelAndView(resultsExcelView);
		}
		
		return new ModelAndView("solve");
	}

	private  void  calculate(final Ode ode, final Model model, final BindingResult bindingResult) {
		try {
		final OdeSolver odeSolver = odeSolverService.odeResolver(ode.algorithm(), ode.getOde());
		final List<OdeResult> results = odeSolver.solve(ode.y(), ode.start(), ode.stop(),ode.steps() );
		  model.addAttribute("results", results );
		} catch(final Exception exception) {
			bindingResult.addError(new ObjectError("ode", exception.getMessage()));
		}
		
		
	}

	private boolean  validate(final Ode ode, final BindingResult bindingResult) {

		if (ode.getOrder() == 2 && !StringUtils.hasText(ode.getyDerivative())) {
			bindingResult.addError(new ObjectError("ode", "DGL 2. Ordung: y'(0) ist Mußfeld."));
		}

		if (ode.getOrder() == 1 && StringUtils.hasText(ode.getyDerivative())) {
			bindingResult.addError(new ObjectError("ode", "DGL 1. Ordung: y'(0) muß leer sein."));
            
		}
		
		if(ode.start() >= ode.stop()) {
			bindingResult.addError(new ObjectError("ode", "Start muß < stop sein."));
		}
		
		try {
		
			odeSolverService.validateRightSide(ode.getOde(),ode.y(), ode.start());
		} catch (final Exception exception) {
			bindingResult.addError(new ObjectError("ode", exception.getMessage()));
		}
		
		return ! bindingResult.hasGlobalErrors();
	}

}
