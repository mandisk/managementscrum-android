package org.minftel.mscrum.activities;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ContactActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.usercontact);

		final TextView txtname = (TextView) findViewById(R.id.name);
		final TextView txtsurname = (TextView) findViewById(R.id.surname);
		final Button btCall = (Button) findViewById(R.id.btCall);
		final Button btMessage = (Button) findViewById(R.id.btSendMessage);

		// Get data from UserActivity
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			String name = extras.getString("name");
			String surname = extras.getString("surname");
			final String phone = extras.getString("phone");
			final String email = extras.getString("email");

			txtname.setText(name + " " + surname);
			txtsurname.setText("Teléfono: " + phone + " | " + "Email: " + email);

			// Action invoked when call button is pressed.
			btCall.setOnClickListener(new View.OnClickListener() {

				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(Intent.ACTION_CALL);
					intent.setData(Uri.parse("tel:"+phone));
					startActivity(intent);

				}
			});

			// Action invoked when send email button is pressed.
			btMessage.setOnClickListener(new View.OnClickListener() {

				public void onClick(View v) {
					// TODO Auto-generated method stub
					String asunto = "Asunto:";
					Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
					emailIntent.setType("plain/text");
					emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,new String[] {email});
					emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, asunto);
					startActivity(Intent.createChooser(emailIntent, "Envia email a:"));

				}
			});
		}

	}
}