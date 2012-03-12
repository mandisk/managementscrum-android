package org.minftel.mscrum.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class AddTask extends Activity{
	@Override
    public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.addtask);
            
           
            
	}
	
	public void save(View view){
		 String nameTask  =  findViewById(R.id.nameTask).toString();
         String descriptionTask = findViewById(R.id.DescriptionTask).toString();
         String timeTask = findViewById(R.id.TimeTask).toString();
	}
}