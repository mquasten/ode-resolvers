package de.mq.odesolver.support;

import java.util.Collection;
import java.util.Map;

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

import de.mq.odesolver.OdeResult;

@Component
class ResultsGraphView extends AbstractView {

	@Override
	protected void renderMergedOutputModel(final Map<String, Object> model, final HttpServletRequest request,
			final HttpServletResponse response) throws Exception {
		response.setHeader("Content-Disposition", "filename=odeSolverResults.png");
		@SuppressWarnings("unchecked")
		final Collection<OdeResult> results = (Collection<OdeResult>) model.get("results");
		final String ode =  (String) model.get("resultsTitle");

		

		final XYDataset dataset = createDataset(results, ode);

		// Create chart
		final JFreeChart chart = ChartFactory.createXYLineChart("numerische Lösung der DGL", "x", "y", dataset,
				PlotOrientation.VERTICAL, true, true, false);

		final ServletOutputStream os = response.getOutputStream();

		ChartUtilities.writeChartAsPNG(os, chart, 1080, 1080);

	}

	private XYDataset createDataset(final Collection<OdeResult> results, final String title) {
		final XYSeriesCollection dataset = new XYSeriesCollection();
		final XYSeries series = new XYSeries(title);
		results.forEach(or -> series.add(or.x(), or.yDerivative(0)));
		dataset.addSeries(series);
		return dataset;
	}

}
