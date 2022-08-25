
package de.mq.odesolver.solve.support;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.validation.Valid;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import de.mq.odesolver.Result;
import de.mq.odesolver.solve.OdeSolver;
import de.mq.odesolver.solve.OdeSolverService;
import de.mq.odesolver.solve.OdeSolverService.Algorithm;

@Controller
class  SolveController  {

	private final OdeSolverService odeSolverService;
	
	private final Converter<OdeModel, Ode> odeConverter;

	private final ModelAndView solveModelAndView= new ModelAndView("solve");

	
	private final Map<String, ModelAndView> commands;
	private final Collection<Entry<String,String>> algorithms=     Arrays.asList(Algorithm.values()).stream().sorted( (a1,a2) -> a2.order() -a1.order() ).map(a ->new SimpleImmutableEntry<String,String>(a.name(),a.name())).collect(Collectors.toList());
			
	@Autowired
	SolveController(final OdeSolverService odeSolverService, final ResultsExcelView resultsExcelView, final ResultsGraphView resultsGraphView, final Converter<OdeModel, Ode> odeConverter) {
		this.odeSolverService = odeSolverService;
		this.commands=Map.of("valueTable", new ModelAndView(resultsExcelView), "graph", new ModelAndView(resultsGraphView ));
		this.odeConverter=odeConverter;
	}
		

	@GetMapping("/solve")
	public ModelAndView solve(final Model model) {
		model.addAttribute("algorithms", algorithms);
		model.addAttribute("ode", new OdeModel());
		
		return solveModelAndView;
	}
	
	
	// Die Reihenfolge der Parameter ist wichtig, sonst funktioniert Beanvalidation
	// nicht (Errorpage wird angezeigt) !!!
	@PostMapping(value = "/solve")
	public ModelAndView solveSubmit(@RequestParam(name = "command") final String command,
			@ModelAttribute("ode") @Valid final OdeModel odeModel, final BindingResult bindingResult, final Model model) {
	
		model.addAttribute("algorithms", algorithms);
		if (bindingResult.hasFieldErrors()) {
			return solveModelAndView;
		}
		
		final Ode ode = odeConverter.convert(odeModel);

		if ( ! validate(ode, odeModel.getOrder(),  bindingResult) ) {
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
		final OdeSolver odeSolver = odeSolverService.odeSolver(ode.algorithm(), ode.ode());
		final List<? extends Result> results = odeSolver.solve(ode.y(), ode.start(), ode.stop(),ode.steps() );
		  
		model.addAttribute("results",results);
		model.addAttribute("resultsTitle", ode.beautifiedOde());
		  return true;
		} catch(final Exception exception) {
			bindingResult.addError(new ObjectError("ode", exception.getMessage()));
			 return false;
		}
		
	
	}

	private boolean  validate(final Ode ode, final int order, final BindingResult bindingResult) {

		
		if ( ! ode.checkOrder(order)) {
			bindingResult.addError(new ObjectError("ode", "falsche Anzahl Startwerte."));
		}
		
		if(!ode.checkStartBeforeStop()) {
			bindingResult.addError(new ObjectError("ode", "Start muß < stop sein."));
		}
		
		try {
		
			odeSolverService.validateRightSide(ode.ode() ,ode.y(), ode.start());
		} catch (final Exception exception) {
			bindingResult.addError(new ObjectError("ode", exception.getMessage()));
		}
		
		return ! bindingResult.hasGlobalErrors();
	}

	
}
