package de.mq.odesolver.settings.support;

import de.mq.odesolver.support.OdeFunctionUtil;

public class SettingsModel {

	private String language;

	private String scriptLanguage = OdeFunctionUtil.Language.Groovy.name();

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getScriptLanguage() {
		return scriptLanguage;
	}

	public void setScriptLanguage(String scriptLanguage) {
		this.scriptLanguage = scriptLanguage;
	}

}
