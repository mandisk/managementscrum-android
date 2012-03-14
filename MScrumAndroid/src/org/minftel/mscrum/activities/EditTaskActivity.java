package org.minftel.mscrum.activities;

import org.minftel.mscrum.tasks.ModifyTaskTask;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class EditTaskActivity extends Activity {

	private EditText state;
	private EditText user;
	private EditText sprint;
	private EditText description;
	private EditText time;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edittask);
		// Get SelectedTask
		 Bundle extras = getIntent().getExtras();
	       
		 if(extras==null){
	        	return;
	        }
		
		 state = (EditText) findViewById(R.id.stateTaskEditView);
		String a = String.valueOf(extras.getChar("state"));
		state.setHint(a);
		 time = (EditText) findViewById(R.id.timeTaskEdit);

		time.setHint(Integer.toString(extras.getInt("time")));
		 user =  (EditText) findViewById(R.id.UserTaskEdit);
		user.setHint(extras.getString("user"));
//		EditText sprint = (EditText) findViewById(R.id.SprintTask);
//		sprint.setHint(extras.getString("sprint"));
		 description = (EditText) findViewById(R.id.DescriptionTaskEdit);
		description.setHint(extras.getString("description"));
		
	}

	public void modify(View view) {
		  //Envio al servidor para guardar en la bbdd
  	 	ModifyTaskTask modifyTaskTask = new ModifyTaskTask(this);
  	 	modifyTaskTask.execute(state.getText().toString(), user.getHint().toString(), time.getText().toString(),
  	 			sprint.getText().toString(), description.getText().toString());
	}
}
