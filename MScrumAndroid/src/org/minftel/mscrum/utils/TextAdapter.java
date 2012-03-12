package org.minftel.mscrum.utils;

import org.minftel.mscrum.activities.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class TextAdapter extends ArrayAdapter<String>  {
        
private LayoutInflater mInflater;
        
        private String[] mStrings;
        
        private String[] mSubStrings;
        
        private int mViewResourceId;
        
        public TextAdapter(Context ctx, int viewResourceId,
                        String[] strings, String[] substrings) {
                super(ctx, viewResourceId, strings);
                
                mInflater = (LayoutInflater)ctx.getSystemService(
                                Context.LAYOUT_INFLATER_SERVICE);
                
                mStrings = strings;
                mSubStrings = substrings;                
                
                mViewResourceId = viewResourceId;
        }

        @Override
        public int getCount() {
                return mStrings.length;
        }

        @Override
        public String getItem(int position) {
                return mStrings[position];
        }

        @Override
        public long getItemId(int position) {
                return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
                convertView = mInflater.inflate(mViewResourceId, null);
                
                TextView subtv = (TextView)convertView.findViewById(R.id.option_text_scrum);
                subtv.setText(mSubStrings[position]);

                TextView tv = (TextView)convertView.findViewById(R.id.option_text);
                tv.setText(mStrings[position]);
                
                return convertView;
        }

}
