package de.mq.odesolver.support;

import java.util.List;
import java.util.Optional;

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

import de.mq.odesolver.OdeResult;
import de.mq.odesolver.OdeSolver;
import de.mq.odesolver.OdeSolverService;

@Controller
class SolverController {

	private final OdeSolverService odeSolverService;

	@Autowired
	SolverController(final OdeSolverService odeSolverService) {
		this.odeSolverService = odeSolverService;
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
	public String greetingSubmit(@RequestParam(name = "validate", required = false) String validate,
			@ModelAttribute("ode") @Valid Ode ode, BindingResult bindingResult, Model model) {

		if (bindingResult.hasErrors()) {
			System.out.println("*** Validation Errors ***");
			return "solve";
		}

		validate(ode).ifPresent(message -> bindingResult.addError(message));

		if (validate != null) {
			return "solve";
		}
		
		
		final OdeSolver odeSolver = odeSolverService.odeResolver(ode.algorithm(), ode.getOde());
		final List<OdeResult> results = odeSolver.solve(ode.y(), ode.start(), ode.stop(),ode.steps() );
		System.out.println("Results: " + results.size());

		return "solve";
	}

	private Optional<ObjectError> validate(final Ode ode) {

		if (ode.getOrder() == 2 && !StringUtils.hasText(ode.getyDerivative())) {
			return Optional.of(new ObjectError("ode", "DGL 2. Ordung: y'(0) ist Mußfeld."));
		}

		if (ode.getOrder() == 1 && StringUtils.hasText(ode.getyDerivative())) {
			return Optional.of(new ObjectError("ode", "DGL 1. Ordung: y'(0) muß leer sein."));

		}
		
		if(ode.start() >= ode.stop()) {
			return Optional.of(new ObjectError("ode", "Start muß < stop sein."));
		}

		try {
		
			odeSolverService.validateRightSide(ode.getOde(),ode.y(), ode.start());
			return Optional.empty();
		} catch (final Exception exception) {
			System.out.println("Error:" + exception.getMessage());
			return Optional.of(new ObjectError("ode", exception.getMessage()));
		}
	}

}
