package de.mq.odesolver.settings.support;

import java.util.Arrays;
import java.util.Collection;
import java.util.Locale;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import de.mq.odesolver.support.OdeFunctionUtil.Language;
import de.mq.odesolver.support.OdeSessionModel;

@Controller
abstract class SettingController {

	static final String SETTINGS_VIEW = "settings";

	private final Collection<Locale> locales = Arrays.asList(Locale.GERMAN, Locale.ENGLISH);

	private final Collection<String> scriptLanguages = Arrays.asList(Language.values()).stream().map(Enum::name).collect(Collectors.toList());

	@GetMapping("/settings")
	String settings(final Model model) {
		model.addAttribute("settings", odeSessionModel().getSettings());
		addModellAttributes(model);
		return SETTINGS_VIEW;
	}

	private void addModellAttributes(final Model model) {
		model.addAttribute("locales", locales);
		model.addAttribute("scriptLanguages", scriptLanguages);
	}

	@PostMapping(value = "/settings")
	String settingsSubmit(@ModelAttribute("settings") final SettingsModel settingsModel, final BindingResult bindingResult, final Model model, final Locale locale) {
		addModellAttributes(model);
		final SettingsModel settingsModelSession = odeSessionModel().getSettings();
		settingsModelSession.setLanguage(settingsModel.getLanguage());
		settingsModelSession.setScriptLanguage(settingsModel.getScriptLanguage());
		return String.format("redirect:%s?locale=%s", SETTINGS_VIEW, settingsModel.getLanguage());
	}

	@Lookup
	abstract OdeSessionModel odeSessionModel();

}
