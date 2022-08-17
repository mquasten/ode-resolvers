package de.mq.odesolver.support;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import org.springframework.util.StringUtils;

import de.mq.odesolver.OdeResult;
import de.mq.odesolver.OdeSolverService.Algorithm;

@Valid
public class Ode {

	private final String REGEX_Y_DERIVATIVE = "y\\[\\s*%s\\s*\\]";;
	
	private Integer order;

	@NotBlank
	private String ode;

	private String solver;

	@DoubleConstraint
	@NotBlank
	private String y;

	@DoubleConstraint
	private String yDerivative;

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

	public String getyDerivative() {
		return yDerivative;
	}

	public void setyDerivative(String yDerivative) {
		this.yDerivative = yDerivative;
	}

	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

	final double[] y() {
		if (order == 1) {
			return OdeResultImpl.doubleArray(Double.parseDouble(StringUtils.trimWhitespace(y)));
		} else {
			return OdeResultImpl.doubleArray(Double.parseDouble(StringUtils.trimWhitespace(y)),
					Double.parseDouble(StringUtils.trimWhitespace(yDerivative)));
		}
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
