package de.mq.odesolver.settings.support;

import static de.mq.odesolver.support.OdeFunctionUtil.Language.Groovy;
import static de.mq.odesolver.support.OdeFunctionUtil.Language.Nashorn;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import java.util.Locale;
import org.junit.jupiter.api.Test;

class SettingsModelTest {

	private final SettingsModel settingsModel = new SettingsModel();

	@Test
	void language() {
		assertNull(settingsModel.getLanguage());
		settingsModel.setLanguage(Locale.GERMAN.getLanguage());
		assertEquals(Locale.GERMAN.getLanguage(), settingsModel.getLanguage());
	}

	@Test
	void scriptLanguage() {
		assertEquals(Groovy.name(), settingsModel.getScriptLanguage());
		settingsModel.setScriptLanguage(Nashorn.name());
		assertEquals(Nashorn.name(), settingsModel.getScriptLanguage());
	}

}
