package de.mq.odesolver.support;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

@Configuration
class OdeConfiguration implements WebMvcConfigurer {

	static final String LOCALE_PARAMETER_NANE = "locale";
	static final String ENCODING = "UTF-8";
	static final String I18N_MESSAGE_PATH = "i18n/messages";

	@Bean
	@Scope(scopeName = "session")
	OdeSessionModel odeSessionModel() {

		return new OdeSessionModel();

	}

	@Bean(name = "messageSource")
	MessageSource messageSource() {
		final var messageSource = new ResourceBundleMessageSource();
		messageSource.setBasename(I18N_MESSAGE_PATH);
		messageSource.setDefaultEncoding(ENCODING);
		messageSource.setDefaultLocale(Locale.GERMAN);

		return messageSource;
	}

	@Override
	public void addInterceptors(final InterceptorRegistry registry) {
		final LocaleChangeInterceptor interceptor = new LocaleChangeInterceptor();
		interceptor.setParamName(LOCALE_PARAMETER_NANE);
		registry.addInterceptor(interceptor);
	}

	@Bean
	LocaleResolver localeResolver() {
		final var localeResolver = new SessionLocaleResolver();
		localeResolver.setDefaultLocale(Locale.GERMAN);
		return localeResolver;
	}

}
