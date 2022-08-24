package de.mq.odesolver.function;

import java.util.List;

import de.mq.odesolver.Result;

/**
 * Berechnet Funktionswerte einer Funktion von einer Variablen und einem Parametervektor  
 * 
 * @author mq
 *
 */
public interface FunctionSolver {
	
	/**
	 * Berechnet Funktionswerte einer Funktion von einer Variablen und einem Parametervektor  
	 * 
	 * @param x abhaengige Variable 
	 * @param k[] Parametervektor z.B. [k0, k1] können in der Funktion verwendet.  
	 * @param start Beginn des x-Interval.
	 * @param stop  Ende des x-Intervalls.
	 * @param steps Anzahl der zu berechnenden Werte im Interval [start;stop]
	 * @return Liste mit Ergebnissen fur x, y. @link FunctionResultImpl
	 */
	List<Result> solve(final double[] k, final double start, final double stop, final int steps);

}
