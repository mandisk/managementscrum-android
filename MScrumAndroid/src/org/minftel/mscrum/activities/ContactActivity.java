package org.minftel.mscrum.activities;

import java.io.InputStream;
import java.util.ArrayList;

import org.minftel.mscrum.tasks.CloseSessionTask;
import org.minftel.mscrum.utils.ScrumConstants;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.gesture.GestureOverlayView.OnGesturePerformedListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.PhoneLookup;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ContactActivity extends Activity implements
		OnGesturePerformedListener {
	private GestureLibrary gestureLib;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.usercontact);
		// Detección de gesto
		GestureOverlayView gestureOverlayView = new GestureOverlayView(this);
		View inflate = getLayoutInflater().inflate(R.layout.usercontact, null);
		gestureOverlayView.addView(inflate);
		gestureOverlayView.addOnGesturePerformedListener(this);
		// gestureOverlayView.setGestureColor(Color.TRANSPARENT);
		gestureOverlayView.setUncertainGestureColor(Color.TRANSPARENT);
		gestureLib = GestureLibraries.fromRawResource(this, R.raw.gestures);
		if (!gestureLib.load()) {
			// finish();
			Log.w(ScrumConstants.TAG, "Gesture not loaded!");
		}
		setContentView(gestureOverlayView);
		
		final TextView txtname = (TextView) findViewById(R.id.name);
		final ImageView btCall = (ImageView) findViewById(R.id.userContactCall);
		final ImageView btMessage = (ImageView) findViewById(R.id.userContactEmail);
		final TextView txtphone = (TextView) findViewById(R.id.phone);
		final TextView txtemail = (TextView) findViewById(R.id.email);
		ImageView photo = (ImageView) findViewById(R.id.imageContact);

		// Get data from UserActivity
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			String name = extras.getString("name");
			String surname = extras.getString("surname");
			final String phone = extras.getString("phone");
			final String email = extras.getString("email");

			txtname.setText(name + " " + surname);
			txtphone.setText(phone);
			txtemail.setText(email);
			//
			// Uri contactUri =
			// Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI,
			// Uri.encode(phone));
			// Uri photoUri = Uri.withAppendedPath(contactUri,
			// ContactsContract.CommonDataKinds.Photo.PHOTO);
			//
			photo.setImageBitmap(getFacebookPhoto(phone));

			// Action invoked when call button is pressed.
			btCall.setOnClickListener(new View.OnClickListener() {

				public void onClick(View v) {
					Intent intent = new Intent(Intent.ACTION_CALL);
					intent.setData(Uri.parse("tel:" + phone));
					startActivity(intent);

				}
			});

			// Action invoked when send email button is pressed.
			btMessage.setOnClickListener(new View.OnClickListener() {

				public void onClick(View v) {
					String asunto = "Asunto:";
					Intent emailIntent = new Intent(
							android.content.Intent.ACTION_SEND);
					emailIntent.setType("plain/text");
					emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
							new String[] { email });
					emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
							asunto);
					startActivity(Intent.createChooser(emailIntent,
							"Envia email a:"));

				}
			});
		}

	}

	public Bitmap getFacebookPhoto(String phoneNumber) {
		Uri phoneUri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI,
				Uri.encode(phoneNumber));
		Uri photoUri = null;
		ContentResolver cr = this.getContentResolver();
		Cursor contact = cr.query(phoneUri,
				new String[] { ContactsContract.Contacts._ID }, null, null,
				null);

		if (contact.moveToFirst()) {
			long userId = contact.getLong(contact
					.getColumnIndex(ContactsContract.Contacts._ID));
			photoUri = ContentUris.withAppendedId(
					ContactsContract.Contacts.CONTENT_URI, userId);

		} else {
			Bitmap defaultPhoto = BitmapFactory.decodeResource(getResources(),
					R.drawable.android);
			return defaultPhoto;
		}
		if (photoUri != null) {
			InputStream input = ContactsContract.Contacts
					.openContactPhotoInputStream(cr, photoUri);
			if (input != null) {
				return BitmapFactory.decodeStream(input);
			}
		} else {
			Bitmap defaultPhoto = BitmapFactory.decodeResource(getResources(),
					R.drawable.android);
			return defaultPhoto;
		}
		Bitmap defaultPhoto = BitmapFactory.decodeResource(getResources(),
				R.drawable.android);
		return defaultPhoto;
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