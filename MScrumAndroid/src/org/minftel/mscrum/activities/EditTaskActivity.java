package org.minftel.mscrum.activities;

import org.minftel.mscrum.tasks.ModifyTaskTask;
import org.minftel.mscrum.utils.ScrumConstants;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

public class EditTaskActivity extends Activity {

	private EditText state;
	private EditText user;
	private EditText description;
	private EditText time;
	private RadioGroup radioGroup;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edittask);
		// Get SelectedTask
		Bundle extras = getIntent().getExtras();

		if (extras == null) {
			return;
		}
		radioGroup = (RadioGroup) findViewById(R.id.radiogroup);
		// int checkedRadioButton = radioGroup.getCheckedRadioButtonId();
		// state = (EditText) findViewById(R.id.stateTaskEditView);
		String a = String.valueOf(extras.getChar("state"));
		// state.setHint(a);
		switch (a.charAt(0)) {
		case 't':
			radioGroup.check(R.id.todo);
			break;
		case 'i':
			radioGroup.check(R.id.doing);
			break;
		case 'd':
			radioGroup.check(R.id.done);
			break;
		default:
			break;
		}

		time = (EditText) findViewById(R.id.timeTaskEdit);
		time.setHint(Integer.toString(extras.getInt("time")));

		user = (EditText) findViewById(R.id.UserTaskEdit);
		user.setHint(extras.getString("user"));

		description = (EditText) findViewById(R.id.DescriptionTaskEdit);
		description.setHint(extras.getString("description"));

	}

	public void modify(View view) {
		// Envio al servidor para guardar en la bbdd
		Log.i(ScrumConstants.TAG, "Saving modified");

		// String stateTask = state.getText().toString();
		String userTask = user.getText().toString();
		String timeTask = time.getText().toString();
		String descriptionTask = description.getText().toString();

		int checkedRadioButton = radioGroup.getCheckedRadioButtonId();

		String stateTask = "";

		switch (checkedRadioButton) {
		case R.id.todo:
			stateTask = "t";
			break;
		case R.id.doing:
			stateTask = "i";
			break;
		case R.id.done:
			stateTask = "d";
			break;
		}
		if (chekValues(stateTask, userTask, timeTask, descriptionTask)) {
			ModifyTaskTask modifyTaskTask = new ModifyTaskTask(this);
			modifyTaskTask.execute(stateTask, userTask, timeTask,
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
