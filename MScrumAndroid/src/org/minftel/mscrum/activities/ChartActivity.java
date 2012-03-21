package org.minftel.mscrum.activities;

import java.util.List;
import java.util.Vector;

import org.json.JSONException;
import org.minftel.mscrum.chart.flot.FlotChartContainer;
import org.minftel.mscrum.chart.flot.FlotDraw;
import org.minftel.mscrum.chart.flot.Options;
import org.minftel.mscrum.chart.flot.data.PointData;
import org.minftel.mscrum.chart.flot.data.SeriesData;
import org.minftel.mscrum.chart.flot.data.TickData;
import org.minftel.mscrum.model.TaskDetail;
import org.minftel.mscrum.utils.JSONConverter;
import org.minftel.mscrum.utils.ScrumConstants;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import org.minftel.mscrum.activities.R;

public class ChartActivity extends Activity {

	private List<TaskDetail> tasks;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Get the task List
		String json = getIntent().getExtras().getString("tasks");
		Log.i(ScrumConstants.TAG, json);

		try {
			tasks = JSONConverter.fromJSONtoTaskList(json);
		} catch (JSONException e) {
			Log.i(ScrumConstants.TAG, "JSONException" + e.getMessage());
		}

		Vector<SeriesData> sds = new Vector<SeriesData>();

		SeriesData sd1 = new SeriesData();

		Vector<PointData> pds = new Vector<PointData>();

		for (int i = 0; i < tasks.size(); i++) {
			TaskDetail td = tasks.get(i);
			pds.add(new PointData(td.getIdTask(), td.getTime()));
		}
		
		// Series1
		sd1.setData(pds);
		sd1.label = "Series 1";
		sds.add(sd1);
	
		Options opt = new Options();

		opt.grid.hoverable = true;

		opt.series.lines.setShow(true);
		opt.series.points.show = true;

		opt.yaxis.max = 10;
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
