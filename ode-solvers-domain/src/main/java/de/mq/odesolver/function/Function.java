package de.mq.odesolver.function;

import de.mq.odesolver.support.OdeFunctionUtil.Language;

public interface Function {

	boolean checkStartBeforeStop();

	String function();

	double start();

	int steps();

	double[] k();

	Language language();

	double stop();

}