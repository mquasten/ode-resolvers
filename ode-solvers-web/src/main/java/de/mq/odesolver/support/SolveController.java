
package de.mq.odesolver.support;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
class  SolveController  {

	private final OdeSolverService odeSolverService;

	private final ModelAndView solveModelAndView= new ModelAndView("solve");

	
	private final Map<String, ModelAndView> commands;
			
	@Autowired
	SolveController(final OdeSolverService odeSolverService, final ResultsExcelView resultsExcelView, final ResultsGraphView resultsGraphView) {
		this.odeSolverService = odeSolverService;
		this.commands=Map.of("valueTable", new ModelAndView(resultsExcelView), "graph", new ModelAndView(resultsGraphView ));
	}
		

	@GetMapping("/solve")
	public ModelAndView solve(final Model model) {
		
		model.addAttribute("ode", new Ode());
		
		return solveModelAndView;
	}
	
	
	// Die Reihenfolge der Parameter ist wichtig, sonst funktioniert Beanvalidation
	// nicht (Errorpage wird angezeigt) !!!
	@PostMapping(value = "/solve")
	public ModelAndView solveSubmit(@RequestParam(name = "command") final String command,
			@ModelAttribute("ode") @Valid final Ode ode, final BindingResult bindingResult, final Model model) {
	
		
		if (bindingResult.hasFieldErrors()) {
			return solveModelAndView;
		}

		if ( ! validate(ode, bindingResult) ) {
			return solveModelAndView;
		}

		if(! commands.containsKey(command)) {
			return solveModelAndView;
		}

		if(! calculate(ode, model, bindingResult)) {
			return solveModelAndView;
		}
		
		
		return commands.get(command);
		
		
	}


	

	private  boolean  calculate(final Ode ode, final Model model, final BindingResult bindingResult) {
		try {
		final OdeSolver odeSolver = odeSolverService.odeResolver(ode.algorithm(), ode.getOde());
		final List<OdeResult> results = odeSolver.solve(ode.y(), ode.start(), ode.stop(),ode.steps() );
		  
		model.addAttribute("results",results);
		model.addAttribute("resultsTitle", ode.odeBeautified());
		  return true;
		} catch(final Exception exception) {
			bindingResult.addError(new ObjectError("ode", exception.getMessage()));
			 return false;
		}
		
	
	}

	private boolean  validate(final Ode ode, final BindingResult bindingResult) {

		
		if ( ode.y().length != ode.getOrder() ) {
			bindingResult.addError(new ObjectError("ode", "falsche Anzahl Startwerte."));
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
