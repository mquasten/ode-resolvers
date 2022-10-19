package de.mq.odesolver;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.SpringApplication;


class ServingWebContentApplicationTest {

	@Test
	void main() {
		final String[] args = { "arg1", "arg2" };
		try (final var mocked = Mockito.mockStatic(SpringApplication.class)) {
			ServingWebContentApplication.main(args);
			mocked.verify(() -> SpringApplication.run(ServingWebContentApplication.class, args));
		}
	}
	
	@Test
	void newServingWebContentApplication() {
		BeanUtils.instantiateClass(Mockito.mock(ServingWebContentApplication.class).getClass());
	}

}
