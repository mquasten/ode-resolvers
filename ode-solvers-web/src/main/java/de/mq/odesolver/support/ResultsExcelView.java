package de.mq.odesolver.support;

import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractXlsxView;

import de.mq.odesolver.OdeResult;

@Component()
public class ResultsExcelView extends AbstractXlsxView {

	@Override
	protected void buildExcelDocument(final Map<String, Object> model, final Workbook workbook,
			final HttpServletRequest request, final HttpServletResponse response) throws Exception {
		@SuppressWarnings("unchecked")
		final List<OdeResult> results = (List<OdeResult>) model.get("results");
		response.setHeader("Content-Disposition", "attachment; filename=odeSolverResults.xls");
		final Sheet sheet = workbook.createSheet("Numerische Lösung der DGL");
		sheet.setFitToPage(true);

		final Row header = sheet.createRow(0);
		header.createCell(0).setCellValue("x");
		header.createCell(1).setCellValue("y(x)");
		if (results.size() == 0) {
			return;
		}

		if (results.get(0).yDerivatives().length > 1) {
			header.createCell(2).setCellValue("y'(x)");
		}

		IntStream.range(0, results.size()).forEach(i -> writeRow(results, sheet, i));
		
		model.remove("results");

	}

	private void writeRow(final List<OdeResult> results, final Sheet sheet, final int i) {
		final Row date = sheet.createRow(i + 1);
		final OdeResult odeResult = results.get(i);
		date.createCell(0).setCellValue(odeResult.x());
		IntStream.range(0, odeResult.yDerivatives().length)
				.forEach(k -> date.createCell(1 + k).setCellValue(odeResult.yDerivative(k)));
	}

}
