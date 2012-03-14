package org.minftel.mscrum.activities;

import org.minftel.mscrum.tasks.AddTaskTask;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class AddTask extends Activity{
	
	String descriptionTask;
	String timeTask;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.addtask);
	}
	
	public void save(View view){
		  
          descriptionTask = findViewById(R.id.DescriptionTask).toString();
          timeTask = findViewById(R.id.TimeTask).toString();
          
          if(checkValues()){
        	  //Envio al servidor para guardar en la bbdd
        	  AddTaskTask addTaskTask = new AddTaskTask(this);
        	  addTaskTask.execute(descriptionTask, timeTask);
  			
  			    	 
          }else{
        	  Toast.makeText(this, R.string.check_empty_fields, Toast.LENGTH_SHORT).show();
          }
	}
	
	public boolean checkValues(){
		
		if(descriptionTask.isEmpty() || timeTask.isEmpty()){
			
			return false;
		}
	
		return true;
	}
}