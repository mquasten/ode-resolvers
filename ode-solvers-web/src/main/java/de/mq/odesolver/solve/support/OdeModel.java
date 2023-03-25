package de.mq.odesolver.solve.support;

import de.mq.odesolver.validator.DoubleArrayConstraint;
import de.mq.odesolver.validator.DoubleConstraint;
import de.mq.odesolver.validator.NaturalNumberConstraint;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;

@Valid
public class OdeModel {
	
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
	@Max(1048573)
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


	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}


}
