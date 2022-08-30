package de.mq.odesolver.support;

import de.mq.odesolver.function.support.FunctionModel;
import de.mq.odesolver.result.support.ResultModel;
import de.mq.odesolver.solve.support.OdeModel;

public class HistoryModel {
	
	private  OdeModel odeModel = new OdeModel(); 
	
	private FunctionModel functionModel = new FunctionModel();
	
	private ResultModel result=new ResultModel(null, null, null);


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



	

	

	


}
