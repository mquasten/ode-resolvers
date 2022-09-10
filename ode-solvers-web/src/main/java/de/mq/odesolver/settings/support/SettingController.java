package de.mq.odesolver.settings.support;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Arrays;
import java.util.Collection;
import java.util.Locale;
import java.util.stream.Collectors;

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

	static final String SETTINGS_VIEW = "settings";
	private final OdeSessionModelRepository odeSessionModelRepository;
	private final Collection<String> scriptLanguages = Arrays.asList(Language.values()).stream().map(Enum::name).collect(Collectors.toList());

	@Autowired
	SettingController(final OdeSessionModelRepository odeSessionModelRepository) {
		this.odeSessionModelRepository = odeSessionModelRepository;
	}

	@GetMapping("/settings")
	String settings(final Model model, final Locale locale) {
		final SettingsModel settings = odeSessionModelRepository.odeSessionModel().getSettings();
		settings.setLanguage(locale.getLanguage());
		model.addAttribute("settings", settings);
		addModellAttributes(model, locale);
		return SETTINGS_VIEW;
	}

	private void addModellAttributes(final Model model, final Locale locale) {
		model.addAttribute("locales",
				Arrays.asList(Locale.GERMAN, Locale.ENGLISH).stream().map(l -> new SimpleImmutableEntry<>(l.getLanguage(), l.getDisplayLanguage(locale))).collect(Collectors.toList()));
		model.addAttribute("scriptLanguages", scriptLanguages);
	}

	@PostMapping(value = "/settings")
	String settingsSubmit(@ModelAttribute("settings") final SettingsModel settingsModel, final BindingResult bindingResult, final Model model, final Locale locale) {
		addModellAttributes(model, locale);
		final SettingsModel settingsModelSession = odeSessionModelRepository.odeSessionModel().getSettings();
		settingsModelSession.setScriptLanguage(settingsModel.getScriptLanguage());
		return String.format("redirect:%s?locale=%s", SETTINGS_VIEW, settingsModel.getLanguage());
	}

}
