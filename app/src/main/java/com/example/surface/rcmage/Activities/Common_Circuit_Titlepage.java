package com.example.surface.rcmage.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class Common_Circuit_Titlepage extends AppCompatActivity {
    private ListView listview;
    private String[] currentItemList;
    private int[] drawableNames = {R.drawable.common_voltage_divider, R.drawable.common_current_divider, R.drawable.common_low_pass_filter, R.drawable.common_high_pass_filter};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_titlepage);

        listview = (ListView) findViewById(R.id.titleView);
        currentItemList = getResources().getStringArray(R.array.Common_Circuits);
        listview.setBackgroundColor(Color.parseColor("#7FA8B0"));
        myListAdapter adapter = new myListAdapter(getApplicationContext(), currentItemList, drawableNames);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case (0):
                        startActivity(new Intent(Common_Circuit_Titlepage.this, Common_voltage_divider.class));
                        break;
                    case (1):
                        startActivity(new Intent(Common_Circuit_Titlepage.this,Common_current_divider.class));
                        break;
                    case (2):
                        Intent intent = new Intent(Common_Circuit_Titlepage.this, newComponantSet.class);
                        startActivity(intent);
                        break;
                    case (3):
                        startActivity(new Intent(Common_Circuit_Titlepage.this, Resistor_Band_Viewer.class));
                        break;



                }

            }

        });
    }

    private class myListAdapter extends BaseAdapter {
        Context context;
        String currentItemList[];
        int drawableNames[];
        LayoutInflater inflter;

        public myListAdapter(Context context, String[] currentItemList, int[] drawableNames) {
            this.context = context;
            this.currentItemList = currentItemList;
            this.drawableNames = drawableNames;
            this.inflter = (LayoutInflater.from(context));
        }


        @Override
        public int getCount() {
            return currentItemList.length;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }


        @Override
        public View getView(final int position, View view, ViewGroup parent) {

            view = inflter.inflate(R.layout.itemview_titlepage, parent, false);

            // Get the Layout Parameters for ListView Current Item View
            //ViewGroup.LayoutParams params = view.getLayoutParams();
            // Set the height of the Item View
            //params.height = 400;
            //view.setLayoutParams(params);
            //view.setBackgroundResource(drawableNames[position]);
            //ViewGroup.LayoutParams params = parent.getLayoutParams();

            //view.setLayoutParams(params);

            TextView textView = (TextView) view.findViewById(R.id.itemview_titlepage_Heading);
            TextView description = (TextView) view.findViewById(R.id.itemview_titlepage_Description);
            ImageView image = (ImageView) view.findViewById(R.id.itemview_titlepage_Image);
            image.setBackgroundColor(Color.WHITE);
            Display display = getWindowManager().getDefaultDisplay();


            image.setMinimumWidth(1400);
            image.setMaxWidth(1400);
            image.setMaxHeight(900);
            image.setMinimumHeight(900);
            image.setImageResource(drawableNames[position]);

            description.setTextColor(Color.BLACK);
            textView.setTextSize(30);
            switch (position) {
                case 0:
                    textView.setTextColor(Color.WHITE);
                    description.setText(R.string.Active_LPF_description);

                    textView.setTextSize(25);
                    break;
                case 1:
                    textView.setTextColor(Color.WHITE);
                    description.setText(R.string.Active_HPF_description);
                    break;
                case 2:
                    textView.setTextColor(Color.WHITE);
                    description.setText(R.string.Active_LPF_description);
                    break;
                case 3:
                    textView.setTextColor(Color.WHITE);
                    description.setText(R.string.Active_LPF_description);

                    break;
            }
            textView.setTextSize(25);
            textView.setTypeface(null, Typeface.BOLD);
            textView.setText(currentItemList[position]);

            return view;

        }
    }
}

