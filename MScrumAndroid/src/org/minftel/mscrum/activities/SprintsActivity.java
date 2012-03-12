package org.minftel.mscrum.activities;

import java.util.List;

import org.minftel.mscrum.model.ProjectDetail;
import org.minftel.mscrum.model.SprintDetail;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
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

		String[] countries = getResources().getStringArray(
				R.array.sprintsList);
		setListAdapter(new ArrayAdapter<String>(this, R.layout.list_item,
				countries));

		ListView lv = getListView();
		lv.setTextFilterEnabled(true);

		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				// When clicked, show a toast with the TextView text
				Toast.makeText(getApplicationContext(),
						"Has selected " + ((TextView) view).getText(),
						Toast.LENGTH_SHORT).show();

//				Intent i = new Intent(TasksActivity.this, ShowTasksActivity.class);
//				i.putExtra("Value1", "This value one for ActivityTwo ");
//				i.putExtra("Value2", "This value two ActivityTwo");
//				startActivity(i);
			}
		});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_sprint, menu);
		return true;
	}	
    
    /** Llamado cuando un elemento del menu es seleccionado. */
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		Intent intent = new Intent(this, AddSprintActivity.class);
	    startActivity(intent);
		
		return true;
	}
	
public void onListItemClick(ListView parent, View v, int position, long id) {
		
		// Get selected project
		SprintDetail selectedSprint = this.sprintList.get(position);

		Toast.makeText(this,
				"Number of selected sprint : " +  selectedSprint.getSprintNumber() + 
				" [ ID: " + selectedSprint.getIdSprint() + " ]",
				Toast.LENGTH_SHORT).show();
          
//        ProjectsTask projectTask = new ProjectsTask(this);
//        projectTask.execute(projectName);       

	}
	

}