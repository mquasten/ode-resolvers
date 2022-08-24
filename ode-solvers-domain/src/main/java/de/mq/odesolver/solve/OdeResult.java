package de.mq.odesolver.solve;

import de.mq.odesolver.Result;

/**
 * Rechte Seite einer gewoehnlichen (skalaren) DGL vom Grad n (x, y und seine
 * Ableitungen bis zur n-1.sten).
 * 
 * @author mq
 *
 */
public interface OdeResult extends Result {

	/**
	 * Eine Fehleranschätzung, für die abhaengige Grroesse.
	 * 
	 * @return Fehlerabschaetzung.
	 */
	double errorEstimaion();

	/**
	 * Der Grad der DGL (ergibt sich bekanntlich aus der Groesse des y-Vektors, y
	 * und n-1 Ableitungen bestimmen die rechte Seite (DGL nach der hoechsten
	 * Ableitung aufgeloest).
	 * 
	 * @return Grad der DGL.
	 */
	int order();

}