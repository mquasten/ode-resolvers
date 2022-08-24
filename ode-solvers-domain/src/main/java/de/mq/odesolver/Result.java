package de.mq.odesolver;

public interface Result {
	
	/**
	 * Unabhaengige Groesse, Abszisse. Die unabhaengige Groesse ist ein Skalar.
	 * 
	 * @return unabhaengige Groesse
	 */
	double x();

	/**
	 * Abhaengige Groesse, ist ein Vektor. 
	 * z.B y  und seine Ableitungen bis zur n-1.sten als Vektor [y,y1].
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


}
