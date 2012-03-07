package org.minftel.mscrum.activities;

import java.util.concurrent.ExecutionException;

import org.minftel.mscrum.tasks.LoginTask;
import org.minftel.mscrum.utils.ScrumConstants;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {
	
	private EditText email;
	private EditText password;
	private SharedPreferences preferences;
	private Editor editor;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        
        email = (EditText) findViewById(R.id.editTextEmail);
        password = (EditText) findViewById(R.id.editTextPassword);
        
        preferences = getSharedPreferences(ScrumConstants.SHARED_PREFERENCES_FILE, MODE_PRIVATE);
        editor = preferences.edit();
    }
    
    public void doLogin(View view) {
    	String sEmail = email.getText().toString();
    	String sPassword = password.getText().toString();
    	
		LoginTask loginTask = new LoginTask(this);
		loginTask.execute(sEmail, sPassword);
		
    }
    
    public Editor getEditor() {
    	return this.editor;
    }
}