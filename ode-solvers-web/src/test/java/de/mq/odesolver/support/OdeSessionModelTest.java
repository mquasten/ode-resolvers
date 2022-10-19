package de.mq.odesolver.support;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import de.mq.odesolver.function.support.FunctionModel;
import de.mq.odesolver.result.support.ResultModel;
import de.mq.odesolver.settings.support.SettingsModel;
import de.mq.odesolver.solve.support.OdeModel;

class OdeSessionModelTest {

	private final OdeSessionModel odeSessionModel = new OdeSessionModel();

	@Test
	void resultModel() {

		assertNotNull(odeSessionModel.getResult());

		final var resultModel = Mockito.mock(ResultModel.class);
		odeSessionModel.setResult(resultModel);

		assertEquals(resultModel, odeSessionModel.getResult());
	}

	@Test
	void odeModel() {
		assertNotNull(odeSessionModel.getOdeModel());

		final var odeModel = Mockito.mock(OdeModel.class);
		odeSessionModel.setOdeModel(odeModel);

		assertEquals(odeModel, odeSessionModel.getOdeModel());
	}

	@Test
	void functionModel() {
		assertNotNull(odeSessionModel.getFunctionModel());

		final var functionModel = Mockito.mock(FunctionModel.class);
		odeSessionModel.setFunctionModel(functionModel);

		assertEquals(functionModel, odeSessionModel.getFunctionModel());
	}

	@Test
	void settingsModel() {
		assertNotNull(odeSessionModel.getSettings());

		final var settingsModel = Mockito.mock(SettingsModel.class);
		odeSessionModel.setSettings(settingsModel);

		assertEquals(settingsModel, odeSessionModel.getSettings());
	}

}
