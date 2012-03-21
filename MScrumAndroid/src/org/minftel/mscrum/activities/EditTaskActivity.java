package org.minftel.mscrum.activities;

import java.util.List;

import org.json.JSONException;
import org.minftel.mscrum.model.TaskDetail;
import org.minftel.mscrum.model.UserDetail;
import org.minftel.mscrum.tasks.ModifyTaskSendTask;
import org.minftel.mscrum.utils.JSONConverter;
import org.minftel.mscrum.utils.ScrumConstants;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

public class EditTaskActivity extends Activity {

	private EditText description;
	private EditText time;
	private List<UserDetail> userList;
	private TaskDetail task;
	private Spinner taskSpinner;
	private Spinner userSpinner;
	private String taskState;
	private UserDetail userSelected;
	
	public void onCreate(Bundle savedInstanceState) {
		
		
		try{
			super.onCreate(savedInstanceState);
			setContentView(R.layout.edittask);
		
			
			//Recogida informacion servidor
			String json = getIntent().getExtras().getString(
					"usersinproject");
			String json2 = getIntent().getExtras().getString("task");
			
			userList = JSONConverter.fromJSONtoUserList(json);
			task = JSONConverter.fromJSONObjectToTaskDetail(json2);
	
			
			//Buscamos e inicializamos los distintos componentes
			taskSpinner = (Spinner)findViewById(R.id.spinner1);
			taskState = taskSpinner.getSelectedItem().toString();
			
			
			String a = String.valueOf(task.getState());
			// state.setHint(a);
			switch (a.charAt(0)) 
			{
			case 't':
				taskSpinner.setSelection(0);
				break;
			case 'i':
				taskSpinner.setSelection(1);
				break;
			case 'd':
				taskSpinner.setSelection(2);
				break;
			default:
				break;
			}
	
			time = (EditText) findViewById(R.id.timeTaskEdit);
			time.setText(Integer.toString(task.getTime()));
		
			description = (EditText) findViewById(R.id.DescriptionTaskEdit);
			description.setText(task.getDescription());
			
			//Carga dinamica del spinner de usuario
			String[] items = new String[userList.size()];
			
			int i=0;
			for(UserDetail user: userList)
			{
				items[i] = user.getName();
				i++;
			}
			
			userSpinner = (Spinner) findViewById(R.id.spinner1);
			
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
			            android.R.layout.simple_spinner_item, items);
			
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			userSpinner.setAdapter(adapter);
			
			
			userSpinner.setOnItemSelectedListener(
			        new AdapterView.OnItemSelectedListener() {
			        public void onItemSelected(AdapterView<?> parent,
			            android.view.View v, int position, long id) {
			        	userSelected = userList.get(position);
			        }
			 
			        public void onNothingSelected(AdapterView<?> parent) {
			            
			        }
			});
		
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	
	public void modify(View view) {
		// Envio al servidor para guardar en la bbdd
		Log.i(ScrumConstants.TAG, "Saving modified");

		// String stateTask = state.getText().toString();
		String timeTask = time.getText().toString();
		String descriptionTask = description.getText().toString();

		

		String stateTask = "";

		if(taskState.equalsIgnoreCase("todo"))
			stateTask = "t";
		if(taskState.equalsIgnoreCase("doing"))
			stateTask = "i";
		if(taskState.equalsIgnoreCase("done"))
			stateTask = "d";
		
		if (chekValues(stateTask, userSelected.getName(), timeTask, descriptionTask)) {
			ModifyTaskSendTask modifyTaskTask = new ModifyTaskSendTask(this);
			modifyTaskTask.execute(stateTask, Integer.toString(userSelected.getId()), timeTask,
					descriptionTask);
		} else {
			Toast.makeText(this, R.string.check_empty_fields,
					Toast.LENGTH_SHORT).show();
		}

	}

	public boolean chekValues(String stateTask, String userTask,
			String timeTask, String descriptionTask) {

		if (stateTask.isEmpty() || userTask.isEmpty() || timeTask.isEmpty()
				|| descriptionTask.isEmpty()) {

			return false;
		}

		return true;
	}
}
