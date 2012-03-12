package org.minftel.mscrum.activities;

import java.util.List;

import org.minftel.mscrum.model.ProjectDetail;
import org.minftel.mscrum.model.TaskDetail;
import org.minftel.mscrum.utils.TextAdapter;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class TaskActivity extends ListActivity {

	private List<TaskDetail> taskList;
	
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.task);
		Context ctx = getApplicationContext();

//		String[] countries = getResources().getStringArray(R.array.sprintsList);
		//Antonio aqui te he comentado el TextAdapter para que no te de errores. He hecho una modificación
		//en la clase. Lo unico que tienes que añadir es un campo más para la subopción. Si entras en
		//ProjectActivity verás que el campo nuevo se añade de la misma forma. Cargas los datos que quieres
		//mostrar y añades la variable del submenu al constructor del setListAdapter
		//setListAdapter(new TextAdapter(ctx, R.layout.list_item, countries));

	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_task, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();

		switch (item.getItemId()) {
		case R.id.addTask:
			// View task description
			return true;
		case R.id.deleteTask:
			// Delete Project
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}

	public void onListItemClick(ListView parent, View v, int position, long id) {

		// Get selected project
		TaskDetail selectedTask = this.taskList.get(position);

		Toast.makeText(this,
				"Project selected: " +  selectedTask.getDescription() + 
				" [ ID: " + selectedTask.getIdTask() + " ]" +  "Time: " + selectedTask.getTime(),
				Toast.LENGTH_SHORT).show();
	

	}

	
}
