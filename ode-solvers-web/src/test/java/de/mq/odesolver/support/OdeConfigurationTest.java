package de.mq.odesolver.support;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Locale;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

class OdeConfigurationTest {

	private final OdeConfiguration odeConfiguration = new OdeConfiguration();

	@Test
	void odeSessionModel() {
		assertNotNull(odeConfiguration.odeSessionModel());
	}

	@Test
	void messageSource() {
		final var messageSource = (ResourceBundleMessageSource) odeConfiguration.messageSource();
		assertEquals(1, messageSource.getBasenameSet().size());
		assertEquals(OdeConfiguration.I18N_MESSAGE_PATH, messageSource.getBasenameSet().iterator().next());
		assertEquals(OdeConfiguration.ENCODING, ReflectionTestUtils.getField(messageSource, "defaultEncoding"));
		assertEquals(Locale.GERMAN, ReflectionTestUtils.getField(messageSource, "defaultLocale"));
	}

	@Test
	void addInterceptors() {
		final var interceptorRegistry = Mockito.mock(InterceptorRegistry.class);
		Mockito.doAnswer(this::assertParameterName).when(interceptorRegistry).addInterceptor(Mockito.any());
		odeConfiguration.addInterceptors(interceptorRegistry);
	}

	private Object assertParameterName(final InvocationOnMock invocationOnMock) {
		assertEquals(OdeConfiguration.LOCALE_PARAMETER_NANE,
				invocationOnMock.getArgument(0, LocaleChangeInterceptor.class).getParamName());
		return null;
	}

	@Test
	void localeResolver() {
		final var localeResolver = (odeConfiguration.localeResolver());
		assertEquals(Locale.GERMAN, ReflectionTestUtils.getField(localeResolver, "defaultLocale"));
	}

}
