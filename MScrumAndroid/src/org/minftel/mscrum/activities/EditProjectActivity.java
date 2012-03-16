package org.minftel.mscrum.activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.minftel.mscrum.model.TaskDetail;
import org.minftel.mscrum.model.UserDetail;
import org.minftel.mscrum.tasks.EditProjectAskTask;
import org.minftel.mscrum.tasks.EditProjectSendTask;
import org.minftel.mscrum.utils.JSONConverter;
import org.minftel.mscrum.utils.ScrumConstants;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;

public class EditProjectActivity extends android.app.ExpandableListActivity {

	private List<UserDetail> userListNotinProject;
	private List<UserDetail> userList;
	private List<UserDetail> userListAll;
	private List<Integer> chekboxEnabled;
	private CheckBox chekbox;
	private ArrayList secList;

	@SuppressWarnings("unchecked")
	public void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.editproject);
			
			chekbox = (CheckBox) findViewById(R.id.check1);
			// Lista de usuarios
			String json2 = getIntent().getExtras().getString(
					"usersnotinproject");
			String json = getIntent().getExtras().getString("user");
			Log.e(ScrumConstants.TAG, " "+json);
			Log.e(ScrumConstants.TAG, " "+json2);
			userList = JSONConverter.fromJSONtoUserList(json);
			Log.e(ScrumConstants.TAG, "--------2-");
			
			Log.e(ScrumConstants.TAG, "--------3-");
			userListNotinProject = JSONConverter.fromJSONtoUserList(json2);
			Log.e(ScrumConstants.TAG, "--------4-");
		
			// Expandable list
			SimpleExpandableListAdapter expListAdapter = new SimpleExpandableListAdapter(
					this, createGroupList(), // Creating group List.
					R.layout.group_row, // Group item layout XML.
					new String[] { "Group Item" }, // the key of group item.
					new int[] { R.id.row_name }, // ID of each group item.-Data
													// under the key goes into
													// this TextView.
					createChildList(), // childData describes second-level
										// entries.
					R.layout.child_row, // Layout for sub-level entries(second
										// level).
					new String[] { "Sub Item" }, // Keys in childData maps to
													// display.
					new int[] { R.id.grp_child } // Data under the keys above go
													// into these TextViews.
			);
			setListAdapter(expListAdapter); // setting the adapter in the list.

		} catch (Exception e) {
			System.out.println("Errrr +++ " + e.getMessage());
		}
	}

	/* Creating the Hashmap for the row */
	@SuppressWarnings("unchecked")
	private List createGroupList() {
		ArrayList result = new ArrayList();

		HashMap m = new HashMap();
		m.put("Group Item", "Usuarios"); // the key and it's value.
		result.add(m);

		return (List) result;
	}

	/* creatin the HashMap for the children */
	@SuppressWarnings("unchecked")
	private List createChildList() {

		ArrayList result = new ArrayList();
		// /* each group need each HashMap-Here for each group we have 3
		// subgroups */
		secList = new ArrayList();

		for (UserDetail user : userList) {

			Log.e(ScrumConstants.TAG, " " + user.getName());
			HashMap child = new HashMap();
			child.put("Sub Item", user.getName());
			chekbox.setChecked(true);
			secList.add(child);
		}
		
		for (UserDetail user : userListNotinProject) {

			HashMap child = new HashMap();
			child.put("Sub Item", user.getName());
			secList.add(child);
		}

		userListAll.addAll(userList);
		userListAll.addAll(userListNotinProject);

		result.add(secList);
		return result;
	}

	public void onContentChanged() {
		System.out.println("onContentChanged");
		super.onContentChanged();
	}

	/* This function is called on each child click */
	public boolean onChildClick(ExpandableListView parent, View v,
			int groupPosition, int childPosition, long id) {
		CheckBox cb = (CheckBox) findViewById(R.id.check1);

		if (cb != null)
			cb.toggle();

		if (cb.isEnabled()) {
			chekboxEnabled.add(childPosition);
		}
		return true;
	}

	/* This function is called on expansion of the group */
	public void onGroupExpand(int groupPosition) {
		try {
			System.out.println("Group exapanding Listener => groupPosition = "
					+ groupPosition);
		} catch (Exception e) {
			System.out.println(" groupPosition Errrr +++ " + e.getMessage());
		}
	}

	public void save(View view) {
		String[] idUsers = new String[chekboxEnabled.size()];

		int i = 0;
		for (Integer position : chekboxEnabled) {
			idUsers[i] = String.valueOf(userListAll.get(position).getId());
			i++;
		}

		EditProjectSendTask editprojectTask = new EditProjectSendTask(this);
		editprojectTask.execute(idUsers);

	}

}