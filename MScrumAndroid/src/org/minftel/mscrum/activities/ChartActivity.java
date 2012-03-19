package org.minftel.mscrum.activities;

import java.util.Vector;

import org.minftel.mscrum.chart.flot.FlotChartContainer;
import org.minftel.mscrum.chart.flot.FlotDraw;
import org.minftel.mscrum.chart.flot.Options;
import org.minftel.mscrum.chart.flot.data.PointData;
import org.minftel.mscrum.chart.flot.data.SeriesData;
import org.minftel.mscrum.chart.flot.data.TickData;

import android.app.Activity;
import android.os.Bundle;

import org.minftel.mscrum.activities.R;

public class ChartActivity extends Activity {


    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
               
        Vector<SeriesData> sds = new Vector<SeriesData>();
		
		SeriesData sd1 = new SeriesData();
		
		Vector<PointData> pds = new Vector<PointData>();
		
		//for(double i=0; i < 14; i+=0.5) {
		//	pds.add(new PointData(i, Math.sin(i)));
	   //	}
		pds.add(new PointData(1, 2));
		pds.add(new PointData(2, 1));
		pds.add(new PointData(3, 3));
		pds.add(new PointData(4, 6));
		pds.add(new PointData(5, 8));
		
		//Series1
		sd1.setData(pds);
		sd1.label = "Series 1";
		sds.add(sd1);
				
		//Series2
		SeriesData sd2 = new SeriesData();
		Vector<PointData> pds1 = new Vector<PointData>();
		
		//for(double i=0; i < 14; i+=0.5) {
		//	pds1.add(new PointData(i, Math.cos(i)));
	    //	}
		pds1.add(new PointData(1, 2));
		pds1.add(new PointData(2, 3));
		pds1.add(new PointData(3, 2));
		pds1.add(new PointData(4, 7));
		pds1.add(new PointData(5, 9));
		
		sd2.setData(pds1);
		sd2.label = "Series 2";
		sds.add(sd2);
		
        Options opt = new Options();
		
		opt.grid.hoverable = true;
		
		opt.series.lines.setShow(true);
		opt.series.points.show = true;
		
		opt.yaxis.max = 10;
		opt.yaxis.min = 0;

		
		FlotDraw fd = new FlotDraw(sds, opt, null);

        setContentView(R.layout.charts);
        
        FlotChartContainer tv = (FlotChartContainer)this.findViewById(R.id.flotdraw);
        
        if(tv != null) {
            tv.setDrawData(fd);
        }
        
    }
}
