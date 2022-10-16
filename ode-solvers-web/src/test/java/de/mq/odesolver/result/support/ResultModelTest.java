package de.mq.odesolver.result.support;

import static java.util.Collections.EMPTY_LIST;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import de.mq.odesolver.Result;
import de.mq.odesolver.solve.OdeResult;

class ResultModelTest {

	private static final String TITLE_FUNCTION = "y=1/2*x**4+k[0]*x**2+k[1]*x**3";
	private static final String TITLE_ODE = "y''=4/x*y'-6/(x**2)*y+x**2";
	private static final int MAX_VALUES = 1700;
	private static final double X_START = 1.6;
	private static final double X_STOP = 3.3;

	private static final double K1 = 6d;
	private static final double K2 = -10d / 3d;
	final Function<Double, Double> function = x -> 1 / 2d * Math.pow(x, 4) + K1 * x * x + K2 * Math.pow(x, 3);
	final Function<Double, Double> derivative = x -> 2 * Math.pow(x, 3) + 2d * K1 * x + 3 * K2 * Math.pow(x, 2);

	private final double h = (X_STOP - X_START) / MAX_VALUES;

	@Test
	void emptyResultModel() {
		final var resultModel = new ResultModel();

		assertEquals(EMPTY, resultModel.getBack());
		assertEquals(EMPTY, resultModel.getTitle());
		assertEquals(EMPTY_LIST, resultModel.getInitialValues());
		assertEquals(EMPTY_LIST, resultModel.getRanges());
		assertEquals(EMPTY_LIST, resultModel.getResults());
	}

	@Test
	void resultsOde() {
		final List<OdeResult> results = IntStream.rangeClosed(0, MAX_VALUES).mapToDouble(i -> X_START + h * i).mapToObj(this::odeResult).collect(Collectors.toList());

		final var resultModel = new ResultModel(results, TITLE_ODE);

		assertEquals(1 + MAX_VALUES, resultModel.getResults().size());
		assertEquals(3, resultModel.getRanges().size());
		final var ranges = resultModel.getRanges().stream().collect(Collectors.toMap(Entry::getKey, Entry::getValue));
		assertRangesXAndY(resultModel);
		// f'' = 6x**2 -20x +12 = 6(x**2-10/3x +2) => x1,2=10/6+-sqrt(28)/6 
		// nur x1=10/6+sqrt(28)/6 liegt im Intervall.
		assertEquals(derivative.apply(10 / 6d + Math.sqrt(28) / 6d), ranges.get(ResultModel.KEY_Y + "'")[0], 1e-6);
		assertEquals(derivative.apply(X_STOP), ranges.get(ResultModel.KEY_Y + "'")[1]);

		assertInitialValuesOde(resultModel);
		assertEquals(TITLE_ODE, resultModel.getTitle());
	}

	@Test
	void resultsOdeEmpty() {
		final var resultModel = new ResultModel(Collections.emptyList(), TITLE_ODE);

		assertEquals(EMPTY_LIST, resultModel.getInitialValues());
		assertEquals(EMPTY_LIST, resultModel.getRanges());
		assertEquals(EMPTY_LIST, resultModel.getResults());
		assertEquals(TITLE_ODE, resultModel.getTitle());
	}

	@Test
	void resultsOdeLessThan2() {
		final var resultModel = new ResultModel(Collections.singletonList(odeResult(X_START)), TITLE_ODE);

		assertEquals(EMPTY_LIST, resultModel.getRanges());
		assertEquals(1, resultModel.getResults().size());

		assertInitialValuesOde(resultModel);
		assertEquals(TITLE_ODE, resultModel.getTitle());
	}

	@Test
	void resultsFunction() {
		final List<Result> results = IntStream.rangeClosed(0, MAX_VALUES).mapToDouble(i -> X_START + h * i).mapToObj(this::functionResult).collect(Collectors.toList());
		final var resultModel = new ResultModel(results, TITLE_FUNCTION, new double[] { K1, K2 });
		
		assertEquals(1 + MAX_VALUES, resultModel.getResults().size());
		assertEquals(2, resultModel.getRanges().size());
		assertRangesXAndY(resultModel);
		assertEquals(2, resultModel.getInitialValues().size());
		final var initialValues = resultModel.getInitialValues().stream().collect(Collectors.toMap(Entry::getKey, Entry::getValue));
		assertEquals(K1, initialValues.get(String.format("%s[0]", ResultModel.KEY_VECTOR_NAME)));
		assertEquals(K2, initialValues.get(String.format("%s[1]", ResultModel.KEY_VECTOR_NAME)));
		assertEquals(TITLE_FUNCTION, resultModel.getTitle());

	}

	@Test
	void resultsFunctionVectorNull() {
		final List<Result> results = IntStream.rangeClosed(0, MAX_VALUES).mapToDouble(i -> X_START + h * i).mapToObj(this::functionResult).collect(Collectors.toList());
		final var resultModel = new ResultModel(results, TITLE_FUNCTION, null);
		
		assertEquals(1 + MAX_VALUES, resultModel.getResults().size());
		assertEquals(2, resultModel.getRanges().size());
		assertRangesXAndY(resultModel);
		assertEquals(0, resultModel.getInitialValues().size());
		assertEquals(TITLE_FUNCTION, resultModel.getTitle());

	}

	private void assertRangesXAndY(final ResultModel resultModel) {
		final var ranges = resultModel.getRanges().stream().collect(Collectors.toMap(Entry::getKey, Entry::getValue));
		assertEquals(X_START, ranges.get(ResultModel.KEY_X)[0]);
		assertEquals(X_STOP, ranges.get(ResultModel.KEY_X)[1]);
		assertEquals(function.apply(2d), ranges.get(ResultModel.KEY_Y)[1]);
		assertEquals(function.apply(3d), ranges.get(ResultModel.KEY_Y)[0]);
	}

	private void assertInitialValuesOde(final ResultModel resultModel) {
		assertEquals(3, resultModel.getInitialValues().size());
		final var initialValues = resultModel.getInitialValues().stream().collect(Collectors.toMap(Entry::getKey, Entry::getValue));
		assertEquals(derivative.apply(X_START), initialValues.get(String.format("%s'(%s)", ResultModel.KEY_Y, ResultModel.KEY_X0)));
		assertEquals(function.apply(X_START), initialValues.get(String.format("%s(%s)", ResultModel.KEY_Y, ResultModel.KEY_X0)));
		assertEquals(X_START, initialValues.get(ResultModel.KEY_X0));
	}

	private OdeResult odeResult(final double x) {

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
		final Result result = Mockito.mock(Result.class);
		Mockito.when(result.x()).thenReturn(x);
		final var y = function.apply(x);
		Mockito.when(result.yDerivative(0)).thenReturn(y);
		Mockito.when(result.yDerivatives()).thenReturn(new double[] { y });
		return result;

	}

}
