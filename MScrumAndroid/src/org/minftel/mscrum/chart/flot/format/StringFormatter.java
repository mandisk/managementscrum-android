package org.minftel.mscrum.chart.flot.format;

import org.minftel.mscrum.chart.flot.data.SeriesData;

public interface StringFormatter {
	public String format(String str, SeriesData axis);

	public String formatNumber(double val, SeriesData axis);
}
