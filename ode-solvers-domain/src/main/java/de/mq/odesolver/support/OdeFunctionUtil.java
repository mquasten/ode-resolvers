package de.mq.odesolver.support;

import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * Function, die die rechte Seite einer gewoehnlichen DGL beschreibt als
 * Invocable aus einem String erzeugen und den Wert der n-ten Ableitung
 * berechnen.
 * 
 * @author mq
 *
 */
class OdeFunctionUtil {

	double invokeFunction(final Invocable invocable, final double[] y, double x) {
		try {
			final double result = ((Number) invocable.invokeFunction("f", y, x)).doubleValue();
			resultGuard(x, result);
			return result;
		} catch (final NoSuchMethodException | ScriptException e) {
			throw new IllegalStateException("Unable to invoke function.", e);
		} catch (final ClassCastException castException) {
			throw new IllegalStateException("Function do not return a Number.");
		}

	}

	private void resultGuard(final double x, final double result) {
		if (Double.isNaN(result)) {
			throw new IllegalArgumentException(
					String.format("Function returns NaN for x=%e, may be wrong size y[]", x));
		}
		if (Double.isInfinite(result)) {
			throw new IllegalArgumentException(String.format("Function returns Infinite for x=%e", x));
		}
	}

	/**
	 * Funktion die die rechte Seite einer gewoehnlichen DGL beschreibt aus einem
	 * String als Invocable erzeugen
	 * 
	 * @param function die Funktion als String, die Ableitungen sind y[0]: die 0.
	 *                 Ableitung, d.h. y y[1]: die 1. Ableitung, d.h. y'
	 * @return die compilierte Funktion
	 */
	Invocable prepareFunction(final String function) {
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
