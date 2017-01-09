package com.example.surface.rcmage.Activities;

import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.surface.rcmage.Class.DB_Handler;
import com.example.surface.rcmage.Class.PaintResistor;
import com.example.surface.rcmage.Class.RCRecord;
import com.example.surface.rcmage.Class.Symbol;
import com.example.surface.rcmage.Class.itemNewComp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// Activity to show band colours given of a given resistor by ohm rating or
// input by band colours. The first band is loaded from a string array set
// in the String resources. The  second band is loaded dynamically and is loaded
// based on the users input of the 1st band. Third band is also loaded from a string
// array and is limited to the highest value of the resistor set being used.

// Query button finds the resistor that the user inputs ( If it exists in the E24 dataset)
// It then prints the tolerance of the resistor as well as the resistor value in the lower
// textview. The Spinners are then altered to suit the required value.
public class Resistor_Band_Viewer extends AppCompatActivity {
    private ImageView image;
    private EditText editText;
    private DB_Handler db;
    private static List<itemNewComp> currentList;
    private ArrayAdapter<String> band1_Colour;
    private ArrayAdapter<CharSequence> band2_Colour;
    private ArrayAdapter<CharSequence> band3_Colour;
    private ArrayAdapter<CharSequence> band4_Colour;
    private String LoadedSet = "E24 Resistor";
    private Spinner band1spinner;
    private Spinner band2spinner;
    private Spinner band3spinner;
    private Spinner band4spinner;
    private CharSequence[] allColours1;
    private CharSequence[] allColours2;
    private CharSequence[] allColours3;
    private CharSequence[] lastBand;
    private TextView textView;
    private TextView textView2;
    private CharSequence[] allBands;
    private Button goButton;
    private Button queryButton;
    private String band1;
    private String band2;
    private String band3;
    private String band4;
    private Boolean present;
    private Boolean Query;
    private List<String> band2List;
    private List<String> band1List;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resistor__band__viewer);
        Query = false;
        image =(ImageView)findViewById(R.id.imageView);
        db = new DB_Handler(this, "", null, 1);
        goButton = (Button) findViewById(R.id.Gobutton);
        queryButton = (Button) findViewById(R.id.QueryButton);
        currentList = new ArrayList<>();
        allColours1 = getResources().getStringArray(R.array.ColorSetNames);
        allColours2 = getResources().getStringArray(R.array.ColorSetNames);
        allColours3 = getResources().getStringArray(R.array.ColorSetNamesband3);
        lastBand = getResources().getStringArray(R.array.lastBand);
        currentList = db.getitemNewComp("Resistor");
        band1spinner = (Spinner) findViewById(R.id.spinner_band1);
        band2spinner = (Spinner) findViewById(R.id.spinner_band2);
        band3spinner = (Spinner) findViewById(R.id.spinner_band3);
        band4spinner = (Spinner) findViewById(R.id.spinner_band4);
        textView = (TextView) findViewById(R.id.displayText);
        textView2 = (TextView) findViewById(R.id.displayTolerance);
        band2List = new ArrayList();
        final CharSequence[] charList = band2List.toArray(new String[band2List.size()]);
        band1List = new ArrayList<>();
        band1List = Arrays.asList(getResources().getStringArray(R.array.ColorSetNames));

        band1_Colour = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, band1List);
        band2_Colour = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, charList);
        band3_Colour = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, allColours3);
        band4_Colour = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, lastBand);

        band1_Colour.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        band2_Colour.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        band3_Colour.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        band4_Colour.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        band1spinner.setAdapter(band1_Colour);
        band1spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // just say what item you landed on and what DATABASE set we are currently using
                //Toast.makeText(getBaseContext(), parent.getItemAtPosition(position) + " selected", Toast.LENGTH_LONG).show();
                // need to set current Data base
                if (parent.getItemAtPosition(position) != null) {
                    if(Query) {
                        band1 = parent.getItemAtPosition(position).toString() + ",";
                        Query = false;
                    }
                        else{
                        band1 = parent.getItemAtPosition(position).toString() + ",";
                        setBand2Adapter(band1);
                        }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Default problem as nothing is selected // should never get here.
            }
        });
        band2spinner.setAdapter(band2_Colour);
        band2spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // just say what item you landed on and what DATABASE set we are currently using
                // Toast.makeText(getBaseContext(), parent.getItemAtPosition(position) + " selected", Toast.LENGTH_LONG).show();
                // need to set current Data base
                if (parent.getItemAtPosition(position) != null) {
                    band2 = parent.getItemAtPosition(position).toString() + ",";
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Default problem as nothing is selected // should never get here.
            }
        });
        band3spinner.setAdapter(band3_Colour);
        band3spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // just say what item you landed on and what DATABASE set we are currently using
                // Toast.makeText(getBaseContext(), parent.getItemAtPosition(position) + " selected", Toast.LENGTH_LONG).show();
                // need to set current Data base
                if (parent.getItemAtPosition(position) != null) {
                    band3 = parent.getItemAtPosition(position).toString();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Default problem as nothing is selected // should never get here.
            }
        });
        band4spinner.setAdapter(band4_Colour);
        band4spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // just say what item you landed on and what DATABASE set we are currently using
                //Toast.makeText(getBaseContext(), parent.getItemAtPosition(position) + " selected", Toast.LENGTH_LONG).show();
                // need to set current Data base
                if (parent.getItemAtPosition(position) != null) {
                    band4 = parent.getItemAtPosition(position).toString();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Default problem as nothing is selected // should never get here.
            }
        });

    }


    public void button_click(View view) {
        switch (view.getId()) {
            case R.id.Gobutton:
                RCRecord rc = db.getBandColourFromDatabase(band1 + band2 + band3);
                if (rc == null) {
                    Toast.makeText(this, "You dont have enough bands selected for this", Toast.LENGTH_SHORT).show();
                    break;
                }

                Double value = rc.getValue();
                Symbol s1 = new Symbol("Resistor");
                String name = s1.numberToSymbol(value);
                textView.setTextSize(22);
                textView2.setTextSize(20);
                textView.setText("Resistor Value: " + name + "\u2126");
                textView.setTypeface(null, Typeface.BOLD);
                textView2.setTypeface(null, Typeface.BOLD);
                if (band4.equals("Silver")) {
                    textView2.setText("Tolerance: 10%");
                } else {
                    textView2.setText("Tolerance: 5%");
                }
                setImage(band1,band2,band3);
                break;
            case R.id.QueryButton:

                AlertDialog.Builder dialog = new AlertDialog.Builder(Resistor_Band_Viewer.this);
                dialog.setMessage("Enter A Known Resistor Value Into The Box Below And We Will Find It In The E24 Series Resistor Set");
                dialog.setTitle("Resistor Query Wizard");
                dialog.setCancelable(false);
                final EditText new_Resistor = new EditText(this);
                new_Resistor.setRawInputType(Configuration.KEYBOARD_12KEY);
                new_Resistor.setHint("    Type Here");
                new_Resistor.setImeOptions(EditorInfo.IME_ACTION_DONE);
                new_Resistor.setOnEditorActionListener(new TextView.OnEditorActionListener() {

                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        return false;
                    }
                });
                dialog.setView(new_Resistor);
                dialog.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (new_Resistor.getText().equals("")) {
                            Toast.makeText(Resistor_Band_Viewer.this, "Nothing was entered", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                        double value = Double.parseDouble(new_Resistor.getText().toString());
                        present = false;

                        for (itemNewComp i : currentList) {
                            if (i.getResistor() == value) {
                                Query = true;
                                present = true;
                                String b2 = i.getBand2();
                                int b1 = Arrays.asList(allColours1).indexOf(i.getBand1());
                                band1spinner.setSelection(b1);
                                //int b2 = Arrays.asList(band2_Colour).indexOf(i.getBand2());
                                int b3 = Arrays.asList(allColours1).indexOf(i.getBand3());
                                band1 = band1spinner.getSelectedItem().toString() + ",";
                                setBand2Adapter(band1);
                                band2spinner.setSelection(band2_Colour.getPosition(b2));
                                band3spinner.setSelection(b3);

                                band2 = band2spinner.getSelectedItem().toString() + ",";
                                band3 = band3spinner.getSelectedItem().toString();
                               // band4 = band4spinner.getSelectedItem().toString();
                                Symbol s1 = new Symbol("Resistor");
                                String name = s1.numberToSymbol(i.getResistor());

                                textView.setTextSize(22);
                                textView2.setTextSize(20);
                                textView.setText("Resistor Value: " + name + "\u2126");
                                textView.setTypeface(null, Typeface.BOLD);
                                textView2.setTypeface(null, Typeface.BOLD);
                                if (band4.equals("Silver")) {
                                    textView2.setText("Tolerance: 10%");
                                } else {
                                    textView2.setText("Tolerance: 5%");
                                }
                                setImage(band1, band2, band3);
                                dialog.dismiss();
                            }


                        }
                        if (present == false) {
                            Toast.makeText(Resistor_Band_Viewer.this, "Unknown value entered", Toast.LENGTH_SHORT).show();
                            textView2.setText("");
                            textView.setText("");
                            dialog.dismiss();
                        }
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

    public void setBand2Adapter(String currentband1) {
        band2List = db.SetLocalColorband2ArrayList(currentband1, LoadedSet);
        final CharSequence[] charList = band2List.toArray(new String[band2List.size()]);
        band2_Colour = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, charList);
        band2spinner.setAdapter(band2_Colour);

    }

    public void setImage(String b1,String b2,String b3) {
        b1= b1.replace(",","");
        b2= b2.replace(",","");
        PaintResistor pr1 = new PaintResistor(b1, b2, b3);
        Bitmap bitmap = Bitmap.createBitmap(image.getMaxWidth(), image.getMaxHeight(), Bitmap.Config.RGB_565);
        bitmap.eraseColor(Color.parseColor("#F9F9F9"));
        Canvas c1 = new Canvas(bitmap);
        pr1.drawResistorBody(c1, image.getMaxWidth(), image.getMaxHeight());
        image.setImageBitmap(bitmap);
    }
}