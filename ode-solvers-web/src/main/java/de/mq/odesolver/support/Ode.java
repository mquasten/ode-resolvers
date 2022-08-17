package de.mq.odesolver.support;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;

import org.springframework.util.StringUtils;

import de.mq.odesolver.OdeResult;
import de.mq.odesolver.OdeSolverService.Algorithm;
import de.mq.odesolver.support.validator.DoubleArrayConstraint;
import de.mq.odesolver.support.validator.DoubleArrayValidator;
import de.mq.odesolver.support.validator.DoubleConstraint;
import de.mq.odesolver.support.validator.NaturalNumberConstraint;

@Valid
public class Ode {

	private final String REGEX_Y_DERIVATIVE = "y\\[\\s*%s\\s*\\]";;
	
	private Integer order;

	@NotBlank
	private String ode;

	private String solver;

	@DoubleArrayConstraint
	@NotBlank
	private String y;

	

	@DoubleConstraint
	@NotBlank
	private String start;

	@DoubleConstraint
	@NotBlank
	private String stop;

	@NaturalNumberConstraint
	@Max(1048574)
	private String steps;
	
	private List<OdeResult> results = new ArrayList<>();

	public String getSolver() {
		return solver;
	}

	public void setSolver(String solver) {
		this.solver = solver;
	}

	public String getOde() {
		return ode;
	}

	public void setOde(String ode) {
		this.ode = ode;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getStop() {
		return stop;
	}

	public void setStop(String stop) {
		this.stop = stop;
	}

	public String getSteps() {
		return steps;
	}

	public void setSteps(String steps) {
		this.steps = steps;
	}

	public String getY() {
		return y;
	}

	public void setY(String y) {
		this.y = y;
	}


	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

	final double[] y() {
		return Arrays.asList(y.split(DoubleArrayValidator.REGEX_SPLIT_DOUBLE_VECTOR)).stream().mapToDouble(Double::parseDouble).toArray();
	}


	final double start() {
		return Double.valueOf(StringUtils.trimWhitespace(start));
	}

	final double stop() {
		return Double.valueOf(StringUtils.trimWhitespace(stop));
	}
	
	final int steps() {
		return Integer.parseInt(StringUtils.trimWhitespace(steps));
	}

	final Algorithm algorithm() {
		return Algorithm.valueOf(solver);
	}
	
	final void assign(final Collection<OdeResult> results) {
		this.results.clear();
		this.results.addAll(results);
	}
	
	final List<OdeResult> results() {
		return results;
		
	}
	final String odeBeautified() {
		final String prefix = order  == 1 ? "y'=" : "y''=";

		return  prefix
				+ ode.replaceAll(String.format(REGEX_Y_DERIVATIVE, 0), "y").replaceAll(String.format(REGEX_Y_DERIVATIVE, 1), "y'");
		
	}



}
