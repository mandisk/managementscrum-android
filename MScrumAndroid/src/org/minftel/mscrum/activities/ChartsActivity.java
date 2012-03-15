package org.minftel.mscrum.activities;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class ChartsActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.charts);
		
		// Sample with Google API
		WebView googleChartView = new WebView(this);
		setContentView(googleChartView);
		String mUrl = "https://chart.googleapis.com/chart?cht=p3&chs=250x100&chd=t:60,40&chl=Hello|World";
		googleChartView.loadUrl(mUrl);
		}
		
	}


