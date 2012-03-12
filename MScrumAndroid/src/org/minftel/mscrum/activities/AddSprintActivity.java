package org.minftel.mscrum.activities;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.minftel.mscrum.tasks.AddProjectTask;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AddSprintActivity extends Activity{
	private int numberSprints=0;
	private TextView mDateDisplay1;
	private TextView mPickDate1;
	private TextView mDateDisplay2;
	private TextView mPickDate2;
	
	private TextView mId;
	
	private int mYear1;
	private int mMonth1;
	private int mDay1;

	private int mYear2;
	private int mMonth2;
	private int mDay2;

	private int actualYear;
	private int actualMonth;
	private int actualDay;

	static final int DATE_DIALOG_ID1 = 0;
	static final int DATE_DIALOG_ID2 = 1;
	@Override
    public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.addsprint);
//            numberSprints = getIntent().getExtras().getInt("numberSprint");
            
            // capture our View elements
            mDateDisplay1 = (TextView) findViewById(R.id.addSprintInitialDate);
            mDateDisplay2 = (TextView) findViewById(R.id.addSprintEndDate);
            mPickDate1 = (TextView) findViewById(R.id.addSprintInitialDateLabel);
            mPickDate2 = (TextView) findViewById(R.id.addSprintEndDateLabel);
            mId = (TextView) findViewById(R.id.addSprintId);
            mId.setText(String.valueOf(numberSprints+1));
            // add a click listener to the button
    		mPickDate1.setOnClickListener(new View.OnClickListener() {
    			public void onClick(View v) {
    				showDialog(DATE_DIALOG_ID1);
    			}
    		});

    		mPickDate2.setOnClickListener(new View.OnClickListener() {
    			public void onClick(View v) {
    				showDialog(DATE_DIALOG_ID2);
    			}
    		});

    		// get the current date
    		final Calendar c = Calendar.getInstance();
    		actualYear = mYear2 = mYear1 = c.get(Calendar.YEAR);
    		actualMonth = mMonth2 = mMonth1 = c.get(Calendar.MONTH);
    		actualDay = mDay2 = mDay1 = c.get(Calendar.DAY_OF_MONTH);
    		
    		// display the current date (this method is below)
    		updateDisplay();
	}
	
	// updates the date in the TextView
		private void updateDisplay() {
			mDateDisplay1.setText(new StringBuilder()
					// Month is 0 based so add 1
					.append(mDay1).append("-").append(mMonth1 + 1).append("-")
					.append(mYear1).append(" "));
			mDateDisplay2.setText(new StringBuilder()
					// Month is 0 based so add 1
					.append(mDay2).append("-").append(mMonth2 + 1).append("-")
					.append(mYear2).append(" "));
		}

		// the callback received when the user "sets" the date in the dialog
		private DatePickerDialog.OnDateSetListener mDateSetListener1 = new DatePickerDialog.OnDateSetListener() {
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				mYear1 = year;
				mMonth1 = monthOfYear;
				mDay1 = dayOfMonth;

				updateDisplay();

			}
		};
		private DatePickerDialog.OnDateSetListener mDateSetListener2 = new DatePickerDialog.OnDateSetListener() {
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				mYear2 = year;
				mMonth2 = monthOfYear;
				mDay2 = dayOfMonth;

				updateDisplay();

			}
		};

		@Override
		protected Dialog onCreateDialog(int id) {
			switch (id) {
			case DATE_DIALOG_ID1:
				return new DatePickerDialog(this, mDateSetListener1, mYear1,
						mMonth1, mDay1);
			case DATE_DIALOG_ID2:
				return new DatePickerDialog(this, mDateSetListener2, mYear2,
						mMonth2, mDay2);
			}
			return null;
		}
		
	public void checkValues(View view){
		int envio = 2;
		int res1 = checkDate(mYear1, mMonth1, mDay1, mYear2, mMonth2, mDay2);
		int res2 = checkDate(actualYear, actualMonth, actualDay, mYear1, mMonth1, mDay1);
		String sId = mId.getText().toString().trim();
		
		if(res1 > 0 || res2 > 0){
			Toast.makeText(this, "wrong dates", Toast.LENGTH_SHORT).show();
			envio--;
		} 
		if(sId.isEmpty()){
			Toast.makeText(this, "ID required", Toast.LENGTH_SHORT).show();
			envio--;
		}
		
		if(envio == 2){
			String sDay1 = String.valueOf(mDay1);
			String sMonth1 = String.valueOf(mMonth1);
			String sYear1 = String.valueOf(mYear1);
			String sDay2 = String.valueOf(mDay2);
			String sMonth2 = String.valueOf(mMonth2);
			String sYear2 = String.valueOf(mYear2);
			
			AddProjectTask addProjectTask = new AddProjectTask(this);
			addProjectTask.execute(sId, sDay1, sMonth1, sYear1, sDay2, sMonth2, sYear2);
		}
	}

	public int checkDate(int y1, int m1, int d1, int y2, int m2, int d2) {
		Calendar c1 = new GregorianCalendar();
		c1.set(y1, m1, d1);
		Date date1 = c1.getTime();
		Calendar c2 = new GregorianCalendar();
		c2.set(y2, m2, d2);
		Date date2 = c2.getTime();
		
		return date1.compareTo(date2);
	}

}
