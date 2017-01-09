package com.example.surface.rcmage.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.surface.rcmage.Class.DB_Handler;
import com.example.surface.rcmage.Class.PaintResistor;
import com.example.surface.rcmage.Class.RCRecord;
import com.example.surface.rcmage.Class.Symbol;
import com.example.surface.rcmage.Class.capRecord;
import com.example.surface.rcmage.Class.itemViewRecord;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private ListView listView;
    private EditText editText;
    private Canvas canvas1;
    private Spinner spinnerRes;
    private Spinner spinnerCap;
    private ArrayAdapter<CharSequence> adapterRes;
    private ArrayAdapter<CharSequence> adapterCap;
    DB_Handler db;
    private Button button;

    public static List<RCRecord> currentResistorList;
    public static List<capRecord> currentCapacitorList;
    public static List<itemViewRecord> currentItemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.display);

        // backround painter
        setFunkyBackround();
        button = (Button) findViewById(R.id.button_freq_calculator);
        button.setBackgroundColor(Color.parseColor("#FFFFFF"));
        editText = (EditText) findViewById(R.id.userInput);
        editText.setTextColor(Color.parseColor("#FFFFFF"));
        editText.setHint(R.string.hint);
        editText.setHintTextColor(Color.parseColor("#FFFFFF"));
        editText.setImeOptions(EditorInfo.IME_ACTION_DONE);
        editText.setOnEditorActionListener(
                new EditText.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_DONE) {
                            if (editText.getText().toString().equals("")) {
                                return false;
                            }
                            int freq = Integer.parseInt(editText.getText().toString());
                            addToItemList(freq);
                            populateListview();
                            return false;
                        }
                        // Return true if you have consumed the action, else false.
                        return true;
                    }
                });
        // Initialises and loads database up.
        db = new DB_Handler(this, "", null, 1);


        spinnerRes = (Spinner) findViewById(R.id.spinnerRes);
        spinnerRes.setBackgroundColor(Color.parseColor("#FFFFFF"));
        currentResistorList = new ArrayList<>();
        List<String> ResistoArray = db.getSetFromDataBase("Resistor");
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item_main_activity, ResistoArray);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item_main_activity);
        spinnerRes.setAdapter(spinnerArrayAdapter);
        spinnerRes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // just say what item you landed on and what DATABASE set we are currently using
                //Toast.makeText(getBaseContext(), parent.getItemAtPosition(position) + " selected", Toast.LENGTH_LONG).show();
                // need to set current Data base
                currentResistorList = db.SetLocalResistor(parent.getItemAtPosition(position).toString());

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Default problem as nothing is selected // should never get here.
            }
        });

        spinnerCap = (Spinner) findViewById(R.id.spinnerCap);
        spinnerCap.setBackgroundColor(Color.parseColor("#FFFFFF"));
        currentCapacitorList = new ArrayList<>();
        List<String> CapacitorArray = db.getSetFromDataBase("Capacitor");
        ArrayAdapter<String> spinnerArrayAdapter2 = new ArrayAdapter<String>(
                this, R.layout.spinner_item_main_activity, CapacitorArray);
        spinnerArrayAdapter2.setDropDownViewResource(R.layout.spinner_item_main_activity);
        spinnerCap.setAdapter(spinnerArrayAdapter2);
        spinnerCap.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // just say what item you landed on and what Table set we are currently using
                // Toast.makeText(getBaseContext(), parent.getItemAtPosition(position) + " selected", Toast.LENGTH_LONG).show();
                // need to set current Data base
                currentCapacitorList = db.SetLocalCapacitor(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Default problem as nothing is selected // should never get here.
            }
        });

    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        // Spinner spinnerMenu = (Spinner)( MenuItemCompat.getActionView(item));

        //ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.menuItems, android.R.layout.simple_spinner_item);
        // adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //spinnerMenu.setAdapter(adapter);

        // set window theme i think for pop up backround i.e change colour or whatever
        // spinnerMenu.setPopupBackgroundResource(R.drawable.ic_action_name);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.item1:
                // make intent to beam these lists to different place.
                Intent intent = new Intent(MainActivity.this, newComponantSet.class);
                startActivity(intent);

                return true;
            case R.id.item2:
                startActivity(new Intent(MainActivity.this, Resistor_Band_Viewer.class));
                return true;
            case R.id.item3:
                startActivity(new Intent(MainActivity.this, Titlepage.class));
                return true;
            case R.id.item4:
                startActivity(new Intent(MainActivity.this, Resistor_parallel_calculator.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void populateListview() {
        ArrayAdapter<itemViewRecord> adapter = new myListAdapter();
        listView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        listView.setAdapter(adapter);

    }

    private class myListAdapter extends ArrayAdapter<itemViewRecord> {

        public myListAdapter() {
            super(MainActivity.this, R.layout.itemview_layout, currentItemList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View ItemView = convertView;
            if (ItemView == null) {
                ItemView = getLayoutInflater().inflate(R.layout.itemview_layout, parent, false);
            }
//                view = inflter.inflate(R.layout.itemview_titlepage,parent,false);
//
//                // Get the Layout Parameters for ListView Current Item View
            //ViewGroup.LayoutParams params = convertView.getLayoutParams();
            // Set the height of the Item View
            //params.height = 400;
            //convertView.setLayoutParams(params);
            //parent.setBackgroundResource(drawableNames[position]);
            final itemViewRecord currentItem = currentItemList.get(position);

            // fill the view with stuff from the R.id.item_your name here
            ImageView imageview = (ImageView) ItemView.findViewById(R.id.item_imageComponant);
            final SeekBar seekbar = (SeekBar) ItemView.findViewById(R.id.seekBar4);
            TextView textFreq = (TextView) ItemView.findViewById(R.id.item_textFreq);

            TextView textCompo = (TextView) ItemView.findViewById(R.id.item_textComponants);
            TextView textPercent = (TextView) ItemView.findViewById(R.id.item_textTolerance);
            TextView textCap = (TextView) ItemView.findViewById(R.id.item_cap);
            textCap.setTextSize(18);
            textCompo.setTextSize(18);
            textPercent.setTextSize(26);

            seekbar.setProgress(currentItem.getSliderPosition());
            String band1 = currentItem.getBand1();
            String band2 = currentItem.getBand2();
            String band3 = currentItem.getBand3();
            PaintResistor pr1 = new PaintResistor(band1, band2, band3);
            Bitmap bitmap = Bitmap.createBitmap(imageview.getMaxWidth(), imageview.getMaxHeight(), Bitmap.Config.RGB_565);
            bitmap.eraseColor(Color.parseColor("#F9F9F9"));
            Canvas c1 = new Canvas(bitmap);

            pr1.drawResistorBody(c1, imageview.getMaxWidth(), imageview.getMaxHeight());
            imageview.setImageBitmap(bitmap);
            // TextView textFreq = (TextView) ItemView.findViewById(R.id.item_textFreq);
            textFreq.setTextSize(30);
            textFreq.setText(currentItem.getFrequency().toString().format("%.2f", currentItem.getFrequency()) + " " + "\u3390");
            Symbol s1 = new Symbol("Capacitor");
            String cap1 = s1.numberToSymbol(currentItem.getCapacitor());
            textCap.setText("Capacitor: " + cap1);
            Symbol s2 = new Symbol("Resistor");
            String res1 = s2.numberToSymbol(currentItem.getResistor());

            textCompo.setText("Resistor: " + res1 + " " + "\u2126");

            //TextView textPercent = (TextView) ItemView.findViewById(R.id.item_textTolerance);

            if (currentItem.getError() < 5) {
                textPercent.setTextColor(Color.GREEN);
                textPercent.setText(currentItem.getError().toString().format("%.2f", currentItem.getError()) + "%");

            } else {
                textPercent.setTextColor(Color.RED);
                textPercent.setText(currentItem.getError().toString().format("%.2f", currentItem.getError()) + "%");

            }
            seekbar.setOnSeekBarChangeListener(new mySeekbarListener(currentItem, textFreq, textPercent, textCap));
            return ItemView;

        }
    }

    //.format("%.2f", d), for 2dp formatting to use later but needs calling on a string
    private void addToItemList(Integer desired) {
        currentItemList = new ArrayList<>();
        for (capRecord c : currentCapacitorList) {
            for (RCRecord r : currentResistorList) {
                Double newFrequency = 1.0 / (2.0 * Math.PI * (c.getValue()) * (r.getValue()));
                Double middle = newFrequency - desired;
                Double error = Math.abs((middle / newFrequency)) * 100;
                if (error < 50) {
                    itemViewRecord ir = new itemViewRecord(newFrequency, r.getValue(), c.getValue(), error, r.getBand1(), r.getBand2(), r.getBand3(), 5, desired);
                    currentItemList.add(ir);
                }
            }

        }
        Collections.sort(currentItemList, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                itemViewRecord p1 = (itemViewRecord) o1;
                itemViewRecord p2 = (itemViewRecord) o2;
                return p1.getError().compareTo(p2.getError());
            }
        });
        if (currentItemList.isEmpty()) {
            Toast.makeText(this, "Can't Make That Frequency With Current Sets", Toast.LENGTH_SHORT).show();
        }
    }

    public class mySeekbarListener implements SeekBar.OnSeekBarChangeListener {
        TextView textfreq;
        TextView Error;
        TextView newCap;
        itemViewRecord currentItem;

        public mySeekbarListener(itemViewRecord item, TextView textfreq, TextView Error, TextView newCap) {
            this.textfreq = textfreq;
            this.Error = Error;
            this.newCap = newCap;
            this.currentItem = item;

        }

        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

            Double newCapValue;
            Symbol s1 = new Symbol("Capacitor");
            Double value1 = currentItem.getCapacitor();
            newCapValue = s1.changeInValue(value1);
            Double n1;
            Double n2;
            Double n3;
            Double n4;
            Double n5;
            Double n6;
            Double n7;
            Double n8;
            Double n9;
            Double n10;
            Double n11;
            switch (seekBar.getProgress()) {

                case (0):

                    n1 = currentItem.getCapacitor() - (newCapValue * (5));
                    setItemInView(currentItem, n1, textfreq, newCap, Error);

                    //Toast.makeText(MainActivity.this,"new FrequenEcy value: "+newFrequency , Toast.LENGTH_SHORT).show();
                    break;
                case (1):
                    n2 = currentItem.getCapacitor() - (newCapValue * (4));
                    setItemInView(currentItem, n2, textfreq, newCap, Error);
                    //Toast.makeText(MainActivity.this, "new Capacitor value " + n2, Toast.LENGTH_SHORT).show();
                    break;
                case (2):
                    n3 = currentItem.getCapacitor() - (newCapValue * (3));
                    setItemInView(currentItem, n3, textfreq, newCap, Error);
                    //Toast.makeText(MainActivity.this, "new Capacitor value " + n3, Toast.LENGTH_SHORT).show();
                    break;
                case (3):
                    n4 = currentItem.getCapacitor() - (newCapValue * (2));
                    setItemInView(currentItem, n4, textfreq, newCap, Error);
                    // Toast.makeText(MainActivity.this, "new Capacitor value " + n4, Toast.LENGTH_SHORT).show();
                    break;
                case (4):
                    n5 = currentItem.getCapacitor() - (newCapValue * (1));
                    setItemInView(currentItem, n5, textfreq, newCap, Error);
                    //Toast.makeText(MainActivity.this, "new Capacitor value " + n5, Toast.LENGTH_SHORT).show();
                    break;
                case (5):
                    n6 = currentItem.getCapacitor();
                    setItemInView(currentItem, n6, textfreq, newCap, Error);
                    //Toast.makeText(MainActivity.this, "new Capacitor value " + n6, Toast.LENGTH_SHORT).show();
                    break;
                case (6):
                    n7 = currentItem.getCapacitor() + newCapValue * (1);
                    setItemInView(currentItem, n7, textfreq, newCap, Error);
                    // Toast.makeText(MainActivity.this, "new Capacitor value " + n7, Toast.LENGTH_SHORT).show();
                    break;
                case (7):
                    n8 = currentItem.getCapacitor() + (newCapValue * (2));
                    setItemInView(currentItem, n8, textfreq, newCap, Error);
                    // Toast.makeText(MainActivity.this, "new Capacitor value " + n8, Toast.LENGTH_SHORT).show();
                    break;
                case (8):
                    n9 = currentItem.getCapacitor() + (newCapValue * (3));
                    setItemInView(currentItem, n9, textfreq, newCap, Error);
                    // Toast.makeText(MainActivity.this, "new Capacitor value " + n9, Toast.LENGTH_SHORT).show();
                    break;
                case (9):
                    n10 = currentItem.getCapacitor() + (newCapValue * (4));
                    setItemInView(currentItem, n10, textfreq, newCap, Error);
                    // Toast.makeText(MainActivity.this, "new Capacitor value " + n10, Toast.LENGTH_SHORT).show();
                    break;
                case (10):
                    n11 = currentItem.getCapacitor() + (newCapValue * (5));
                    setItemInView(currentItem, n11, textfreq, newCap, Error);
                    // Toast.makeText(MainActivity.this, "new Capacitor value " + n11, Toast.LENGTH_SHORT).show();
                    break;
            }
            currentItem.setSliderPosition(seekBar.getProgress());


        }

        public void setItemInView(itemViewRecord currentItem, Double n1, TextView textfreq, TextView newCap, TextView Error) {
            Symbol s1 = new Symbol("Capacitor");
            Double newFrequency = 1.0 / (2.0 * Math.PI * (n1) * (currentItem.getResistor()));
            Double middle = newFrequency - currentItem.getDesiredFrequency();
            Double error = Math.abs((middle / newFrequency)) * 100;
            newCap.setTextSize(18);
            newCap.setText("Capacitor: " + s1.numberToSymbol(n1));
            setErrorTextview(Error, error);
            textfreq.setTextSize(30);
            textfreq.setText(newFrequency.toString().format("%.2f", newFrequency) + " " + "\u3390");


        }

        public void setErrorTextview(TextView textPercent, Double errorValue) {
            if (errorValue < 5) {
                textPercent.setTextColor(Color.GREEN);
                textPercent.setText(errorValue.toString().format("%.2f", errorValue) + "%");

            } else {
                textPercent.setTextColor(Color.RED);
                textPercent.setText(errorValue.toString().format("%.2f", errorValue) + "%");
            }


        }
    }

    public void setFunkyBackround() {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int yScale = displaymetrics.heightPixels;
        int xScale = displaymetrics.widthPixels;
        Bitmap bitmap = Bitmap.createBitmap(xScale, yScale, Bitmap.Config.RGB_565);
        canvas1 = new Canvas(bitmap);
        bitmap.eraseColor(Color.parseColor("#001001"));
        drawCircle(.5f * xScale, .5f * yScale, (.5f * xScale) + .5f * yScale);
        View view = findViewById(R.id.activity_main);
        Drawable d = new BitmapDrawable(getResources(), bitmap);
        view.setBackground(d);
    }

    public void drawCircle(float x, float y, float radius) {
        Paint p1 = new Paint(Paint.ANTI_ALIAS_FLAG);
        p1.setColor(Color.BLUE);
        p1.setStyle(Paint.Style.STROKE);
        canvas1.drawCircle(x, y, radius, p1);
        if (radius > 80) {
            drawCircle(x + radius / 2, y, radius / 2);
            drawCircle(x - radius / 2, y, radius / 2);
            drawCircle(x, y + radius / 2, radius / 2);
            drawCircle(x, y - radius / 2, radius / 2);
        }

    }

    public void click(View v) {
        if (editText.getText().toString().equals("")) {
            return;
        }
        int freq = Integer.parseInt(editText.getText().toString());
        addToItemList(freq);
        populateListview();
    }
}
