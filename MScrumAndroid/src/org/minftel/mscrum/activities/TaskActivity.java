package org.minftel.mscrum.activities;

import org.minftel.mscrum.utils.TextAdapter;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;

public class TaskActivity extends ListActivity {

	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.task);
		Context ctx = getApplicationContext();
		
		String[] countries = getResources().getStringArray(
				R.array.sprintsList);
		setListAdapter(new TextAdapter(ctx, R.layout.list_item, countries));
		
	}
}
