package de.mq.odesolver.result.support;

import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.AbstractView;

import de.mq.odesolver.Result;
import de.mq.odesolver.solve.OdeResult;

@Component
public class ResultsGraphView extends AbstractView {

	@Override
	protected void renderMergedOutputModel(final Map<String, Object> model, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
		response.setHeader("Content-Disposition", "filename=Funktionsgraph.png");
		@SuppressWarnings("unchecked")
		final List<? extends Result> results = (List<OdeResult>) model.get("results");
		final String ode = (String) model.get("resultsTitle");

		final XYDataset dataset = createDataset(results, ode);

		// Create chart
		final JFreeChart chart = ChartFactory.createXYLineChart(ode, "x", "y", dataset, PlotOrientation.VERTICAL, true, true, false);

		final ServletOutputStream os = response.getOutputStream();

		ChartUtilities.writeChartAsPNG(os, chart, 1080, 1080);

	}

	private XYDataset createDataset(final List<? extends Result> results, final String title) {
		final XYSeriesCollection dataset = new XYSeriesCollection();

		if (results == null) {
			return dataset;

		}

		if (results.size() < 1) {
			return dataset;
		}

		IntStream.range(0, results.get(0).yDerivatives().length).forEach(i -> {
			final StringBuffer text = new StringBuffer("y");
			IntStream.rangeClosed(1, i).forEach(k -> text.append("'"));

			final XYSeries series = new XYSeries(text);
			results.forEach(result -> series.add(result.x(), result.yDerivative(i)));
			dataset.addSeries(series);

		});

		return dataset;
	}

}
