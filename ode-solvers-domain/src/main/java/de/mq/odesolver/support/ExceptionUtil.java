package de.mq.odesolver.support;

public interface ExceptionUtil {
	
	public static RuntimeException translateToRuntimeException(final Exception exception) {
		if (exception instanceof RuntimeException) {
			return (RuntimeException) exception;
		}
		return new IllegalStateException(exception);
		
	}

}
