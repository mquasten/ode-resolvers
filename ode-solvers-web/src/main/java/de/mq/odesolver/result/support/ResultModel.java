package de.mq.odesolver.result.support;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.IntStream;

import org.springframework.util.CollectionUtils;

import de.mq.odesolver.Result;
import de.mq.odesolver.function.support.FunctionResultImpl;

public class ResultModel {

	private final String back;
	private final String title;
	private final Collection<Result> results = new ArrayList<>();

	private final Collection<Entry<String, double[]>> ranges = new ArrayList<>();

	private final Collection<Entry<String, Double>> initialValues = new ArrayList<>();

	ResultModel() {
		this.back = "";
		this.title = "";
	}

	public ResultModel(final List<? extends Result> results, final String title) {
		this.title = title;
		this.results.addAll(results);
		this.back = "solve";
		calculateRanges(results);
		calculateInitialValues(results);
	}

	public ResultModel(final List<? extends Result> results, final String title, final double[] kVector) {
		this.title = title;
		this.results.addAll(results);
		this.back = "function";
		calculateRanges(results);
		calculateInitialValues(kVector);
	}

	void calculateRanges(final List<? extends Result> results) {
		ranges.clear();
		if (CollectionUtils.isEmpty(results)) {
			return;
		}

		if (results.size() < 2) {
			return;
		}

		final Result firstResult = results.get(0);
		final Result lastResult = results.get(results.size() - 1);
		ranges.add(new SimpleImmutableEntry<>("x", FunctionResultImpl.doubleArray(firstResult.x(), lastResult.x())));

		IntStream.range(0, firstResult.yDerivatives().length).forEach(i -> addRangeDerivative(ranges, results, i));

	}

	void calculateInitialValues(final List<? extends Result> results) {
		initialValues.clear();

		if (CollectionUtils.isEmpty(results)) {
			return;
		}
		final Result initialValue = results.get(0);

		initialValues.add(new SimpleImmutableEntry<>("x0", initialValue.x()));

		IntStream.range(0, initialValue.yDerivatives().length).forEach(i -> {
			final StringBuffer text = new StringBuffer("y");
			IntStream.rangeClosed(1, i).forEach(k -> text.append("'"));
			text.append("(x0)");
			initialValues.add(new SimpleImmutableEntry<>(text.toString(), initialValue.yDerivative(i)));
		});

	}

	void calculateInitialValues(final double[] kVector) {
		initialValues.clear();

		if (kVector == null) {
			return;
		}

		IntStream.range(0, kVector.length)
				.forEach(i -> initialValues.add(new SimpleImmutableEntry<>(String.format("k[%d]", i), kVector[i])));
	}

	private void addRangeDerivative(final Collection<Entry<String, double[]>> ranges,
			final Collection<? extends Result> results, final int yDerivative) {
		final Optional<Double> min = results.stream().map(r -> r.yDerivative(yDerivative))
				.min((x1, x2) -> x1.compareTo(x2));
		final Optional<Double> max = results.stream().map(r -> r.yDerivative(yDerivative))
				.max((x1, x2) -> x1.compareTo(x2));
		final StringBuffer text = new StringBuffer("y");
		IntStream.rangeClosed(1, yDerivative).forEach(i -> text.append("'"));
		if (min.isPresent() && max.isPresent()) {
			ranges.add(
					new SimpleImmutableEntry<>(text.toString(), FunctionResultImpl.doubleArray(min.get(), max.get())));
		}
	}

	public String getBack() {
		return back;
	}

	public String getTitle() {
		return title;
	}

	public Collection<? extends Result> getResults() {
		return results;
	}

	public final Collection<Entry<String, double[]>> getRanges() {
		return ranges;
	}

	public final Collection<Entry<String, Double>> getInitialValues() {
		return initialValues;
	}

}
