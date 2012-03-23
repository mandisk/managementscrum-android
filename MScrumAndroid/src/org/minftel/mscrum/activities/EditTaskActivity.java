package org.minftel.mscrum.activities;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
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
			Bundle extras = getIntent().getExtras();
			task = (TaskDetail) extras.getSerializable("task");
			String json = extras.getString("users");
			
			userList = JSONConverter.fromJSONtoUserList(json);
			
			//Buscamos e inicializamos los distintos componentes
			taskSpinner = (Spinner)findViewById(R.id.spinner2);
			taskSpinner.setOnItemSelectedListener(
			        new AdapterView.OnItemSelectedListener() {
			        public void onItemSelected(AdapterView<?> parent, android.view.View v, int position, long id) {
			        	taskState = (String) taskSpinner.getSelectedItem();
			        }
			 
			        public void onNothingSelected(AdapterView<?> parent) {
			        }
			});
			
			
			String a = String.valueOf(task.getState());
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
			
			userSpinner = (Spinner) findViewById(R.id.spinner1);
			
			int position=0; //Variable para guardar la posicion en la que se encuentra el usuario que esta asignado a la tarea
			String email = "";
			
			if(task.getUser() != null){
				email = task.getUser().getEmail();
			}
			Log.i(ScrumConstants.TAG, "name: "+email);
			
			//Carga dinamica del spinner de usuario
			String[] items = new String[userList.size()];
			
			int i=0;
			int vueltas=0;
			for(UserDetail user: userList)
			{
				if(user.getEmail().equalsIgnoreCase(email) ){
					if(vueltas!=i){
						position=i+1;
					}else
					{
						position=i;
					}
					
				}
				items[i] = user.getName();
				i++;
				vueltas++;
			
			}
			
			Log.i(ScrumConstants.TAG, "position: "+position);
			
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
			            android.R.layout.simple_spinner_item, items);
			
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			userSpinner.setAdapter(adapter);
			
			userSpinner.setSelection(position);
			
			
			userSpinner.setOnItemSelectedListener(
			        new AdapterView.OnItemSelectedListener() {
			        public void onItemSelected(AdapterView<?> parent, android.view.View v, int position, long id) {
			        	UserDetail userDetail = userList.get(position);
			        	userSelected = userDetail;
			        }
			 
			        public void onNothingSelected(AdapterView<?> parent) {
			            
			        }
			});
		
		} catch (JSONException e) {
			Log.e(ScrumConstants.TAG, "JSONException: "+ e.getMessage());
		}
		
	}
	
	
	
	public void modify(View view) {
		// Envio al servidor para guardar en la bbdd
		Log.i(ScrumConstants.TAG, "Saving modified");

		String timeTask = time.getText().toString();
		String descriptionTask = description.getText().toString();

		String stateTask = "";

		if(taskState.equalsIgnoreCase(getResources().getString(R.string.ToDo)))
			stateTask = "t";
		if(taskState.equalsIgnoreCase(getResources().getString(R.string.Doing)))
			stateTask = "i";
		if(taskState.equalsIgnoreCase(getResources().getString(R.string.Done)))
			stateTask = "d";
		
		if (chekValues(stateTask, userSelected.getName(), timeTask, descriptionTask)) {
			ModifyTaskSendTask modifyTaskTask = new ModifyTaskSendTask(this);
			modifyTaskTask.execute(String.valueOf(task.getIdTask()), stateTask, Integer.toString(userSelected.getId()), timeTask,
					descriptionTask);
		} else {
			Toast.makeText(this, R.string.check_empty_fields,
					Toast.LENGTH_SHORT).show();
		}

	}

	public boolean chekValues(String stateTask, String userTask,
			String timeTask, String descriptionTask) {

    	Log.i(ScrumConstants.TAG, "stateTask: " + stateTask);
    	Log.i(ScrumConstants.TAG, "userTask: " + userTask);
    	Log.i(ScrumConstants.TAG, "timeTask: " + timeTask);
    	Log.i(ScrumConstants.TAG, "edscriptionTask: " + descriptionTask);

		if (stateTask.isEmpty() || userTask.isEmpty() || timeTask.isEmpty()
				|| descriptionTask.isEmpty()) {

			return false;
		}

		return true;
	}
}
