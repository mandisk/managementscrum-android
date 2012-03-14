package org.minftel.mscrum.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class AddTask extends Activity{
	String nameTask;
	String descriptionTask;
	String timeTask;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.addtask);
            
           
            
	}
	
	public void save(View view){
		  nameTask  =  findViewById(R.id.nameTask).toString();
          descriptionTask = findViewById(R.id.DescriptionTask).toString();
          timeTask = findViewById(R.id.TimeTask).toString();
	}
	
	public boolean checkValues(View view){
		
		if(nameTask.isEmpty() || descriptionTask.isEmpty() || timeTask.isEmpty()){
			Toast.makeText(this, R.string.check_empty_fields, Toast.LENGTH_SHORT).show();
			return false;
		}
	
		return true;
	}
}