package de.mq.ode;

import java.util.List;

/**
 * Loest eine gewoehnliche DGL nach einem numerischen Verfahren
 * 
 * @author mq
 *
 */
public interface OdeResolver {

	/**
	 * Loest die DGL numerisch. Unterstuetzt werden DGL'n 1. und 2. Ordnung.
	 * 
	 * @param y0    Array mit Anfangswerten fuer y und seien Ableitungen. Bei einer
	 *              DGL 1. Ordnung wird y(0) uegergeben, bei einer DGL 2. Ordnung
	 *              werden y(0) als 1. Wert und y'(0) als 2. Wert uebergeben.
	 * @param start Beginn des x-Interval.
	 * @param stop  Ende des x-Intervalls.
	 * @param steps Anzahl der zu berechnenden Werte im Interval [start;stop]
	 * @return Liste mit Ergebnissen fur x, y und seinen n-1 Ableitungen. @link
	 *         OdeResultImpl
	 */
	List<OdeResult> solve(final double[] y0, final double start, final double stop, final int steps);

}