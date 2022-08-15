package de.mq.odesolver.support;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import org.springframework.util.StringUtils;

import de.mq.odesolver.OdeSolverService.Algorithm;

public class Ode {

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

	double[] y() {
		if (order == 1) {
			return OdeResultImpl.doubleArray(Double.parseDouble(StringUtils.trimWhitespace(y)));
		} else {
			return OdeResultImpl.doubleArray(Double.parseDouble(StringUtils.trimWhitespace(y)),
					Double.parseDouble(StringUtils.trimWhitespace(yDerivative)));
		}
	}


	double start() {
		return Double.valueOf(StringUtils.trimWhitespace(start));
	}

	double stop() {
		return Double.valueOf(StringUtils.trimWhitespace(stop));
	}
	
	int steps() {
		return Integer.parseInt(StringUtils.trimWhitespace(steps));
	}

	Algorithm algorithm() {
		return Algorithm.valueOf(solver);
	}

}
