package org.minftel.mscrum.activities;

import java.util.List;
import java.util.Vector;

import org.json.JSONException;
import org.minftel.mscrum.chart.flot.FlotChartContainer;
import org.minftel.mscrum.chart.flot.FlotDraw;
import org.minftel.mscrum.chart.flot.Options;
import org.minftel.mscrum.chart.flot.data.PointData;
import org.minftel.mscrum.chart.flot.data.SeriesData;
import org.minftel.mscrum.utils.JSONConverter;
import org.minftel.mscrum.utils.ScrumConstants;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import org.minftel.mscrum.activities.R;

public class ChartActivity extends Activity {

	private List<Long> totalTimes;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Get the task list
		String json = getIntent().getExtras().getString("hours");
		Log.i(ScrumConstants.TAG, json);

		try {
			totalTimes = JSONConverter.fromJSONtoHoursList(json);
		} catch (JSONException e) {
			Log.i(ScrumConstants.TAG, "JSONException" + e.getMessage());
		}

		Vector<SeriesData> sds = new Vector<SeriesData>();

		SeriesData sd1 = new SeriesData();

		Vector<PointData> pds = new Vector<PointData>();

		long max = 0;

		int size = totalTimes.size();

		for (int i = 0; i < size; i++) {
			pds.add(new PointData(i, totalTimes.get(i)));
			if (max < totalTimes.get(i)) {
				max = totalTimes.get(i);
			}
		}

		// Series1
		sd1.setData(pds);
		sd1.label = "Burn Down";
		sds.add(sd1);

		Options opt = new Options();

		opt.grid.hoverable = true;

		opt.series.lines.setShow(true);
		opt.series.points.show = true;

		// Add 10 hours to avoid the bar finishes on the chart's top
		opt.yaxis.max = max + 10;
		opt.yaxis.min = 0;

		FlotDraw fd = new FlotDraw(sds, opt, null);

		setContentView(R.layout.charts);

		FlotChartContainer tv = (FlotChartContainer) this
				.findViewById(R.id.flotdraw);

		if (tv != null) {
			tv.setDrawData(fd);
		}

	}
}
