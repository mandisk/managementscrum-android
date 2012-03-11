package org.minftel.mscrum.activities;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;
import org.minftel.mscrum.model.ProjectDetail;

import java.util.List;

import org.minftel.mscrum.utils.JSONConverter;
import org.minftel.mscrum.utils.ScrumConstants;
import org.minftel.mscrum.utils.TextAdapter;

public class ProjectActivity extends ListActivity {
	
	private List<ProjectDetail> projectList;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.project);
		// PRUEBA
		registerForContextMenu(getListView());
		// FIN PRUEBA
		
		// Get Project List
		String json = getIntent().getExtras().getString("projects");
		
		try {
			projectList = JSONConverter.fromJSONtoProjecList(json);
		} catch (JSONException e) {
			Log.e(ScrumConstants.TAG, "JSONException: " + e.getMessage());
		}
		
		int size = projectList.size();
		
		String[] options = new String[size];
		
		for (int i = 0; i < size; i++) {
			ProjectDetail project = projectList.get(i);
			options[i] = project.getName();
		}

		// Load data in ListAdapter
		setListAdapter(new TextAdapter(this, R.layout.list_item, options));

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
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		ProjectDetail projectDetail = this.projectList.get(info.position);
		switch(item.getItemId()) {
		case R.id.ctx_menu_view_users:
			// View Users
			return true;
		case R.id.ctx_menu_delete:
			// Delete Project
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}

	public void onListItemClick(ListView parent, View v, int position, long id) {
		
		// Get selected project
		ProjectDetail selectedProject = this.projectList.get(position);

		Toast.makeText(this,
				"Project selected: " +  selectedProject.getName() + 
				" [ ID: " + selectedProject.getIdProject() + " ]",
				Toast.LENGTH_SHORT).show();
          
//        ProjectsTask projectTask = new ProjectsTask(this);
//        projectTask.execute(projectName);       

	}

}