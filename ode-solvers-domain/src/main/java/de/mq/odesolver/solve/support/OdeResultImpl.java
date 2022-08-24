package de.mq.odesolver.solve.support;

import de.mq.odesolver.function.support.FunctionResultImpl;
import de.mq.odesolver.solve.OdeResult;

class OdeResultImpl extends FunctionResultImpl implements OdeResult {

	private final double errorEstimaion;

	OdeResultImpl(final double y[], final double x) {
		this(y, x, 0);
	}

	OdeResultImpl(final double y[], final double x, final double errorEstimaion) {
		super(y,x);
		this.errorEstimaion = errorEstimaion;
	}

	


	@Override
	public int order() {
		return yDerivatives().length;
	}

	@Override
	public final double errorEstimaion() {
		return errorEstimaion;
	}


}
