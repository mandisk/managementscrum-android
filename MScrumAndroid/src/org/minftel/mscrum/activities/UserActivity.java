package org.minftel.mscrum.activities;

import java.util.List;

import org.json.JSONException;
import org.minftel.mscrum.model.ProjectDetail;
import org.minftel.mscrum.model.UserDetail;
import org.minftel.mscrum.tasks.ProjectsTask;
import org.minftel.mscrum.utils.JSONConverter;
import org.minftel.mscrum.utils.ScrumConstants;
import org.minftel.mscrum.utils.TextAdapter;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

public class UserActivity extends ListActivity {
	
	private List<UserDetail> userList;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user);

		// Get User List
		String json = getIntent().getExtras().getString("users");
		
		try {
			userList = JSONConverter.fromJSONtoUserList(json);
		} catch (JSONException e) {
			Log.e(ScrumConstants.TAG, "JSONException: " + e.getMessage());
		}

		int size = userList.size();
		
		String[] userNames = new String[size];
		String[] emailUser = new String[size];

		for (int i = 0; i < size; i++) {
			UserDetail user = userList.get(i);
			userNames[i] = getString(R.string.user) + user.getName();
			emailUser[i] = getString(R.string.emailField) + user.getEmail();
		}

		// Load data in ListAdapter
		setListAdapter(new TextAdapter(this, R.layout.list_item, userNames,
				emailUser));

	}
	
	public void onListItemClick(ListView parent, View v, int position, long id) {

		//Get selected user
		UserDetail selectedUser = this.userList.get(position);
		Toast.makeText(
				this,
				"User selected: " + selectedUser.getName() + " [ Email: "
						+ selectedUser.getEmail() + " ]",
				Toast.LENGTH_SHORT).show();


	}

}