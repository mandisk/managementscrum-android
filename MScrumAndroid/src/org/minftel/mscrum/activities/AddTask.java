package org.minftel.mscrum.activities;

import java.util.ArrayList;

import org.minftel.mscrum.tasks.AddTaskTask;
import org.minftel.mscrum.utils.ScrumConstants;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.gesture.GestureOverlayView.OnGesturePerformedListener;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AddTask extends Activity implements OnGesturePerformedListener {
	private GestureLibrary gestureLib;

	EditText descriptionTask;
	EditText timeTask;
	String descripcion;
	String time;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addtask);
		// Detección de gesto
		GestureOverlayView gestureOverlayView = new GestureOverlayView(this);
		View inflate = getLayoutInflater().inflate(R.layout.addtask, null);
		gestureOverlayView.addView(inflate);
		gestureOverlayView.addOnGesturePerformedListener(this);
		// gestureOverlayView.setGestureColor(Color.TRANSPARENT);
		gestureOverlayView.setUncertainGestureColor(Color.TRANSPARENT);
		gestureLib = GestureLibraries.fromRawResource(this, R.raw.gestures);
		if (!gestureLib.load()) {
			// finish();
			Log.w(ScrumConstants.TAG, "Gesture not loaded!");
		}
		setContentView(gestureOverlayView);
		descriptionTask = (EditText) findViewById(R.id.DescriptionTask);
		timeTask = (EditText) findViewById(R.id.TimeTask);

	}

	public void save(View view) {

		descripcion = descriptionTask.getText().toString();
		time = timeTask.getText().toString();

		if (checkValues()) {
			// Envio al servidor para guardar en la bbdd
			AddTaskTask addTaskTask = new AddTaskTask(this);
			addTaskTask.execute(descripcion, time);

		} else {
			Toast.makeText(this, R.string.check_empty_fields,
					Toast.LENGTH_SHORT).show();
		}
	}

	public boolean checkValues() {

		if (descripcion.isEmpty() || time.isEmpty()) {

			return false;
		}

		return true;
	}

	public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {
		ArrayList<Prediction> predictions = gestureLib.recognize(gesture);
		for (Prediction prediction : predictions) {
			if (prediction.score > 2.0) {
				if (prediction.name.equalsIgnoreCase("toleft")) {
					onBackPressed();
				}
				if (prediction.name.equalsIgnoreCase("logout")) {
					logOut();
				}
			}
		}
	}

	public void logOut() {

		SharedPreferences prefs = getSharedPreferences(
				ScrumConstants.SHARED_PREFERENCES_FILE, MODE_PRIVATE);
		prefs.edit().clear().commit();
		Intent intent = new Intent(this, LoginActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}
}