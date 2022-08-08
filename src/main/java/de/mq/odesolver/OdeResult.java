package de.mq.odesolver;

/**
 * Rechte Seite einer gewoehnlichen (skalaren) DGL vom Grad n (x, y und seine
 * Ableitungen bis zur n-1.sten).
 * 
 * @author mq
 *
 */
public interface OdeResult {

	/**
	 * Unabhaengige Groesse, Abszisse.
	 * 
	 * @return unabhaengige Groesse
	 */
	double x();

	/**
	 * Abhaengige Groesse und deren Ableitungen bis zur n-1.sten als Vektor [y,y1].
	 * 
	 * @param n-te Ableitung (0 entspricht der abhaengigen Groesse).
	 * @return die n-te Ableitung
	 */
	double yDerivative(final int n);

	/**
	 * Der komplette Vektor der der abhaengigen Groesse und der n-1 Ableitungen
	 * 
	 * @return y und seine n-1 Ableitungen
	 */
	double[] yDerivatives();

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