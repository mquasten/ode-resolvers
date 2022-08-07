package de.mq.odesolver.impl;

import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class OdeStringUtil {

	public double invokeFunction(final Invocable invocable, final double[] y, double x) {
		try {
			return ((Number) invocable.invokeFunction("f", y, x)).doubleValue();
		} catch (final NoSuchMethodException | ScriptException e) {
			throw new IllegalStateException("Unable to invoke function.", e);
		}

	}

	public Invocable prepareFunction(final String function) {
		final ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
		final Compilable compilable = (Compilable) engine;
		final Invocable invocable = (Invocable) engine;

		final String statement = String.format("function f(y, x) {return %s}", function);
		try {
			final CompiledScript compiled = compilable.compile(statement);
			compiled.eval();
			return invocable;
		} catch (final ScriptException e) {
			throw new IllegalStateException(String.format("Unable to compile function.", function), e);
		}

	}

}
