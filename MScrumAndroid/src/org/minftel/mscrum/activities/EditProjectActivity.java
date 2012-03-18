package org.minftel.mscrum.activities;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.minftel.mscrum.activities.R.id;
import org.minftel.mscrum.tasks.AddProjectTask;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class EditProjectActivity extends Activity {

	protected static final int DATE_DIALOG_ID1 = 0;
	protected static final int DATE_DIALOG_ID2 = 1;
	private EditText nameEdit;
	private EditText descripctionEdit;
	private TextView initDateEdit;
	private TextView endDateEdit;
	private ImageView mPickDate1;
	private ImageView mPickDate2;
	private int mDay1;
	private int mMonth1;
	private int mYear1;
	private int mDay2;
	private int mMonth2;
	private int mYear2;
	private int actualYear;
	private int actualMonth;
	private int actualDay;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.editproject);
		// Get SelectedTask
		Bundle extras = getIntent().getExtras();

		if (extras == null) {
			return;
		}

		nameEdit = (EditText) findViewById(R.id.nameProjectEdit);
		nameEdit.setHint(extras.getString("name"));
		
		descripctionEdit = (EditText) findViewById(R.id.descriptionEditProject);
		descripctionEdit.setHint(extras.getString("description"));
		
		initDateEdit = (TextView) findViewById(R.id.EditInitialDateProjectRes);
	//	initDateEdit.setText(extras.getString("initdate"));
		
		endDateEdit = (TextView) findViewById(R.id.EditEndDateProjectRes);
		//endDateEdit.setText(extras.getString("enddate"));
		
		mPickDate1 = (ImageView) findViewById(R.id.editProjectInitialCalendar);
		mPickDate2 = (ImageView) findViewById(R.id.editProjectEndCalendar);

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
		 actualYear = c.get(Calendar.YEAR);
		 actualMonth =  c.get(Calendar.MONTH);
		 actualDay = c.get(Calendar.DAY_OF_MONTH);

		// display the current date (this method is below)
		 updateDisplay();
	}

	// updates the date in the TextView
	private void updateDisplay() {
		DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
		Date date = new Date();
		date.setDate(mDay1);
		date.setMonth(mMonth1);
		date.setYear(mYear1);
		String fecha = df.format(date);
		initDateEdit.setText(fecha);
		date.setDate(mDay2);
		date.setMonth(mMonth2);
		date.setYear(mYear2);
		fecha = df.format(date);
		endDateEdit.setText(fecha);
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
	
	public int checkDate(int y1, int m1, int d1, int y2, int m2, int d2) {
		Calendar c1 = new GregorianCalendar();
		c1.set(y1, m1, d1);
		Date date1 = c1.getTime();
		Calendar c2 = new GregorianCalendar();
		c2.set(y2, m2, d2);
		Date date2 = c2.getTime();

		return date1.compareTo(date2);
	}
	
	

	public void save(View view) {
	
		
		
		int envio = 3;
		int res1 = checkDate(mYear1, mMonth1, mDay1, mYear2, mMonth2, mDay2);
		int res2 = checkDate(actualYear, actualMonth, actualDay, mYear1,
				mMonth1, mDay1);
		
		String name = nameEdit.getText().toString();
		String description = descripctionEdit.getText().toString();
		
		if (res1 > 0 || res2 > 0) {
			Toast.makeText(this, R.string.wrong_dates, Toast.LENGTH_SHORT)
					.show();
			envio--;
		}
		if (name.isEmpty()) {
			Toast.makeText(this, R.string.name_equired, Toast.LENGTH_SHORT)
					.show();
			envio--;
		}

		if (description.isEmpty()) {
			Toast.makeText(this, R.string.description_required,
					Toast.LENGTH_SHORT).show();
			envio--;
		}
		if (envio == 3) {
			String sDay1 = String.valueOf(mDay1);
			String sMonth1 = String.valueOf(mMonth1);
			String sYear1 = String.valueOf(mYear1);
			String sDay2 = String.valueOf(mDay2);
			String sMonth2 = String.valueOf(mMonth2);
			String sYear2 = String.valueOf(mYear2);
			
//			AddProjectTask addProjectTask = new AddProjectTask(this);
//			addProjectTask.execute(sName, sDescription, sDay1, sMonth1, sYear1,
//					sDay2, sMonth2, sYear2, sEmail);
		}

	}
}
