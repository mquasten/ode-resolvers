package de.mq.odesolver.result.support;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.util.StringUtils;

import de.mq.odesolver.Result;
import de.mq.odesolver.solve.OdeResult;

class ResultsExcelViewTest {

	private static final double X_START = 1;
	private static final double X_STOP = 4;

	private static final double K1 = 6d;
	private static final double K2 = -10d / 3d;
	private static final int MAX_VALUES = 1000;
	private static final String TITLE = "y''=4/x*y'-6/(x**2)*y+x**2";
	private final ResultsExcelView resultsExcelView = new ResultsExcelView();
	private final Function<Double, Double> function = x -> 1 / 2d * Math.pow(x, 4) + K1 * x * x + K2 * Math.pow(x, 3);
	private final Function<Double, Double> derivative = x -> 2 * Math.pow(x, 3) + 2d * K1 * x + 3 * K2 * Math.pow(x, 2);

	private final double h = (X_STOP - X_START) / MAX_VALUES;

	final Workbook workbook = new HSSFWorkbook();
	final HttpServletResponse response = new MockHttpServletResponse();
	
	@Test
	void buildExcelDocument() throws Exception {
		final Map<String, Object> model = new HashMap<>();
		final Collection<Result> results = IntStream.rangeClosed(0, MAX_VALUES).mapToObj(i -> odeResult(X_START + h * i)).collect(Collectors.toList());
		model.put(ResultsExcelView.RESULTS_MODEL, results);
		model.put(ResultsExcelView.RESULTS_TITLE, TITLE);
		assertEquals(MAX_VALUES + 1, results.size());
		
		resultsExcelView.buildExcelDocument(model, workbook, null, response);

		assertEquals(ResultsExcelView.CONTENT_DISPOSITION_HEADER_VALUE, response.getHeader(ResultsExcelView.CONTENT_DISPOSITION_HEADER));
		final var sheet = workbook.getSheetAt(0);
		assertEquals(ResultsExcelView.SHEET_NAME, sheet.getSheetName());
		assertEquals(TITLE, sheet.getRow(0).getCell(0).getStringCellValue());
		assertEquals(ResultsExcelView.COLUMN_HEADER_X, sheet.getRow(1).getCell(0).getStringCellValue());
		assertEquals(ResultsExcelView.COLUMN_HEADER_Y, sheet.getRow(1).getCell(1).getStringCellValue());
		assertEquals(ResultsExcelView.COLUMN_HEADER_DERIVATIVE, sheet.getRow(1).getCell(2).getStringCellValue());
		assertEquals(11, sheet.getRow(0).getLastCellNum());
		assertEquals(3, sheet.getRow(1).getLastCellNum());
		IntStream.range(3, results.size() + 2).forEach(i -> assertRowWithDerivative(sheet, i));
		assertEquals(results.size() + 2, sheet.getPhysicalNumberOfRows());
		assertEquals(X_STOP, sheet.getRow(sheet.getPhysicalNumberOfRows() - 1).getCell(0).getNumericCellValue());

	}
	
	@Test
	void buildExcelDocumentFunctionResult() throws Exception {
		final Map<String, Object> model = new HashMap<>();
		final Collection<Result> results = IntStream.rangeClosed(0, MAX_VALUES).mapToObj(i -> functionResult(X_START + h * i)).collect(Collectors.toList());
		model.put(ResultsExcelView.RESULTS_MODEL, results);
		assertEquals(MAX_VALUES + 1, results.size());
		
		resultsExcelView.buildExcelDocument(model, workbook, null, response);

		assertEquals(ResultsExcelView.CONTENT_DISPOSITION_HEADER_VALUE, response.getHeader(ResultsExcelView.CONTENT_DISPOSITION_HEADER));
		final var sheet = workbook.getSheetAt(0);
		assertEquals(ResultsExcelView.SHEET_NAME, sheet.getSheetName());
		assertEquals(ResultsExcelView.COLUMN_HEADER_X, sheet.getRow(1).getCell(0).getStringCellValue());
		assertEquals(ResultsExcelView.COLUMN_HEADER_Y, sheet.getRow(1).getCell(1).getStringCellValue());
		
		assertEquals(11, sheet.getRow(0).getLastCellNum());
		assertEquals(2, sheet.getRow(1).getLastCellNum());
		IntStream.range(3, results.size() + 2).forEach(i -> assertRowWithoutDerivative(sheet, i));
		assertEquals(results.size() + 2, sheet.getPhysicalNumberOfRows());
		assertEquals(X_STOP, sheet.getRow(sheet.getPhysicalNumberOfRows() - 1).getCell(0).getNumericCellValue());

	}
	
	
	@Test
	void buildExcelDocumentResultsMissing() throws Exception {
		final Map<String, Object> model = new HashMap<>();
		
		resultsExcelView.buildExcelDocument(model, workbook, null, response);
		
		assertEmptyResult(workbook.getSheetAt(0));
	}
	
	@Test
	void buildExcelDocumentResultsEmty() throws Exception {
		final Map<String, Object> model = new HashMap<>();
		model.put(ResultsExcelView.RESULTS_MODEL, Collections.emptyList());
		resultsExcelView.buildExcelDocument(model, workbook, null, response);
		
		assertEmptyResult(workbook.getSheetAt(0));
	}


	private void assertEmptyResult(final Sheet sheet) {
		assertEquals(ResultsExcelView.SHEET_NAME, sheet.getSheetName());
		assertEquals(ResultsExcelView.COLUMN_HEADER_X, sheet.getRow(1).getCell(0).getStringCellValue());
		assertEquals(ResultsExcelView.COLUMN_HEADER_Y, sheet.getRow(1).getCell(1).getStringCellValue());
		assertFalse(StringUtils.hasLength(sheet.getRow(0).getCell(0).getStringCellValue()));
		assertEquals(11, sheet.getRow(0).getLastCellNum());
		assertEquals(2, sheet.getRow(1).getLastCellNum());
	}

	private void assertRowWithDerivative(final Sheet sheet, int i) {
		final var x = X_START + h * (i - 2);
		assertEquals(x, sheet.getRow(i).getCell(0).getNumericCellValue());
		assertEquals(function.apply(x), sheet.getRow(i).getCell(1).getNumericCellValue());
		assertEquals(derivative.apply(x), sheet.getRow(i).getCell(2).getNumericCellValue());
		assertEquals(3, sheet.getRow(i).getLastCellNum());
	}
	
	private void assertRowWithoutDerivative(final Sheet sheet, int i) {
		final var x = X_START + h * (i - 2);
		assertEquals(x, sheet.getRow(i).getCell(0).getNumericCellValue());
		assertEquals(function.apply(x), sheet.getRow(i).getCell(1).getNumericCellValue());
		assertEquals(2, sheet.getRow(i).getLastCellNum());
	}

	private Result odeResult(final double x) {
		final OdeResult result = Mockito.mock(OdeResult.class);
		Mockito.when(result.x()).thenReturn(x);
		final var y = function.apply(x);
		final var dy = derivative.apply(x);
		Mockito.when(result.yDerivative(0)).thenReturn(y);
		Mockito.when(result.yDerivative(1)).thenReturn(dy);
		Mockito.when(result.yDerivatives()).thenReturn(new double[] { y, dy });
		return result;
	}
	
	private Result functionResult(final double x) {
		final OdeResult result = Mockito.mock(OdeResult.class);
		Mockito.when(result.x()).thenReturn(x);
		final var y = function.apply(x);
		Mockito.when(result.yDerivative(0)).thenReturn(y);
		Mockito.when(result.yDerivatives()).thenReturn(new double[] {y});
		return result;
	}

}
