package org.minftel.mscrum.activities;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;
import org.minftel.mscrum.model.ProjectDetail;

import java.util.ArrayList;
import java.util.List;

import org.minftel.mscrum.tasks.ProjectsTask;
import org.minftel.mscrum.utils.JSONConverter;
import org.minftel.mscrum.utils.TextAdapter;

public class ProjectActivity extends ListActivity {
	/** Called when the activity is first created. */

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.project);

		Context ctx = getApplicationContext();

		int i = 0;
		Intent intent = getIntent();
		Bundle datos = intent.getExtras();
		String json = datos.getString("projects");
		List<ProjectDetail> projects = new ArrayList<ProjectDetail>();
		
		try {
			projects = JSONConverter.fromJSONtoProjecList(json);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Initialize Array String
		String[] options = new String[projects.size()];

		for (ProjectDetail project : projects) {
			options[i] = project.getName();
			i++;
		}

		// Load data in ListAdapter
		setListAdapter(new TextAdapter(ctx, R.layout.list_item, options));

	}

	public void onListItemClick(ListView parent, View v, int position, long id) {
		//Intent intent = null;
		// Show position clicked

		Toast.makeText(getApplicationContext(),
				"You have selected " + (position + 1) + "th item",
				Toast.LENGTH_SHORT).show();
		//Alejandro
		//  String projectName = ((TextView) v).getText().toString();
          
        //   ProjectsTask projectTask = new ProjectsTask(this);
       //   projectTask.execute(projectName);       

	}

}