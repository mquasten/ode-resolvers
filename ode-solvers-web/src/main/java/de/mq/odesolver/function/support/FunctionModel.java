package de.mq.odesolver.function.support;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;

import de.mq.odesolver.validator.DoubleArrayConstraint;
import de.mq.odesolver.validator.DoubleConstraint;
import de.mq.odesolver.validator.NaturalNumberConstraint;

public class FunctionModel {

	@NotBlank
	private String function;

	@DoubleArrayConstraint
	private String k;

	@DoubleConstraint
	@NotBlank
	private String start;
	@DoubleConstraint
	@NotBlank
	private String stop;
	
	@NaturalNumberConstraint
	@Max(1048573)
	private String steps;

	public String getFunction() {
		return function;
	}

	public void setFunction(String function) {
		this.function = function;
	}

	public String getK() {
		return k;
	}

	public void setK(String k) {
		this.k = k;
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

}
