package com.example.surface.rcmage.Activities;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.SQLException;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.surface.rcmage.Class.DB_Handler;



public class Titlepage extends AppCompatActivity {
    private ListView listview;
    private String[] currentItemList;
    private int[] drawableNames = {R.drawable.resistors_in_parallel,R.drawable.common_voltage_divider, R.drawable.pic2,R.drawable.op_amp_lm741 ,R.drawable.pic3, R.drawable.resistor_chart, R.drawable.backroundimage,R.drawable.pic6};
    private DB_Handler db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_titlepage);
        // Initialises and loads database up.
        db = new DB_Handler(this, "", null, 1);
        if (!db.isDbPresent()) {
            dbInitialLoad();
            Toast.makeText(this, "Database Loaded", Toast.LENGTH_SHORT).show();
        }

        listview = (ListView) findViewById(R.id.titleView);
        currentItemList = getResources().getStringArray(R.array.TitlePageItems);
        listview.setBackgroundColor(Color.parseColor("#3b698c"));
        myListAdapter adapter = new myListAdapter(getApplicationContext(), currentItemList, drawableNames);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case (0):
                        startActivity(new Intent(Titlepage.this, Resistor_parallel_calculator.class));
                        break;
                    case (1):
                        startActivity(new Intent(Titlepage.this, Common_Circuit_Titlepage.class));
                        break;
                    case (2):
                        startActivity(new Intent(Titlepage.this, MainActivity.class));
                        break;
                    case (3):
                        startActivity( new Intent(Titlepage.this, Active_Filters_Titlepage.class));
                        break;
                    case (4):
                        Intent intent = new Intent(Titlepage.this, newComponantSet.class);
                        startActivity(intent);
                        break;
                    case (5):
                        startActivity(new Intent(Titlepage.this, Resistor_Band_Viewer.class));
                        break;
                    case (6):
                        break;
                    case(7):
                        AlertDialog.Builder dialog = new AlertDialog.Builder(Titlepage.this);
                        dialog.setCancelable(false);
                        dialog.setTitle("Report To Admin");
                        dialog.setMessage("Type A Message To The Admin Team, Your Message Is Very Important To Us");
                        dialog.setPositiveButton("Go To Email", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String[] TO = {"creavervs@gmail.com"};

                                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                                emailIntent.setData(Uri.parse("mailto:"));
                                emailIntent.setType("text/plain");


                                emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);

                                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Fault Found");

                                try {
                                   // startActivityForResult(emailIntent,0);
                                    startActivityForResult(Intent.createChooser(emailIntent, "Choose an Email client :"),0);



                                // startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                                finish();


                            } catch (android.content.ActivityNotFoundException ex) {
                                    Toast.makeText(Titlepage.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
                                }

                                dialog.dismiss();
                            }
                            });
                        dialog.setNegativeButton("Back", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();
                            }
                        });
                        dialog.show();
                        break;
                }
            }
        });

    }
    public void dbInitialLoad() {
        AssetManager am = getAssets();

        try {
            db.LoadResistorTable(am);
            db.LoadCapacitorTable(am);
        } catch (SQLException e) {
            Toast.makeText(Titlepage.this, "Problem Loading Database First Time", Toast.LENGTH_SHORT).show();
        }

        //   db.list_all(textView);
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
            image.setImageResource(drawableNames[position]);

            description.setTextColor(Color.BLACK);
            textView.setTextSize(30);
            switch (position) {
                case 0:
                    textView.setTextColor(Color.WHITE);
                    description.setText(R.string.parallelResistors);

                    textView.setTextSize(25);
                    break;
                case 1:
                    textView.setTextColor(Color.WHITE);
                    description.setText(R.string.Common_Circuit_Titlepage_Description);

                    textView.setTextSize(25);
                    break;
                case 2:
                    textView.setTextColor(Color.WHITE);
                    description.setText(R.string.passiveFilterCalculator);
                    break;
                case 3:
                    textView.setTextColor(Color.WHITE);
                    description.setText(R.string.Active_Filters_Description);
                    break;
                case 4:
                    textView.setTextColor(Color.WHITE);
                    description.setText(R.string.newComponantSets);
                    break;
                case 5:
                    textView.setTextColor(Color.WHITE);
                    description.setText(R.string.bandViewer);
                    break;
                case 6:
                    textView.setTextColor(Color.WHITE);
                    description.setText(R.string.aboutUs);
                    break;

                case 7:
                    textView.setTextColor(Color.WHITE);
                    description.setText(R.string.reportAFault);
                    break;
            }

            textView.setTypeface(null, Typeface.BOLD);
            textView.setText(currentItemList[position]);

            return view;

        }
    } @Override   public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub if(requestCode == 0) {
        // You will get callback here when email activity is exited
        // perform you task here



    }
}

