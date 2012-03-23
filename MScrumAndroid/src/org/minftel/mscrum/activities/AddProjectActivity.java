package org.minftel.mscrum.activities;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.minftel.mscrum.tasks.AddProjectTask;
import org.minftel.mscrum.tasks.CloseSessionTask;
import org.minftel.mscrum.utils.ScrumConstants;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class AddProjectActivity extends Activity implements
		OnGesturePerformedListener {
	private GestureLibrary gestureLib;
	private TextView mDateDisplay1;
	private TextView mDateDisplay2;

	private EditText projectName;
	private EditText projectDescription;

	private ImageView mPickDate1;
	private ImageView mPickDate2;

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

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addproject);

		// Gesture detection
		GestureOverlayView gestureOverlayView = new GestureOverlayView(this);
		View inflate = getLayoutInflater().inflate(R.layout.addproject, null);
		gestureOverlayView.addView(inflate);
		gestureOverlayView.addOnGesturePerformedListener(this);
		// gestureOverlayView.setGestureColor(Color.TRANSPARENT);
		gestureOverlayView.setUncertainGestureColor(Color.TRANSPARENT);
		gestureLib = GestureLibraries.fromRawResource(this, R.raw.gestures);
		if (!gestureLib.load()) {
			Log.w(ScrumConstants.TAG, "Gesture not loaded!");
		}
		setContentView(gestureOverlayView);

		// capture our View elements
		mDateDisplay1 = (TextView) findViewById(R.id.addInitialDateProjectRes);
		mPickDate1 = (ImageView) findViewById(R.id.addProjectInitialCalendar);
		mDateDisplay2 = (TextView) findViewById(R.id.addEndDateProjectRes);
		mPickDate2 = (ImageView) findViewById(R.id.addProjectEndCalendar);
		projectName = (EditText) findViewById(R.id.nameAddProjectText);
		projectDescription = (EditText) findViewById(R.id.descriptionAddProjectText);

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
		DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
		Date date = new Date();
		date.setDate(mDay1);
		date.setMonth(mMonth1);
		date.setYear(mYear1);
		String fecha = df.format(date);
		mDateDisplay1.setText(fecha);
		date.setDate(mDay2);
		date.setMonth(mMonth2);
		date.setYear(mYear2);
		fecha = df.format(date);
		mDateDisplay2.setText(fecha);
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
		int envio = 3;
		int res1 = checkDate(mYear1, mMonth1, mDay1, mYear2, mMonth2, mDay2);
		int res2 = checkDate(actualYear, actualMonth, actualDay, mYear1,
				mMonth1, mDay1);
		String sName = projectName.getText().toString().trim();
		String sDescription = projectDescription.getText().toString().trim();
		if (res1 > 0 || res2 > 0) {
			Toast.makeText(this, R.string.wrong_dates, Toast.LENGTH_SHORT)
					.show();
			envio--;
		}
		if (sName.isEmpty()) {
			Toast.makeText(this, R.string.name_equired, Toast.LENGTH_SHORT)
					.show();
			envio--;
		}

		if (sDescription.isEmpty()) {
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

			String sEmail = getSharedPreferences(
					ScrumConstants.SHARED_PREFERENCES_FILE, MODE_PRIVATE)
					.getString("email", "");

			String varControl = "0";
			AddProjectTask addProjectTask = new AddProjectTask(this);
			addProjectTask.execute(varControl, sName, sDescription, sDay1,
					sMonth1, sYear1, sDay2, sMonth2, sYear2, sEmail);
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

	public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {
		ArrayList<Prediction> predictions = gestureLib.recognize(gesture);
		for (Prediction prediction : predictions) {
			if (prediction.score > 2.0) {
				if (prediction.name.equalsIgnoreCase("toRight")) {
					onBackPressed();
				}
				if (prediction.name.equalsIgnoreCase("logout")) {
					logOut();
				}
			}
		}
	}

	public void logOut() {
		Log.i(ScrumConstants.TAG, "Antes de CloseSessionTask");
		CloseSessionTask closeSessionTask = new CloseSessionTask(this);
		closeSessionTask.execute();
		Log.i(ScrumConstants.TAG, "Despues de CloseSessionTask");
	}
}
