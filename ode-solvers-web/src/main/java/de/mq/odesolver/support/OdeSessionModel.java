package de.mq.odesolver.support;

import org.springframework.beans.BeanUtils;

import de.mq.odesolver.function.support.FunctionModel;
import de.mq.odesolver.result.support.ResultModel;
import de.mq.odesolver.settings.support.SettingsModel;
import de.mq.odesolver.solve.support.OdeModel;

public class OdeSessionModel {

	private OdeModel odeModel = new OdeModel();

	private FunctionModel functionModel = new FunctionModel();

	private ResultModel result = BeanUtils.instantiateClass(ResultModel.class);
	
	private SettingsModel settings = new SettingsModel();

	public ResultModel getResult() {
		return result;
	}

	public void setResult(ResultModel result) {
		this.result = result;
	}

	public OdeModel getOdeModel() {
		return odeModel;
	}

	public void setOdeModel(OdeModel odeModel) {
		this.odeModel = odeModel;
	}

	public FunctionModel getFunctionModel() {
		return functionModel;
	}

	public void setFunctionModel(FunctionModel functionModel) {
		this.functionModel = functionModel;
	}
	
	public SettingsModel getSettings() {
		return settings;
	}

	public void setSettings(SettingsModel settings) {
		this.settings = settings;
	}

}
