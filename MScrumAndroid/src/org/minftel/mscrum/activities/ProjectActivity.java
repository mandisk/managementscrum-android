package org.minftel.mscrum.activities;

import java.util.List;

import org.json.JSONException;
import org.minftel.mscrum.model.ProjectDetail;
import org.minftel.mscrum.tasks.DeleteProjectTask;
import org.minftel.mscrum.tasks.DeleteTaskTask;
import org.minftel.mscrum.tasks.EditUserProjectAskTask;
import org.minftel.mscrum.tasks.ProjectsTask;
import org.minftel.mscrum.tasks.UserTask;
import org.minftel.mscrum.utils.JSONConverter;
import org.minftel.mscrum.utils.ScrumConstants;
import org.minftel.mscrum.utils.TextAdapter;
import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
		Log.i(ScrumConstants.TAG,json);

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
			scrumMasters[i] = getString(R.string.scrum_master)
					+ project.getScrumMaster().getName();
		}

		// Load data in ListAdapter
		setListAdapter(new TextAdapter(this, R.layout.list_item, projectNames,
				scrumMasters));

	}
	
	//Stadistic Testing
	public void estadistica(View v){
		Intent intent = new Intent(ProjectActivity.this,ChartActivity.class);
		startActivity(intent);
	}
	//End Testing

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
		String idproject = String.valueOf(projectDetail.getIdProject());

		switch (item.getItemId()) {
		case R.id.ctx_menu_view_users:
			// View Users
			// Convert to string to send
			
			
			UserTask ut = new UserTask(this);
			ut.execute(idproject);
			return true;
		case R.id.ctx_menu_delete:

			// Convert to string to send
			//String idproject = String.valueOf(projectDetail.getIdProject());
			
			DeleteProjectTask dpt = new DeleteProjectTask(this);
			dpt.execute(idproject);

			return true;
			
		case R.id.ctx_menu_edit_project:
			String SmEmail = "";
			
			SharedPreferences prefs;
			prefs = getSharedPreferences(ScrumConstants.SHARED_PREFERENCES_FILE, MODE_PRIVATE);
			prefs.getString("userEmail", SmEmail);
			
			/**********************************************************/
			//Lo de arriba falla ¿por que? <<<<<<<<<<---------------------
			/*******************************************************/
			Log.e(ScrumConstants.TAG, " " + SmEmail);
			Log.e(ScrumConstants.TAG, " " + projectDetail.getScrumMaster().getEmail() );
//			if(projectDetail.getScrumMaster().getEmail() == SmEmail)
//			{
				Intent intent = new Intent(this, EditProjectActivity.class);
				intent.putExtra("name", projectDetail.getName());
				intent.putExtra("description", projectDetail.getDescription());
				intent.putExtra("initdate", projectDetail.getInitialDate());
				intent.putExtra("enddate", projectDetail.getEndDate());
				intent.putExtra("ScrumMaster", projectDetail.getScrumMaster().getEmail());
				startActivity(intent);
//			}
//			else{
//				Toast.makeText(this, "You aren't Scrum Master", Toast.LENGTH_SHORT)
//				.show();
//			}
			return true;
		case R.id.ctx_menu_edit_user_in_project:
			
			EditUserProjectAskTask editprojectTask = new EditUserProjectAskTask(this);
			editprojectTask.execute(Integer.toString(projectDetail.getIdProject()));
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}

	public void onListItemClick(ListView parent, View v, int position, long id) {

		// Get selected project
		ProjectDetail selectedProject = this.projectList.get(position);

//		Toast.makeText(
//				this,
//				"Project selected: " + selectedProject.getName() + " [ ID: "
//						+ selectedProject.getIdProject() + " ]",
//				Toast.LENGTH_SHORT).show();

		// Converted to string to send
		String idProject = String.valueOf(selectedProject.getIdProject());

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
		Intent intent;
		switch (item.getItemId()) {
		case R.id.LogOut:
			SharedPreferences prefs = getSharedPreferences(ScrumConstants.SHARED_PREFERENCES_FILE, MODE_PRIVATE);
			prefs.edit().clear().commit();
			intent = new Intent(this, LoginActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	        startActivity(intent);
			break;
		case R.id.AddProject:
			intent = new Intent(this, AddProjectActivity.class);
			startActivity(intent);
		default:
			break;
		}
		return true;
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
//		super.onBackPressed();
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
	}
	
	

}