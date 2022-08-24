package de.mq.odesolver.support;

import de.mq.odesolver.support.OdeFunctionUtil.Language;

public interface  OdeFunctionUtilFactory {
	
	 public static OdeFunctionUtil newOdeFunctionUtil(final Language language) {
		 return new OdeFunctionUtilImpl(language);
	 }
	 
	 public static OdeFunctionUtil newOdeFunctionUtil(final Language language, final String vectorName) {
		 return new OdeFunctionUtilImpl(language, vectorName);
	 }

}
