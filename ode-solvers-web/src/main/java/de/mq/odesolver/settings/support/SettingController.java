package de.mq.odesolver.settings.support;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Collection;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import de.mq.odesolver.support.OdeFunctionUtil.Language;
import de.mq.odesolver.support.OdeSessionModelRepository;

@Controller
class SettingController {

	static final String REDIRECT_PATTERN = "redirect:%s?locale=%s";
	static final String ATTRIBUTE_LOCALES = "locales";
	static final String ATTRIBUTE_SCRIPT_LANGUAGES = "scriptLanguages";
	static final String ATTRIBUTE_SETTINGS = "settings";
	static final String SETTINGS_VIEW = ATTRIBUTE_SETTINGS;
	private final OdeSessionModelRepository odeSessionModelRepository;
	private final Collection<String> scriptLanguages = asList(Language.values()).stream().map(Enum::name).collect(toList());

	@Autowired
	SettingController(final OdeSessionModelRepository odeSessionModelRepository) {
		this.odeSessionModelRepository = odeSessionModelRepository;
	}

	@GetMapping("/"+SETTINGS_VIEW)
	String settings(final Model model, final Locale locale) {
		final SettingsModel settings = odeSessionModelRepository.odeSessionModel().getSettings();
		settings.setLanguage(locale.getLanguage());
		model.addAttribute(ATTRIBUTE_SETTINGS, settings);
		addModellAttributes(model, locale);
		return SETTINGS_VIEW;
	}

	private void addModellAttributes(final Model model, final Locale locale) {
		model.addAttribute(ATTRIBUTE_LOCALES,asList(Locale.GERMAN, Locale.ENGLISH).stream().map(l -> new SimpleImmutableEntry<>(l.getLanguage(), l.getDisplayLanguage(locale))).collect(toList()));
		model.addAttribute(ATTRIBUTE_SCRIPT_LANGUAGES, scriptLanguages);
	}

	@PostMapping(value = "/"+SETTINGS_VIEW)
	String settingsSubmit(@ModelAttribute(ATTRIBUTE_SETTINGS) final SettingsModel settingsModel, final BindingResult bindingResult, final Model model, final Locale locale) {
		addModellAttributes(model, locale);
		final SettingsModel settingsModelSession = odeSessionModelRepository.odeSessionModel().getSettings();
		settingsModelSession.setScriptLanguage(settingsModel.getScriptLanguage());
		return String.format(REDIRECT_PATTERN, SETTINGS_VIEW, settingsModel.getLanguage());
	}

}
