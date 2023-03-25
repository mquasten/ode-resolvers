package de.mq.odesolver.result.support;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.OutputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.mock.web.MockHttpServletResponse;

import de.mq.odesolver.Result;
import de.mq.odesolver.solve.OdeResult;
import jakarta.servlet.http.HttpServletResponse;

class ResultsGraphViewTest {

	private final ResultsGraphView resultsGraphView = new ResultsGraphView();
	private static final double X_START = 1;
	private static final double X_STOP = 4;
	private static final int MAX_VALUES = 1000;
	private static final String TITLE = "y''=4/x*y'-6/(x**2)*y+x**2";
	final Map<String, Object> model = new HashMap<>();
	final HttpServletResponse response = new MockHttpServletResponse();
	private static final double K1 = 6d;
	private static final double K2 = -10d / 3d;
	private final Function<Double, Double> function = x -> 1 / 2d * Math.pow(x, 4) + K1 * x * x + K2 * Math.pow(x, 3);
	private final Function<Double, Double> derivative = x -> 2 * Math.pow(x, 3) + 2d * K1 * x + 3 * K2 * Math.pow(x, 2);
	private final double h = (X_STOP - X_START) / MAX_VALUES;

	private final ArgumentCaptor<OutputStream> outputStreamCaptor = ArgumentCaptor.forClass(OutputStream.class);
	private final ArgumentCaptor<JFreeChart> chartCaptor = ArgumentCaptor.forClass(JFreeChart.class);
	private final ArgumentCaptor<Integer> widthCaptor = ArgumentCaptor.forClass(Integer.class);
	private final ArgumentCaptor<Integer> heightCaptor = ArgumentCaptor.forClass(Integer.class);

	@Test
	void writeChartWithDerivative() throws Exception {
		final var results = IntStream.rangeClosed(0, MAX_VALUES).mapToObj(i -> odeResult(X_START + h * i))
				.collect(Collectors.toList());
		model.put(ResultsGraphView.RESULTS_MODEL, results);
		model.put(ResultsGraphView.RESULTS_TITLE, TITLE);
		assertEquals(MAX_VALUES + 1, results.size());

		try (final var mocked = Mockito.mockStatic(ChartUtilities.class)) {
			resultsGraphView.renderMergedOutputModel(model, null, response);
			mocked.verify(() -> ChartUtilities.writeChartAsPNG(outputStreamCaptor.capture(), chartCaptor.capture(),
					widthCaptor.capture(), heightCaptor.capture()));

		}
		final var chart = chartCaptor.getValue();
		assertEquals(TITLE, chart.getTitle().getText());
		assertEquals(ResultsGraphView.CONTENT_DISPOSITION_HEADER_VALUE,
				response.getHeader(ResultsExcelView.CONTENT_DISPOSITION_HEADER));

		final var dataset = chart.getXYPlot().getDataset();

		assertEquals(2, chart.getXYPlot().getSeriesCount());
		IntStream.range(0, MAX_VALUES + 1).forEach(i -> {
			final var x = X_START + h * i;
			assertEquals(x, dataset.getX(0, i));
			assertEquals(x, dataset.getX(1, i));
			assertEquals(function.apply(x), dataset.getY(0, i));
			assertEquals(derivative.apply(x), dataset.getY(1, i));

		});
		assertEquals(response.getOutputStream(), outputStreamCaptor.getValue());
		assertEquals(ResultsGraphView.WIDTH, widthCaptor.getValue());
		assertEquals(ResultsGraphView.HEIGHT, heightCaptor.getValue());

	}

	@Test
	void writeChartWithoutDerivative() throws Exception {
		final var results = IntStream.rangeClosed(0, MAX_VALUES).mapToObj(i -> functionResult(X_START + h * i))
				.collect(Collectors.toList());
		model.put(ResultsGraphView.RESULTS_MODEL, results);
		assertEquals(MAX_VALUES + 1, results.size());

		try (final var mocked = Mockito.mockStatic(ChartUtilities.class)) {
			resultsGraphView.renderMergedOutputModel(model, null, response);
			mocked.verify(() -> ChartUtilities.writeChartAsPNG(outputStreamCaptor.capture(), chartCaptor.capture(),
					widthCaptor.capture(), heightCaptor.capture()));

		}
		final var chart = chartCaptor.getValue();

		assertEquals(ResultsGraphView.CONTENT_DISPOSITION_HEADER_VALUE,
				response.getHeader(ResultsExcelView.CONTENT_DISPOSITION_HEADER));

		final var dataset = chart.getXYPlot().getDataset();

		assertEquals(1, chart.getXYPlot().getSeriesCount());
		IntStream.range(0, MAX_VALUES + 1).forEach(i -> {
			final var x = X_START + h * i;
			assertEquals(x, dataset.getX(0, i));
			assertEquals(function.apply(x), dataset.getY(0, i));
		});
		assertEquals(response.getOutputStream(), outputStreamCaptor.getValue());
		assertEquals(ResultsGraphView.WIDTH, widthCaptor.getValue());
		assertEquals(ResultsGraphView.HEIGHT, heightCaptor.getValue());

	}

	@ParameterizedTest()
	@MethodSource("emptyResults")
	void writeChart(Collection<Result> results) throws Exception {

		model.put(ResultsGraphView.RESULTS_MODEL, results);
		model.put(ResultsGraphView.RESULTS_TITLE, TITLE);

		try (final var mocked = Mockito.mockStatic(ChartUtilities.class)) {
			resultsGraphView.renderMergedOutputModel(model, null, response);
			mocked.verify(() -> ChartUtilities.writeChartAsPNG(outputStreamCaptor.capture(), chartCaptor.capture(),
					widthCaptor.capture(), heightCaptor.capture()));

		}
		final JFreeChart chart = chartCaptor.getValue();
		assertEquals(TITLE, chart.getTitle().getText());
		assertEquals(ResultsGraphView.CONTENT_DISPOSITION_HEADER_VALUE,
				response.getHeader(ResultsExcelView.CONTENT_DISPOSITION_HEADER));

		assertEquals(response.getOutputStream(), outputStreamCaptor.getValue());
		assertEquals(ResultsGraphView.WIDTH, widthCaptor.getValue());
		assertEquals(ResultsGraphView.HEIGHT, heightCaptor.getValue());

	}

	private static Stream<Collection<Result>> emptyResults() {
		return Stream.of(Collections.emptyList(), null);
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
		Mockito.when(result.yDerivatives()).thenReturn(new double[] { y });
		return result;
	}

}
