package org.minftel.mscrum.activities;

import org.minftel.mscrum.tasks.AddTaskTask;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AddTask extends Activity{
	
	 EditText descriptionTask;
	 EditText timeTask;
	String descripcion;
	String time;
	@Override
    public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.addtask);
            descriptionTask = (EditText) findViewById(R.id.DescriptionTask);
            timeTask = (EditText) findViewById(R.id.TimeTask);
	}
	
	public void save(View view){
		         
          descripcion = descriptionTask.getText().toString();
          time = timeTask.getText().toString();
          
          
          if(checkValues()){
        	  //Envio al servidor para guardar en la bbdd
        	  AddTaskTask addTaskTask = new AddTaskTask(this);
        	  addTaskTask.execute(descripcion, time);
  			
  			    	 
          }else{
        	  Toast.makeText(this, R.string.check_empty_fields, Toast.LENGTH_SHORT).show();
          }
	}
	
	public boolean checkValues(){
		
		if(descripcion.isEmpty() || time.isEmpty()){
			
			return false;
		}
	
		return true;
	}
}