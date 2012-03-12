package org.minftel.mscrum.activities;

import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

public class AddProjectActivity extends Activity {
	private TextView mDateDisplay1;
	private TextView mPickDate1;
	private TextView mDateDisplay2;
	private TextView mPickDate2;

	private int pickDate = 0;
	private int mYear1;
	private int mMonth1;
	private int mDay1;

	private int mYear2;
	private int mMonth2;
	private int mDay2;

	static final int DATE_DIALOG_ID1 = 0;
	static final int DATE_DIALOG_ID2 = 1;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addproject);

		// capture our View elements
		mDateDisplay1 = (TextView) findViewById(R.id.addInitialDateProjectRes);
		mPickDate1 = (TextView) findViewById(R.id.addInitialDateProject);
		mDateDisplay2 = (TextView) findViewById(R.id.addEndDateProjectRes);
		mPickDate2 = (TextView) findViewById(R.id.addEndDateProject);
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
		mYear1 = c.get(Calendar.YEAR);
		mMonth1 = c.get(Calendar.MONTH);
		mDay1 = c.get(Calendar.DAY_OF_MONTH);
		mYear2 = c.get(Calendar.YEAR);
		mMonth2 = c.get(Calendar.MONTH);
		mDay2 = c.get(Calendar.DAY_OF_MONTH);

		// display the current date (this method is below)
		updateDisplay();
	}

	// updates the date in the TextView
	private void updateDisplay() {
		mDateDisplay1.setText(new StringBuilder()
				// Month is 0 based so add 1
				.append(mMonth1 + 1).append("-").append(mDay1).append("-")
				.append(mYear1).append(" "));
		mDateDisplay2.setText(new StringBuilder()
				// Month is 0 based so add 1
				.append(mMonth2 + 1).append("-").append(mDay2).append("-")
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

	public void checkValues(View view) {
		if (mYear2 < mYear1) {
			Toast.makeText(this, "end year < initial year", Toast.LENGTH_SHORT)
					.show();
		} else if (mMonth2 < mMonth1) {
			Toast.makeText(this, "end month < initial month",
					Toast.LENGTH_SHORT).show();
		} else if (mDay2 < mDay1) {
			Toast.makeText(this, "end day < initial day", Toast.LENGTH_SHORT)
					.show();
		} else {
			Toast.makeText(this, "all ok", Toast.LENGTH_SHORT).show();
		}
	}
}
