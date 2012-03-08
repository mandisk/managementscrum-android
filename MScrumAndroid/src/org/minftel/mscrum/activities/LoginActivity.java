package org.minftel.mscrum.activities;


import org.minftel.mscrum.tasks.LoginTask;
import org.minftel.mscrum.utils.ScrumConstants;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
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
    	String sEmail = email.getText().toString().trim();
    	String sPassword = password.getText().toString();
    	
    	if (sEmail.isEmpty() || sPassword.isEmpty()) {
    		Toast.makeText(this, getResources().getString(R.string.login_empty_fields), Toast.LENGTH_SHORT).show();
    		return;
    	}
    	
		LoginTask loginTask = new LoginTask(this);
		loginTask.execute(sEmail, sPassword);	
    }
    
    public void getAccount(View view) {
    	Intent intent = new Intent(this, Register.class);
    	startActivity(intent);
    }
    
    public Editor getEditor() {
    	return this.editor;
    }
}