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

	@Bean
	@Scope(scopeName = "session")
	OdeSessionModel odeSessionModel() {

		return new OdeSessionModel();

	}

	@Bean(name = "messageSource")
	MessageSource messageSource() {

		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		messageSource.setBasename("i18n/messages");
		// messageSource.setFallbackToSystemLocale(false);
		// messageSource.setCacheSeconds(0);
		messageSource.setDefaultEncoding("UTF-8");
		messageSource.setDefaultLocale(Locale.GERMAN);

		return messageSource;
	}

	@Override
	public void addInterceptors(final InterceptorRegistry registry) {
		final LocaleChangeInterceptor interceptor = new LocaleChangeInterceptor();
		interceptor.setParamName("locale");
		registry.addInterceptor(interceptor);
	}

	@Bean
	LocaleResolver localeResolver() {
		final SessionLocaleResolver localeResolver = new SessionLocaleResolver();
		localeResolver.setDefaultLocale(Locale.GERMAN);
		return localeResolver;
	}

}
