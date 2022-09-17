package de.mq.odesolver.settings.support;

import static de.mq.odesolver.settings.support.SettingController.ATTRIBUTE_LOCALES;
import static de.mq.odesolver.settings.support.SettingController.ATTRIBUTE_SCRIPT_LANGUAGES;
import static de.mq.odesolver.settings.support.SettingController.ATTRIBUTE_SETTINGS;
import static de.mq.odesolver.settings.support.SettingController.REDIRECT_PATTERN;
import static de.mq.odesolver.settings.support.SettingController.SETTINGS_VIEW;
import static java.lang.String.format;
import static java.util.Locale.ENGLISH;
import static java.util.Locale.GERMAN;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;

import de.mq.odesolver.support.BasicMockitoControllerTest;
import de.mq.odesolver.support.OdeFunctionUtil.Language;

class SettingControllerTest extends BasicMockitoControllerTest {

	private final SettingController settingController = new SettingController(odeSessionModelRepository());

	@Test
	void settings() {
		assertNull(odeSessionModel().getSettings().getLanguage());

		assertEquals(SETTINGS_VIEW, settingController.settings(model(), locale()));

		assertEquals(locale().getLanguage(), odeSessionModel().getSettings().getLanguage());

		assertEquals(3, attributes().size());
		assertEquals(odeSessionModel().getSettings(), attributes().get(ATTRIBUTE_SETTINGS));

		assertModelAttributes();
	}

	private void assertModelAttributes() {
		@SuppressWarnings("unchecked")
		final var scriptLanguages = (List<String>) attributes().get(ATTRIBUTE_SCRIPT_LANGUAGES);
		assertEquals(Language.values().length, scriptLanguages.size());

		IntStream.range(0, Language.values().length).forEach(i -> assertEquals(Language.values()[i].name(), scriptLanguages.get(i)));

		@SuppressWarnings("unchecked")
		final var locales = (List<Entry<String, String>>) attributes().get(ATTRIBUTE_LOCALES);
		assertEquals(2, locales.size());
		assertEquals(GERMAN.getLanguage(), locales.get(0).getKey());
		assertEquals(GERMAN.getDisplayLanguage(locale()), locales.get(0).getValue());
		assertEquals(ENGLISH.getLanguage(), locales.get(1).getKey());
		assertEquals(ENGLISH.getDisplayLanguage(locale()), locales.get(1).getValue());
	}

	@Test
	void settingsSubmit() {
		final var settingsModel = new SettingsModel();
		settingsModel.setLanguage(Locale.ENGLISH.getLanguage());
		settingsModel.setScriptLanguage(Language.Nashorn.name());

		assertEquals(format(REDIRECT_PATTERN, SETTINGS_VIEW, settingsModel.getLanguage()), settingController.settingsSubmit(settingsModel, bindingResult(), model(), locale()));

		assertEquals(Language.Nashorn.name(), odeSessionModel().getSettings().getScriptLanguage());
		assertModelAttributes();
	}

}
