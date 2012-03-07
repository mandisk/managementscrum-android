package org.minftel.mscrum.activities;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.minftel.mscrum.utils.TextAdapter;

public class ProjectActivity extends ListActivity {
    /** Called when the activity is first created. */

    @Override
    public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.project);

            Context ctx = getApplicationContext();
            Resources res = ctx.getResources();

            String[] options = res.getStringArray(R.array.title_names);

            setListAdapter(new TextAdapter(ctx, R.layout.list_item,options));

    }

    public void onListItemClick(ListView parent, View v, int position, long id) {
            Intent intent = null;
            // Show position clicked
            
              Toast.makeText(getApplicationContext(), "You have selected " +
              (position + 1) + "th item", Toast.LENGTH_SHORT).show();
             
         /*   switch (position) {
            case 0:
                    // Option --> EMERGENCY
                    intent = new Intent(MainActivity.this, EmergencyActivity.class);
                    startActivity(intent);
                    break;
            case 1:
                    // Option --> ASSISTANCE
                    intent = new Intent(MainActivity.this, AssistanceActivity.class);
                    startActivity(intent);
                    break;
            case 2:
                    // Option --> WALK
                    intent = new Intent(MainActivity.this, WalkMainActivity.class);
                    startActivity(intent);
                    break;
            case 3:
                    // Option --> CONSULTATION
                    intent = new Intent(MainActivity.this, ConsultActivity.class);
                    startActivity(intent);
                    break;
            case 4:
                    // Option --> CONFIGURATION
                    intent = new Intent(MainActivity.this, ConfigActivity.class);
                    startActivity(intent);
                    break;
            }*/

    }

}