package org.minftel.mscrum.activities;

import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.minftel.mscrum.model.ProjectDetail;
import org.minftel.mscrum.model.SprintDetail;
import org.minftel.mscrum.tasks.ProjectsTask;
import org.minftel.mscrum.tasks.SprintsTask;
import org.minftel.mscrum.utils.JSONConverter;
import org.minftel.mscrum.utils.ScrumConstants;
import org.minftel.mscrum.utils.TextAdapter;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SprintsActivity extends ListActivity {

	private List<SprintDetail> sprintList = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sprints);

		String[] sprintNumbers = null;
		String[] dates = null;

		registerForContextMenu(getListView());

		// Get theSprints List
		String json = getIntent().getExtras().getString("sprints");
		
		try {
			sprintList = JSONConverter.fromJSONtoSprintList(json);
			dates = new String[sprintList.size()];
			sprintNumbers = new String[sprintList.size()];
		} catch (Exception e) {
			Log.e(ScrumConstants.TAG, "JSONException: " + e.getMessage());
		}

		for (int i = 0; i < sprintList.size(); i++) {
			SprintDetail sprint = sprintList.get(i);
			sprintNumbers[i] = "" + sprint.getIdSprint();
			dates[i] = "From " + sprint.getInitialDate() + " to " + sprint.getEndDate();
		}

		// Load data in ListAdapter
		setListAdapter(new TextAdapter(this, R.layout.list_item, sprintNumbers,
				dates));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_sprint, menu);
		return true;
	}

	/** Called when an item of the menu is selected. */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = new Intent(this, AddSprintActivity.class);
		startActivity(intent);

		return true;
	}

	/** Called when an item of the list is selected. */
	public void onListItemClick(ListView parent, View v, int position, long id) {

		// Get selected sprint
		SprintDetail selectedSprint = this.sprintList.get(position);

		Toast.makeText(
				this,
				"Number of selected sprint : "
						+ selectedSprint.getSprintNumber() + " [ ID: "
						+ selectedSprint.getIdSprint() + " ]",
				Toast.LENGTH_SHORT).show();

		// Converted to string to send
		String idSprint = String.valueOf(selectedSprint.getIdSprint());

		SprintsTask sprintTask = new SprintsTask(this);
		sprintTask.execute(idSprint);

	}

}