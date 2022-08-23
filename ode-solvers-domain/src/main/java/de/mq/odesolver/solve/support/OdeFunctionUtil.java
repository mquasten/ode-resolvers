package de.mq.odesolver.solve.support;

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
	
	enum Language {
		Nashorn("function %s(y, x) {return %s}"),
		Groovy("def %s(y, x) {return %s}");
		
		// "def factorial(n) { n == 1 ? 1 : n * factorial(n - 1) }";
		Language(final String functionPattern){
			this.functionPattern=functionPattern;
		}
		private final String functionPattern;
		
		final String functionPattern() {
			return functionPattern;
		}
		
		final String scriptEngine() {
			return name().toLowerCase();
		}
		
	}
	
	private final Language language;
	OdeFunctionUtil(final Language language) {
		this.language=language;
	}
	
	private final String FUNCTION_NAME="f";

	double invokeFunction(final Invocable invocable, final double[] y, double x) {
		try {
			final Number result = ((Number) invocable.invokeFunction(FUNCTION_NAME, y, x));
			notNullGuard(x, result);
			resultGuard(x, result.doubleValue());
			return result.doubleValue();
		} catch (final NoSuchMethodException | ScriptException e) {
			throw new IllegalStateException( e.getCause());
		} catch (final ClassCastException castException) {
			throw new IllegalStateException("Function do not return a Number.");
		}

	}

	private void notNullGuard(double x, final Number result) {
		if( result == null) {
			throw new IllegalStateException(String.format("Function returns null for x=%e, may be wrong size y[]", x));
		}
	}

	private void resultGuard(final Double x, final double result) {
		if (Double.isNaN(result)) {
			throw new IllegalStateException(
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
		
		final ScriptEngine engine = new ScriptEngineManager().getEngineByName(language.scriptEngine());
		final Compilable compilable = (Compilable) engine;
		final Invocable invocable = (Invocable) engine;
		final String statement = String.format(language.functionPattern(), FUNCTION_NAME, function);
		try {
			final CompiledScript compiled = compilable.compile(statement);
			compiled.eval();
			return invocable;
		} catch (final ScriptException e) {
			throw new IllegalStateException(String.format("Unable to compile function.", function), e.getCause());
		}

	}

}
