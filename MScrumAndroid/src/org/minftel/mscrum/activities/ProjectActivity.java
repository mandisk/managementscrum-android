package org.minftel.mscrum.activities;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;
import org.minftel.mscrum.model.ProjectDetail;

import java.util.List;

import org.minftel.mscrum.tasks.DeleteProjectTask;
import org.minftel.mscrum.tasks.ProjectsTask;
import org.minftel.mscrum.utils.JSONConverter;
import org.minftel.mscrum.utils.ScrumConstants;
import org.minftel.mscrum.utils.TextAdapter;

public class ProjectActivity extends ListActivity {

	private List<ProjectDetail> projectList;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.project);

		// Context menu
		registerForContextMenu(getListView());

		// Get Project List
		String json = getIntent().getExtras().getString("projects");

		try {
			projectList = JSONConverter.fromJSONtoProjecList(json);
		} catch (JSONException e) {
			Log.e(ScrumConstants.TAG, "JSONException: " + e.getMessage());
		}

		int size = projectList.size();

		String[] projectNames = new String[size];
		String[] scrumMasters = new String[size];

		for (int i = 0; i < size; i++) {
			ProjectDetail project = projectList.get(i);
			projectNames[i] = project.getName();
			scrumMasters[i] = "Scrum Master: "
					+ project.getScrumMaster().getName();
		}

		// Load data in ListAdapter
		setListAdapter(new TextAdapter(this, R.layout.list_item, projectNames,
				scrumMasters));

	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_ctx_projects, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {

		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		ProjectDetail projectDetail = this.projectList.get(info.position);

		switch (item.getItemId()) {
		case R.id.ctx_menu_view_users:
			// View Users
			return true;
		case R.id.ctx_menu_delete:

			String idproject = String.valueOf(projectDetail.getIdProject());

			DeleteProjectTask dpt = new DeleteProjectTask(this);
			dpt.execute(idproject);

			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}

	public void onListItemClick(ListView parent, View v, int position, long id) {

		// Get selected project
		ProjectDetail selectedProject = this.projectList.get(position);

		Toast.makeText(
				this,
				"Project selected: " + selectedProject.getName() + " [ ID: "
						+ selectedProject.getIdProject() + " ]",
				Toast.LENGTH_SHORT).show();

		// Converted to string to send
		String idProject = "" + selectedProject.getIdProject();

		ProjectsTask projectTask = new ProjectsTask(this);
		projectTask.execute(idProject);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_project, menu);
		return true;
	}

	/** Called when an item is selected. */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.LogOut:
			Toast.makeText(this, "Log out", Toast.LENGTH_SHORT).show();
			SharedPreferences prefs;
			prefs = PreferenceManager.getDefaultSharedPreferences(this);
			prefs.edit().putString("userEmail", "").putString("pass", "")
					.commit();
			break;
		case R.id.AddProject:
			Intent intent = new Intent(this, AddProjectActivity.class);
			startActivity(intent);
		default:
			break;
		}
		return true;
	}

}