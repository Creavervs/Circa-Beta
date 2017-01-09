package com.example.surface.rcmage.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.surface.rcmage.Class.DB_Handler;
import com.example.surface.rcmage.Class.Symbol;
import com.example.surface.rcmage.Class.itemNewComp;
import com.example.surface.rcmage.Class.itemViewRecord;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ActivehighPassFilter extends AppCompatActivity {
    private DB_Handler db;
    private List<String> nameStringList;
    private List<String> nameCapList;
    private String currentLoadedRes;
    private String currentLoadedCap;
    private List<itemNewComp> currentValueResList;
    private List<itemNewComp> currentValueCapList;
    private List<itemNewComp> currentMasterResList;
    private List<itemNewComp> currentMasterCapList;
    private List<Double> currentValueList;
    private Spinner Rspinner;
    private Spinner Cspinner;
    private Spinner R1spinner;
    private Spinner R2spinner;
    private ArrayAdapter<String> ResAdapter;
    private ArrayAdapter<String> CapAdapter;
    private double R1;
    private double R2;
    private double Rzero;
    private double C;
    private TextView gainView;
    private TextView freqView;
    private EditText editGain;
    private EditText editFreq;
    private List<itemViewRecord> currentItemList;
    private ImageButton imageButton;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_activelow_pass_filter);
            db = new DB_Handler(this, "", null, 1);
            imageButton= (ImageButton)findViewById(R.id.imageButton_ALPF);
            imageButton.setImageResource(R.drawable.active_high_pass_filter);
            gainView = (TextView) findViewById(R.id.gainView);
            freqView = (TextView) findViewById(R.id.freqView);
            editGain = (EditText) findViewById(R.id.editGain);
            editFreq = (EditText) findViewById(R.id.editFreq);
            editFreq.setImeOptions(EditorInfo.IME_ACTION_DONE);
            editGain.setImeOptions(EditorInfo.IME_ACTION_DONE);
            currentLoadedCap = "E6 Capacitor";
            currentLoadedRes = "E12 ActiveFilter";
            currentMasterCapList = db.getitemNewComp("Capacitor");// loads up item list all E12 all selected no
            currentMasterResList = db.getitemNewComp("Resistor");// loads up item list all E12 all clicked no
            currentValueCapList = setCapacitorList();
            currentValueResList = setResistorList();
            Toast.makeText(this, "The Capacitor Set Is " + currentLoadedCap + " " + currentValueCapList.size(), Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "The Resistor Set Is " + currentLoadedRes + " " + currentValueResList.size(), Toast.LENGTH_SHORT).show();
            final List<String> ResistoArray = new ArrayList<>();
            for (int i = 0; i < currentValueResList.size(); ++i) {
                Symbol s1 = new Symbol("Resistor");
                String s = s1.numberToSymbol(currentValueResList.get(i).getResistor());
                ResistoArray.add(s);
            }
            List<String> CapArray = new ArrayList<>();
            for (int i = 0; i < currentValueCapList.size(); ++i) {
                Symbol s1 = new Symbol("Capacitor");
                String s = s1.numberToSymbol(currentValueCapList.get(i).getCapacitor());
                CapArray.add(s);
            }
            ResAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item_main_activity, ResistoArray);
            ResAdapter.setDropDownViewResource(R.layout.spinner_item_main_activity);
            CapAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item_main_activity, CapArray);
            CapAdapter.setDropDownViewResource(R.layout.spinner_item_main_activity);
            Rspinner = (Spinner) findViewById(R.id.Rspinner_ALPF);
            Rspinner.setAdapter(ResAdapter);
            Rspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    itemNewComp I = currentValueResList.get(position);
                    Rzero = I.getResistor();
                    Double newFrequency = 1.0 / 2.0 * Math.PI * (C) * (Rzero);

                    DecimalFormat format = new DecimalFormat("0.#");
                    freqView.setText("Frequency: " + format.format(newFrequency) + "Hz");
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // Default problem as nothing is selected // should never get here.
                }
            });
            R1spinner = (Spinner) findViewById(R.id.R1spinner_ALPF);
            R1spinner.setAdapter(ResAdapter);
            R1spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    itemNewComp I = currentValueResList.get(position);
                    R1 = I.getResistor();
                    double gain = 1 + (R2 / R1);

                    DecimalFormat format = new DecimalFormat("0.##");
                    gainView.setText("Gain: " + format.format(gain));
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // Default problem as nothing is selected // should never get here.
                }
            });
            R2spinner = (Spinner) findViewById(R.id.R2spinner_ALPF);
            R2spinner.setAdapter(ResAdapter);
            R2spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    itemNewComp I = currentValueResList.get(position);
                    R2 = I.getResistor();
                    double gain = 1 + (R2 / R1);
                    DecimalFormat format = new DecimalFormat("0.##");
                    gainView.setText("Gain: " + format.format(gain));
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // Default problem as nothing is selected // should never get here.
                }
            });
            Cspinner = (Spinner) findViewById(R.id.Cspinner_ALPF);
            Cspinner.setAdapter(CapAdapter);
            Cspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    itemNewComp I = currentValueCapList.get(position);
                    C = I.getCapacitor();
                    Double newFrequency = 1.0 / (2.0 * Math.PI * (C) * (Rzero));
                    DecimalFormat format = new DecimalFormat("0.#");
                    freqView.setText("Frequency: " + format.format(newFrequency) + "Hz");
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // Default problem as nothing is selected // should never get here.
                }
            });
        }

    public void clicka(View view) {
        switch (view.getId()) {
            case (R.id.imageButton_ALPF):
                AlertDialog.Builder dialog = new AlertDialog.Builder(ActivehighPassFilter.this);
                dialog.setCancelable(false);
                //dialog.setTitle("Zoomed Image");
                LayoutInflater inflater = getLayoutInflater();
                View dialogLayout = inflater.inflate(R.layout.image_zoom_view, null);
                dialog.setView(dialogLayout);
                dialog.setNeutralButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
                break;
            case (R.id.button_ALPF_GO):
                AlertDialog.Builder dialog3 = new AlertDialog.Builder(ActivehighPassFilter.this);
                dialog3.setCancelable(false);
                dialog3.setTitle("Choices To Make");
                dialog3.setMessage("This has been created such that you can adjust gain or frequencies on the fly by the user input. This will alter the currently selected Capacitors or Resistors");
                dialog3.setPositiveButton("Set Gain", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        double desiredGain = Double.parseDouble(editGain.getText().toString());
                        doGainCalculate(desiredGain);
//                        double gain = 1 + (R2 / R1);
//                        DecimalFormat format = new DecimalFormat("0.##");
//                        gainView.setText("Gain: " + format.format(gain));
                        dialog.dismiss();
                    }
                });
                dialog3.setNegativeButton("Set Frequency", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int desiredFreq = Integer.parseInt(editFreq.getText().toString());
                        doFrequencyCalculate(desiredFreq);
                        double top = currentItemList.get(0).getFrequency();
                        freqView.setText("Frequency: " + top);
                        double requiredR = currentItemList.get(0).getResistor();
                        Symbol s1 = new Symbol("Resistor");
                        Symbol s2 = new Symbol("Capacitor");
                        int spinnerPosition = ResAdapter.getPosition(s1.numberToSymbol(requiredR));
                        Rspinner.setSelection(spinnerPosition);
                        double requiredC = currentItemList.get(0).getCapacitor();
                        int spinnerPosition1 = CapAdapter.getPosition(s2.numberToSymbol(requiredC));
                        Cspinner.setSelection(spinnerPosition1);
                        dialog.dismiss();
                    }
                });
                dialog3.setNeutralButton("Back", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog3.show();
                break;
            case (R.id.button_ALPF_Load_Capacitor):
                AlertDialog.Builder dialog1 = new AlertDialog.Builder(ActivehighPassFilter.this);
                dialog1.setCancelable(false);
                dialog1.setTitle("Load Capacitor Set");
                nameCapList = db.getSetFromDataBase("Capacitor");
                for (int i = 0; i < nameCapList.size(); ++i) {
                    String s = nameCapList.get(i);
                    nameCapList.remove(i);
                    nameCapList.add(i, s + " (C)");
                }
                final String[] charListCap = nameCapList.toArray(new String[nameCapList.size()]);
                dialog1.setItems(charListCap, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // the user clicked on a String
                        // call contains on both sets to find out which table we dealing with
                        // We then check them against the 24 series resistors and check them off as selected.
                        String item = charListCap[which].replace(" (C)", "").replace(" (R)", "");
                        currentLoadedCap = item;
                        currentValueCapList = setCapacitorList();
                        Toast.makeText(ActivehighPassFilter.this, "The Capacitor Set Is " + currentLoadedCap + " " + currentValueCapList.size(), Toast.LENGTH_SHORT).show();
                        List<String> CapArray = new ArrayList<>();
                        for (int i = 0; i < currentValueCapList.size(); ++i) {
                            Symbol s1 = new Symbol("Capacitor");
                            String s = s1.numberToSymbol(currentValueCapList.get(i).getCapacitor());
                            CapArray.add(s);
                        }
                        ArrayAdapter<String> CapAdapter = new ArrayAdapter<String>(ActivehighPassFilter.this, R.layout.spinner_item_main_activity, CapArray);
                        CapAdapter.setDropDownViewResource(R.layout.spinner_item_main_activity);
                        Cspinner.setAdapter(CapAdapter);
                        dialog.dismiss();
                    }
                });

                dialog1.show();
                break;
            case (R.id.button_ALPF_Load_Resistor):
                AlertDialog.Builder dialog2 = new AlertDialog.Builder(ActivehighPassFilter.this);
                dialog2.setCancelable(false);
                dialog2.setTitle("Load Resistor Set");
                nameStringList = db.getSetFromDataBase("Resistor");
                for (int i = 0; i < nameStringList.size(); ++i) {
                    String s = nameStringList.get(i);
                    nameStringList.remove(i);
                    nameStringList.add(i, s + " (R)");
                }
                final String[] charListRes = nameStringList.toArray(new String[nameStringList.size()]);
                dialog2.setItems(charListRes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // the user clicked on a String
                        // call contains on both sets to find out which table we dealing with
                        // We then check them against the 24 series resistors and check them off as selected.
                        String item = charListRes[which].replace(" (C)", "").replace(" (R)", "");
                        currentLoadedRes = item;
                        currentValueResList = setResistorList();
                        List<String> ResistoArray = new ArrayList<>();
                        for (int i = 0; i < currentValueResList.size(); ++i) {
                            Symbol s1 = new Symbol("Resistor");
                            String s = s1.numberToSymbol(currentValueResList.get(i).getResistor());
                            ResistoArray.add(s);
                        }
                        ArrayAdapter<String> ResAdapter = new ArrayAdapter<String>(ActivehighPassFilter.this, R.layout.spinner_item_main_activity, ResistoArray);
                        ResAdapter.setDropDownViewResource(R.layout.spinner_item_main_activity);
                        Rspinner.setAdapter(ResAdapter);
                        R1spinner.setAdapter(ResAdapter);
                        R2spinner.setAdapter(ResAdapter);
                        Toast.makeText(ActivehighPassFilter.this, "The Resistor Set Is " + currentLoadedRes + " " + currentValueResList.size(), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
                dialog2.show();
                break;

        }
    }

    private List<itemNewComp> setResistorList() {
        List<itemNewComp> current = new ArrayList<>();
        currentValueList = new ArrayList<>();
        currentValueList = db.SetLocalValueArrayList("Resistor", currentLoadedRes);
        for (itemNewComp i1 : currentMasterResList) {
            for (double s1 : currentValueList) {
                if (s1 == i1.getResistor()) {
                    i1.setSelectedTrue();
                    current.add(i1);
                }

            }
        }

        return current;
    }

    private List<itemNewComp> setCapacitorList() {
        List<itemNewComp> current = new ArrayList<>();
        currentValueList = new ArrayList<>();
        currentValueList = db.SetLocalValueArrayList("Capacitor", currentLoadedCap);
        for (itemNewComp i1 : currentMasterCapList) {
            for (double s1 : currentValueList) {
                if (s1 == i1.getCapacitor()) {
                    i1.setSelectedTrue();
                    current.add(i1);
                }

            }
        }
        return current;
    }

    private void doFrequencyCalculate(int desired) {
        currentItemList = new ArrayList<>();
        for (itemNewComp c : currentValueCapList) {
            for (itemNewComp r : currentValueResList) {
                Double newFrequency = 1.0 / (2.0 * Math.PI * (c.getCapacitor()) * (r.getResistor()));
                Double middle = newFrequency - desired;
                Double error = Math.abs((middle / newFrequency)) * 100;
                if (error < 50) {
                    itemViewRecord iVrecord = new itemViewRecord(newFrequency, r.getResistor(), c.getCapacitor(), error, r.getBand1(), r.getBand2(), r.getBand3(), 5, desired);
                    currentItemList.add(iVrecord);
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

    private void doGainCalculate(double desired) {
        List <Double> currentItemList = new ArrayList<>();
        double THRESHHOLD = 100000;
        for (itemNewComp c : currentValueResList) {
            currentItemList.add(c.getResistor());
        }
        for (double r : currentItemList) {
            for(double c :currentItemList) {
                double tempRes1 = c;
                double tempRes2 = r;
                DecimalFormat format = new DecimalFormat("0.#");
                double gain = 1 + (tempRes2 / tempRes1);
                BigDecimal bd = new BigDecimal(gain);
                bd = bd.setScale(3, RoundingMode.HALF_UP);


                if (Math.abs((bd.doubleValue() - desired)) <= THRESHHOLD) {
                    R1 = tempRes1;
                    R2 = tempRes2;
                    THRESHHOLD = Math.abs((bd.doubleValue() - desired));

                }

            }


        }
        Symbol s1 = new Symbol("Resistor");
        Toast.makeText(this, "gain:"+ (1+(R2/R1)), Toast.LENGTH_SHORT).show();
        int spinnerPosition = ResAdapter.getPosition(s1.numberToSymbol(R1));
        R1spinner.setSelection(spinnerPosition);
        int spinnerPosition1 = ResAdapter.getPosition(s1.numberToSymbol(R2));
        R2spinner.setSelection(spinnerPosition1);
        gainView.setText("Gain: "+ (1+(R2/R1)));

    }

}
