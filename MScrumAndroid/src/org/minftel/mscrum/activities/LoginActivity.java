package org.minftel.mscrum.activities;


import java.util.ArrayList;

import org.minftel.mscrum.tasks.LoginTask;
import org.minftel.mscrum.utils.ScrumConstants;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
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
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity implements OnGesturePerformedListener{

	private GestureLibrary gestureLib;
	private EditText email;
	private EditText password;
	private SharedPreferences preferences;
	private Editor editor;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // No title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        setContentView(R.layout.login);
        
      //Detección de gesto
      		GestureOverlayView gestureOverlayView = new GestureOverlayView(this);
//      		gestureOverlayView.setGestureColor(Color.TRANSPARENT);
      		gestureOverlayView.setUncertainGestureColor(Color.TRANSPARENT);
      		View inflate = getLayoutInflater().inflate(R.layout.login, null);
      		gestureOverlayView.addView(inflate);
      		gestureOverlayView.addOnGesturePerformedListener(this);
      		gestureLib = GestureLibraries.fromRawResource(this, R.raw.gestures);
      		if (!gestureLib.load()) {
//      			finish();
      			Log.w(ScrumConstants.TAG, "Gesture not loaded!");
      		}
      		setContentView(gestureOverlayView);
      		
        email = (EditText) findViewById(R.id.editTextEmail);
        password = (EditText) findViewById(R.id.editTextPassword);
        
        preferences = getSharedPreferences(ScrumConstants.SHARED_PREFERENCES_FILE, MODE_PRIVATE);
        editor = preferences.edit();
    }
    
    public void doLogin(View view) {
    	String sEmail = email.getText().toString().trim();
    	String sPassword = password.getText().toString();
    	Log.e(ScrumConstants.TAG, " "+sPassword);
    	if (sEmail.isEmpty() || sPassword.isEmpty()) {
    		Toast.makeText(this, getResources().getString(R.string.login_empty_fields), Toast.LENGTH_SHORT).show();
    		return;
    	}
    	
		LoginTask loginTask = new LoginTask(this);
		loginTask.execute(sEmail, sPassword);	
    }
    
    /** Start Register Activity. */
    public void getAccount(View view) {
    	Intent intent = new Intent(this, Register.class);
    	startActivity(intent);
    }
    
    public Editor getEditor() {
    	return this.editor;
    }
    public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {
		ArrayList<Prediction> predictions = gestureLib.recognize(gesture);
		for (Prediction prediction : predictions) {
			Log.i(ScrumConstants.TAG, String.valueOf(prediction.score));
			if (prediction.score > 2.0) {
				if(prediction.name.equalsIgnoreCase("toleft")){
					onBackPressed();
				}
			}
		}
	}
}