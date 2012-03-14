package org.minftel.mscrum.activities;

import java.util.List;

import org.json.JSONException;
import org.minftel.mscrum.model.ProjectDetail;
import org.minftel.mscrum.model.TaskDetail;
import org.minftel.mscrum.tasks.AddTaskTask;
import org.minftel.mscrum.tasks.ModifyTaskTask;
import org.minftel.mscrum.utils.JSONConverter;
import org.minftel.mscrum.utils.ScrumConstants;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class EditTaskActivity extends Activity {

	private String name;
	private String state;
	private String user;
	private String sprint;
	private String description;
	private String time;
	private List<TaskDetail> taskList;
	
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edittask);
		// Get SelectedTask
		 Bundle extras = getIntent().getExtras();
	       
		 if(extras==null){
	        	return;
	        }
		
		EditText state = (EditText) findViewById(R.id.stateTaskEditView);
		String a = String.valueOf(extras.getChar("state"));
		state.setHint(a);
		EditText time = (EditText) findViewById(R.id.timeTaskEdit);

		time.setHint(Integer.toString(extras.getInt("time")));
		EditText user =  (EditText) findViewById(R.id.UserTaskEdit);
		user.setHint(extras.getString("user"));
//		EditText sprint = (EditText) findViewById(R.id.SprintTask);
//		sprint.setHint(extras.getString("sprint"));
		EditText description = (EditText) findViewById(R.id.DescriptionTaskEdit);
		description.setHint(extras.getString("description"));
		
	}

	public void modify(View view) {
		
		state = findViewById(R.id.stateTaskEditView).toString();
		user =  findViewById(R.id.UserTaskEdit).toString();
		sprint =  findViewById(R.id.SprintTaskEdit).toString();
		time = findViewById(R.id.TimeTask).toString();
		description =  findViewById(R.id.DescriptionTask).toString();
		
		  //Envio al servidor para guardar en la bbdd
  	 	ModifyTaskTask modifyTaskTask = new ModifyTaskTask(this);
  	 	modifyTaskTask.execute(state, user, time, sprint, description);
	}
}
