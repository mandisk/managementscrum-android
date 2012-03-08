package org.minftel.mscrum.activities;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.minftel.mscrum.tasks.ProjectsTask;
import org.minftel.mscrum.utils.ScrumConstants;
import org.minftel.mscrum.utils.TextAdapter;

public class ProjectActivity extends ListActivity {
	/** Called when the activity is first created. */

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.project);

		Context ctx = getApplicationContext();
		Resources res = ctx.getResources();

		String[] options = res.getStringArray(R.array.title_names);

		setListAdapter(new TextAdapter(ctx, R.layout.list_item, options));

	}

	public void onListItemClick(ListView parent, View v, int position, long id) {

		// Show position clicked
		Toast.makeText(getApplicationContext(),
				"You have selected " + (position + 1) + "th item",
				Toast.LENGTH_SHORT).show();

		String projectName = ((TextView) v).getText().toString();
		
		ProjectsTask projectTask = new ProjectsTask(this);
		projectTask.execute(projectName);	
	}

}