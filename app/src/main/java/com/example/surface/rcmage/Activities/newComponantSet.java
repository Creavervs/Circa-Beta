package com.example.surface.rcmage.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.surface.rcmage.Class.DB_Handler;
import com.example.surface.rcmage.Class.PaintResistor;
import com.example.surface.rcmage.Class.RCRecord;
import com.example.surface.rcmage.Class.Symbol;
import com.example.surface.rcmage.Class.capRecord;
import com.example.surface.rcmage.Class.itemNewComp;

import java.util.ArrayList;
import java.util.List;


public class newComponantSet extends AppCompatActivity {
    private Button buttonNew;
    private Button buttonLoad;
    private Button buttonSave;
    private Button buttonDelete;
    private ListView listview;
    private String currentLoadedSet;
    private DB_Handler db;
    private List<Double> currentValueList;
    private List<RCRecord> currentResistorList;
    private List<capRecord> currentCapacitorList;
    private List<itemNewComp> currentItemList;
    private List<String> nameStringList;
    private List<String> nameCapList;
    private List<itemNewComp> temp;
    private Canvas canvas1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_componant_set);

        buttonNew = (Button) findViewById(R.id.button1New);
        buttonLoad = (Button) findViewById(R.id.button2Load);
        buttonSave = (Button) findViewById(R.id.button3Save);
        buttonDelete = (Button)findViewById(R.id.button_delete);

        buttonLoad.setBackgroundColor(Color.LTGRAY);
        buttonNew.setBackgroundColor(Color.LTGRAY);
        buttonSave.setBackgroundColor(Color.LTGRAY);
        buttonDelete.setBackgroundColor(Color.LTGRAY);

        buttonLoad.setTextSize(10);
        buttonNew.setTextSize(10);
        buttonSave.setTextSize(10);
        buttonDelete.setTextSize(10);

        db = new DB_Handler(this, "", null, 1);
        listview = (ListView) findViewById(R.id.listview);
        setFunkyBackround();

    }

    public void btn_click(View view) {
        switch (view.getId()) {
            case R.id.button1New:

                AlertDialog.Builder dialog = new AlertDialog.Builder(newComponantSet.this);
                dialog.setTitle("Set Creator Wizard");
                dialog.setCancelable(false);
                final EditText new_firstname = new EditText(this);
                new_firstname.setHint("    Your New Name Here");
                dialog.setMessage("Enter your Sets Name then choose whether you want to use Capacitors or Resistors");
                new_firstname.setImeOptions(EditorInfo.IME_ACTION_DONE);
                new_firstname.setSingleLine(true);
                new_firstname.setOnEditorActionListener(new TextView.OnEditorActionListener() {

                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        return false;
                    }
                });

                dialog.setView(new_firstname);
                currentValueList = new ArrayList<>();

                dialog.setPositiveButton("Capacitor", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        temp = new ArrayList<>();
                        currentItemList = db.getitemNewComp("Capacitor");
                        if (db.contains("Capacitor", new_firstname.getText().toString())) {
                            Toast.makeText(newComponantSet.this, "Entry already Exists", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        } else {
                            Toast.makeText(newComponantSet.this, "Capacitor Set created ", Toast.LENGTH_SHORT).show();
                            currentLoadedSet = new_firstname.getText().toString();
                            populateListview("Capacitor", currentLoadedSet);
                            dialog.dismiss();

                        }

                    }
                });

                dialog.setNegativeButton("Resistor", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        temp = new ArrayList<>();
                        currentItemList = db.getitemNewComp("Resistor");
                        if (db.contains("Resistor", new_firstname.getText().toString())) {
                            Toast.makeText(newComponantSet.this, "Entry already Exists", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        } else {
                            Toast.makeText(newComponantSet.this, "Resistor Set created ", Toast.LENGTH_SHORT).show();
                            currentLoadedSet = new_firstname.getText().toString();
                            populateListview("Resistor", currentLoadedSet);
                            dialog.dismiss();
                        }


                    }
                });
                dialog.setNeutralButton("Back", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.show();

                break;


            case R.id.button2Load:

                AlertDialog.Builder dialog2 = new AlertDialog.Builder(newComponantSet.this);
                dialog2.setCancelable(false);
                dialog2.setTitle("Load Set From Database");
               // Sets up total list of all Resistors and Capacitors and adds a letter at the end to let us know its a cap or Res
                nameStringList = db.getSetFromDataBase("Resistor");
                for(int i = 0;i<nameStringList.size(); ++i){
                    String s= nameStringList.get(i);
                    nameStringList.remove(i);
                    nameStringList.add(i,s + " (R)");
                }
                nameCapList= db.getSetFromDataBase("Capacitor");
                for(int i = 0;i<nameCapList.size(); ++i){
                    String s= nameCapList.get(i);
                    nameCapList.remove(i);
                    nameCapList.add(i,s + " (C)");
                }
                nameStringList.addAll(nameCapList);

                final String[] charList = nameStringList.toArray(new String[nameStringList.size()]);
                dialog2.setItems(charList, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // the user clicked on a String
                        // call contains on both sets to find out which table we dealing with
                        // We then check them against the 24 series resistors and check them off as selected.
                        String item = charList[which].replace(" (C)","").replace(" (R)","");
                        currentLoadedSet = item;
                        currentValueList = new ArrayList<>();
                        if (db.getSetFromDataBase("Resistor").contains(item)) {
                            currentItemList = db.getitemNewComp("Resistor");// loads up item list all E12 all clicked no
                            temp = new ArrayList<>();

                            currentValueList = db.SetLocalValueArrayList("Resistor", currentLoadedSet);
                            Toast.makeText(newComponantSet.this, " size of current item list" + currentItemList.size() + " size of current Value list" + currentValueList.size(), Toast.LENGTH_SHORT).show();
                            for (itemNewComp i1 : currentItemList) {
                                for (double s1 : currentValueList) {
                                    if (s1 == i1.getResistor()) {
                                        i1.setSelectedTrue();
                                        temp.add(i1);
                                    }

                                }
                            }
                            populateListview("Resistor", item);
                            dialog.dismiss();

                        } else if (db.getSetFromDataBase("Capacitor").contains(item)) {
                            temp = new ArrayList<>();
                            currentValueList = db.SetLocalValueArrayList("Capacitor", item);
                            currentItemList = db.getitemNewComp("Capacitor");
                            Toast.makeText(newComponantSet.this, " size of current item list" + currentItemList.size() + " size of current Value list" + currentValueList.size(), Toast.LENGTH_SHORT).show();
                            for (itemNewComp i1 : currentItemList) {
                                for (double s1 : currentValueList) {
                                    if (s1 == (i1.getCapacitor())) {
                                        i1.setSelected();
                                        temp.add(i1);
                                    }
                                }

                            }
                            populateListview("Capacitor", item);
                            dialog.dismiss();

                        } else {
                            Toast.makeText(newComponantSet.this, item, Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                    }
                });

                dialog2.show();
                break;
            case R.id.button3Save:

                if (temp.size() == 0) {
                    Toast.makeText(this, "Can't Save an empty set", Toast.LENGTH_SHORT).show();
                    break;
                }
                for (itemNewComp i1 : temp) {
                    if (i1.getResistor() != 0.0) {
                        db.loadDbFromItemNewCompResistor(currentLoadedSet, temp);
                        Toast.makeText(this, "Saved " + temp.size() + " Resistors to " + currentLoadedSet, Toast.LENGTH_SHORT).show();
                        // Toast.makeText(this,"got ere", Toast.LENGTH_SHORT).show();
                        break;
                    } else if (i1.getCapacitor() != 0.0) {
                        db.loadDbFromItemNewCompCapacitor(currentLoadedSet, temp);
                        Toast.makeText(this, "Saved " + temp.size() + " Capacitors to " + currentLoadedSet, Toast.LENGTH_SHORT).show();
                        break;
                    } else {
                        Toast.makeText(this, "Problem Saving", Toast.LENGTH_SHORT).show();
                    }
                }


                break;
            case R.id.button_delete:
                final AlertDialog.Builder dialog3 = new AlertDialog.Builder(newComponantSet.this);
                dialog3.setCancelable(false);
                dialog3.setTitle("Delete Set From Database");
                dialog3.setMessage("Are you sure you want to delete the current Set?");
                dialog3.setPositiveButton("Delete Set", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Toast.makeText(newComponantSet.this, "Set "+currentLoadedSet+" has been removed from database", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
                dialog3.setNegativeButton("Back", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    }
                });
                dialog3.show();
        }

    }

    private void populateListview(String table, String name) {
        listview.setBackgroundColor(Color.WHITE);

        if (table.equals("Resistor")) {
            ArrayAdapter<itemNewComp> adapter = new newComponantSet.myListAdapter(table);
            adapter.notifyDataSetChanged();
            listview = (ListView) findViewById(R.id.listview);
            listview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
            listview.setItemsCanFocus(false);
            listview.setAdapter(adapter);
        } else if (table.equals("Capacitor")) {
            ArrayAdapter<itemNewComp> adapter = new newComponantSet.myListAdapter(table);
            adapter.notifyDataSetChanged();
            listview = (ListView) findViewById(R.id.listview);
            listview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
            listview.setItemsCanFocus(false);
            listview.setAdapter(adapter);
        } else {
            Toast.makeText(this, "problem Populate ListView", Toast.LENGTH_SHORT).show();
        }


    }

    /**
     * This list adapter is used in the New,Load,Save buttons in order to display the correct information on screen
     * User should be able to select from the items displayed and when the save button is clicked should update the
     * database with the selected items. needs to know the table that it is working with and the action of the button
     * which is being performed
     */

    public class myListAdapter extends ArrayAdapter<itemNewComp> {
        private String table;

        private class ViewHolder {

            ImageView itemImage;
            CheckBox ck;


        }

        // private String action;
        public myListAdapter(String table) {
            super(newComponantSet.this, R.layout.itemview_newresistor, currentItemList);
            this.table = table;
            //// pass in New, Load or Save
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            itemNewComp item = getItem(position);

            final ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();

                LayoutInflater inflater = LayoutInflater.from(getContext());

                convertView = inflater.inflate(R.layout.itemview_newresistor, parent, false);

                viewHolder.itemImage = (ImageView) convertView.findViewById(R.id.itemNCS_ImageView);

                viewHolder.ck = (CheckBox) convertView.findViewById(R.id.itemNCS_CHECKBOX);

                // Cache the viewHolder object inside the fresh view

                convertView.setTag(viewHolder);

            } else {
                // View is being recycled, retrieve the viewHolder object from tag

                viewHolder = (ViewHolder) convertView.getTag();

            }

            View ItemView = convertView;
            itemNewComp currentItem = currentItemList.get(position);
            ItemView.setBackgroundColor(Color.LTGRAY);
            //ImageView imageview = (ImageView) ItemView.findViewById(R.id.itemNCS_ImageView);
            if (table.equals("Capacitor")) {
                // do shit if capacitor
                Symbol s1 = new Symbol("Capacitor");
                String say = s1.numberToSymbol(currentItem.getCapacitor());
                viewHolder.ck.setChecked(currentItem.getSelected());
                viewHolder.ck.setTextSize(30);
                viewHolder.ck.setText(say);
                // Hz is \u3390// omega is \u2126// micro Farad is \U338C// nano Farad is U338B// pico Farad is \U338A
                if(currentItem.getType().equals("ceramic")){
                    viewHolder.itemImage.setImageResource(R.drawable.ceramic_cap);
                }else{
                    viewHolder.itemImage.setImageResource(R.drawable.cap);
                }
                viewHolder.ck.setOnClickListener(new MyListener(item, position) {
                });
                notifyDataSetChanged();

            } else if (table.equals("Resistor")) {
                // do shit if Resistor
                String band1 = currentItem.getBand1();
                String band2 = currentItem.getBand2();
                String band3 = currentItem.getBand3();
                PaintResistor pr1 = new PaintResistor(band1, band2, band3);
                Bitmap bitmap = Bitmap.createBitmap(viewHolder.itemImage.getMaxWidth(), viewHolder.itemImage.getMaxHeight(), Bitmap.Config.RGB_565);
                bitmap.eraseColor(Color.parseColor("#F9F9F9"));
                Canvas c1 = new Canvas(bitmap);
                pr1.drawResistorBody(c1, viewHolder.itemImage.getMaxWidth(), viewHolder.itemImage.getMaxHeight());
                viewHolder.itemImage.setImageBitmap(bitmap);
                viewHolder.ck.setChecked(currentItem.getSelected());
                Symbol s1 = new Symbol("Resistor");
                String say = s1.numberToSymbol(currentItem.getResistor());
                viewHolder.ck.setTextSize(30);
                viewHolder.ck.setText(say + " " + "\u2126");
                viewHolder.ck.setOnClickListener(new MyListener(item, position) {

                });
                notifyDataSetChanged();
            } else {
                Toast.makeText(newComponantSet.this, "action in New failed", Toast.LENGTH_SHORT).show();
            }

            return convertView;

        }

    }

    public class MyListener implements View.OnClickListener {
        itemNewComp i1;
        int position;

        public MyListener(itemNewComp i1, int position) {
            this.i1 = i1;
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            //i1.setSelected();
            if (temp.contains(i1)) {
                temp.remove(i1);


            } else {
                i1.setSelected();
                temp.add(i1);
            }
            i1.setSelected();
            currentItemList.set(position, i1).setSelected();
        }
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

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.item4:
                // make intent to beam these lists to different place.
                Intent intent = new Intent(newComponantSet.this, MainActivity.class);
                startActivity(intent);

                return true;
            case R.id.item2:
                startActivity(new Intent(newComponantSet.this, helpPage.class));
                return true;
            case R.id.item3:
                startActivity(new Intent(newComponantSet.this, helpPage.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void setFunkyBackround() {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int yScale = displaymetrics.heightPixels;
        int xScale = displaymetrics.widthPixels;
        Bitmap bitmap = Bitmap.createBitmap(xScale, yScale, Bitmap.Config.RGB_565);
        canvas1 = new Canvas(bitmap);
        bitmap.eraseColor(Color.parseColor("#ffdb24"));
        drawCircle(.5f * xScale, .5f * yScale, (.5f * xScale) + .5f * yScale);
        View view = findViewById(R.id.activity_new_componant_set);
        Drawable d = new BitmapDrawable(getResources(), bitmap);
        view.setBackground(d);
    }

    public void drawCircle(float x, float y, float radius) {
        Paint p1 = new Paint(Paint.ANTI_ALIAS_FLAG);
        p1.setColor(Color.parseColor("#ff6666"));
        p1.setStrokeWidth(4f);
        p1.setStyle(Paint.Style.STROKE);
       // radius= (float)Math.random()*radius;
        canvas1.drawCircle(x, y, radius, p1);
        if (radius > 170) {
            drawCircle(x + radius / 2, y, radius / 2);
            drawCircle(x - radius / 2, y, radius / 2);
            drawCircle(x, y + radius / 2, radius / 2);
            drawCircle(x, y - radius / 2, radius / 2);
        }

    }
}
