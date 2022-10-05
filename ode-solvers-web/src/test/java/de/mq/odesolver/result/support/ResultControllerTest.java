package de.mq.odesolver.result.support;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import de.mq.odesolver.Result;
import de.mq.odesolver.support.BasicMockitoControllerTest;

class ResultControllerTest extends BasicMockitoControllerTest {

	private final ResultsExcelView resultsExcelView = Mockito.mock(ResultsExcelView.class);
	private final ResultsGraphView resultsGraphView = Mockito.mock(ResultsGraphView.class);
	private final ResultController resultController = new ResultController(odeSessionModelRepository(), messageSource(),
			resultsExcelView, resultsGraphView);

	@Test
	void result() {
		assertEquals(ResultController.RESULT_VIEW, resultController.result(model()));

		assertEquals(odeSessionModel().getResult(), attributes().get(ResultController.ATTRIBUTE_RESULT));
	}

	@Test
	void backSubmit() {
		final var resultModel = Mockito.mock(ResultModel.class);
		Mockito.when(resultModel.getBack()).thenReturn(ResultModel.BACK_SOLVE);
		odeSessionModel().setResult(resultModel);
		assertEquals(String.format(ResultController.REDIRECT_VIEW_PATTERN, ResultModel.BACK_SOLVE), resultController.backSubmit());
	}

	@Test
	void excelSubmit() {
		final var resultModel = Mockito.mock(ResultModel.class);
		final var results = Arrays.asList(Mockito.mock(Result.class));
		Mockito.when(resultModel.getResults()).thenReturn(results);
		final var title = "y''=y'+x";
		Mockito.when(resultModel.getTitle()).thenReturn(title);
		odeSessionModel().setResult(resultModel);

		assertEquals(resultsExcelView, resultController.excelSubmit(null, bindingResult(), model(), locale()).getView());

		assertEquals(results, attributes().get(ResultController.ATTRIBUTE_RESULTS_LIST));
		assertEquals(title, attributes().get(ResultController.ATTRIBUTE_RESULTS_TITLE));

	}

	@Test
	void excelSubmitEmpty() {
		final var resultModel = Mockito.mock(ResultModel.class);

		odeSessionModel().setResult(resultModel);

		assertEquals(ResultController.RESULT_VIEW, resultController.excelSubmit(null, bindingResult(), model(), locale()).getViewName());

		final var emptyResults = globalErrors().get(0);
		assertEquals(ResultController.ATTRIBUTE_RESULT, emptyResults.getObjectName());
		assertEquals(ResultController.I18N_RESULT_EMPTY, emptyResults.getDefaultMessage());

	}

	@Test
	void graphSubmit() {
		final var resultModel = Mockito.mock(ResultModel.class);
		final var results = Arrays.asList(Mockito.mock(Result.class));
		Mockito.when(resultModel.getResults()).thenReturn(results);
		final var title = "y''=y'+x";
		Mockito.when(resultModel.getTitle()).thenReturn(title);
		odeSessionModel().setResult(resultModel);

		assertEquals(resultsGraphView, resultController.graphSubmit(null, bindingResult(), model(), locale()).getView());

		assertEquals(results, attributes().get(ResultController.ATTRIBUTE_RESULTS_LIST));
		assertEquals(title, attributes().get(ResultController.ATTRIBUTE_RESULTS_TITLE));

	}

}
