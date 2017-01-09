package com.example.surface.rcmage.Activities;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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
import com.example.surface.rcmage.Class.Symbol;
import com.example.surface.rcmage.Class.itemNewComp;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

// This Activity has multiple functions

// The top function will be used to find two resistor values that make up a give Resistance Total
// The algorithm used for this is get a value from the current set and multiply it by a multiplier.
// then find the closest resistor value that exists in the set. Call this value R1. Do the calculation
// 1/Rtotal - 1/R1 = 1/R2. This will give a value. Find the closest resistor value in the set and call this
// value R2. Do the calculation again and see how close you get to the final answer. If the threshold is exceeded
// make this value the new threshold and grab the next resistor value and repeat.

// The Lower function of this activity will calculate the parallel resistor values given a R1 and R2 value
// This is just user defined and does not use the databases loaded set.

public class Resistor_parallel_calculator extends AppCompatActivity {
    private EditText R1Value;
    private EditText R2Value;
    private EditText RtotalValue;
    private TextView textViewTopDisplay;
    private TextView textViewBottomDisplay;
    private ImageView imgTop;
    private ImageView imgBottom;
    private DB_Handler db;
    private Button buttonTop;
    private Button buttonBottom;
    private List<itemNewComp> currentList;
    private double r1;
    private double r2;
    private double rtotal;
    private List<String> nameStringList;
    private List<Double> currentValueList;
    private double currentR1;
    private double currentR2;
    private double error;
    private TextView topR1;
    private TextView topR2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DB_Handler(this, "", null, 1);
        currentList = new ArrayList<>();
        currentList = db.getitemNewComp("Resistor");
        setContentView(R.layout.activity_resistor_parallel_calculator);


        topR1 = (TextView) findViewById(R.id.text_top_R1);
        topR2 = (TextView) findViewById(R.id.text_top_R2);
        buttonTop = (Button) findViewById(R.id.buttonTop);
        buttonBottom = (Button) findViewById(R.id.buttonBottom);
        buttonBottom.setBackgroundColor(Color.parseColor("#ffcc33"));
        buttonTop.setBackgroundColor(Color.parseColor("#ffcc33"));
        R1Value = (EditText) findViewById(R.id.editTextR1Value);
        R2Value = (EditText) findViewById(R.id.editTextR2Value);
        RtotalValue = (EditText) findViewById(R.id.editTextRtotal);
        textViewTopDisplay = (TextView) findViewById(R.id.textViewTopdisplay);
        textViewBottomDisplay = (TextView) findViewById(R.id.textViewBottomDisplay);
        imgTop = (ImageView) findViewById(R.id.imageViewTopPic);
        imgBottom = (ImageView) findViewById(R.id.imageViewBottomPic);
        R1Value.setImeOptions(EditorInfo.IME_ACTION_DONE);
        R2Value.setImeOptions(EditorInfo.IME_ACTION_DONE);
        RtotalValue.setImeOptions(EditorInfo.IME_ACTION_DONE);
    }


    // class to show how given your pocket full of resistors what parallel combinations can be used
    // in order to achieve the value needed.
    public void gotClicked(View view) {
        switch (view.getId()) {
            case (R.id.buttonTop):
                if (RtotalValue.getText().toString().equals("")) {
                    Toast.makeText(this, "R Total Value Empty", Toast.LENGTH_SHORT).show();
                    break;
                }
                AlertDialog.Builder dialog = new AlertDialog.Builder(Resistor_parallel_calculator.this);
                dialog.setTitle("Choose Current Resistor Set");
                nameStringList = db.getSetFromDataBase("Resistor");
                final Spinner spinner = new Spinner(this);
                dialog.setMessage("Choose The Set That You Will Be Using And Hit The OK Button To Display The Resistor Values");
                dialog.setView(spinner);
                final String[] charList = nameStringList.toArray(new String[nameStringList.size()]);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, charList);
                spinner.setAdapter(adapter);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        // just say what item you landed on and what DATABASE set we are currently usinga.
                        Toast.makeText(getBaseContext(), parent.getItemAtPosition(position) + " selected", Toast.LENGTH_LONG).show();
                        // need to set current Data base

                        if (parent.getItemAtPosition(position) != null) {
                            findClosestValueInSet(parent.getItemAtPosition(position).toString());

                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        // Default problem as nothing is selected // should never get here.
                    }
                });
                dialog.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //   Toast.makeText(Resistor_parallel_calculator.this, "Should display R1 and R2 Values", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });

                dialog.show();
                break;
            case (R.id.buttonBottom):
                if (R1Value.getText().toString().equals("") && R2Value.getText().toString().equals("")) {
                    Toast.makeText(this, "Both R1 And R2 Values Empty", Toast.LENGTH_SHORT).show();
                    break;
                }
                if (R1Value.getText().toString().equals("")) {
                    Toast.makeText(this, "R1 Value Empty", Toast.LENGTH_SHORT).show();
                    break;
                }
                if (R2Value.getText().toString().equals("")) {
                    Toast.makeText(this, "R2 Value Empty", Toast.LENGTH_SHORT).show();
                    break;
                }
                double displayTotal = calculateTotal();
                Symbol s1 = new Symbol("Resistor");
                String name = s1.numberToSymbol(displayTotal);
                textViewBottomDisplay.setTextSize(16);
                textViewBottomDisplay.setText("Total Restistance is: " + name + " " + "\u2126");
                break;
        }
    }

    public double calculateTotal() {
        String p1 = R1Value.getText().toString();
        String p2 = R2Value.getText().toString();
        double r1 = Double.parseDouble(p1);
        double r2 = Double.parseDouble(p2);
        double den = r1 + r2;
        double num = r1 * r2;
        double total = num / den;
        BigDecimal bd = new BigDecimal(total);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
    // The top function will be used to find two resistor values that make up a give Resistance Total
// The algorithm used for this is get a value from the current set and multiply it by a multiplier.
// then find the closest resistor value that exists in the set. Call this value R1. Do the calculation
// 1/Rtotal - 1/R1 = 1/R2. This will give a value. Find the closest resistor value in the set and call this
// value R2. Do the calculation again and see how close you get to the final answer. If the threshold is exceeded
// make this value the new threshold and grab the next resistor value and repeat.

    public void findClosestValueInSet(String setName) {
        currentValueList = db.SetLocalValueArrayList("Resistor", setName);
        String p3 = RtotalValue.getText().toString();
        double Rtotal = Double.parseDouble(p3);
// This is for the case that the resistor value entered already exists in the currently selected resistor list.
        if (currentValueList.contains(Rtotal)) {
            Toast.makeText(this, "Just Use This Resistor Numpty " + Rtotal, Toast.LENGTH_SHORT).show();
            topR2.setText("");
            topR1.setText("");
            textViewTopDisplay.setText("Use a single Resistor: " + Rtotal);
            return;
        }
        double THRESHHOLD = 1000000;
        for (int i = 10; i < 21; i++) {
            double multiplier = i / 10;
            double firstResistor = multiplier * Rtotal;
            for (int j = 0; j <= currentValueList.size(); j++) {
                double current = currentValueList.get(j);
                if (current >= firstResistor) {
                    firstResistor = current;
                    break;
                }
            }
            double den = firstResistor - Rtotal;
            double num = Rtotal * firstResistor;
            double secondResistor = Math.round(num / den);
            for (int j = 0; j <= currentValueList.size(); j++) {
                double current = currentValueList.get(j);
                if (current >= secondResistor) {
                    secondResistor = current;
                    break;
                }

            }
            if (Math.abs(doCalculate(firstResistor, secondResistor) - Rtotal) <= THRESHHOLD) {
                currentR1 = firstResistor;
                currentR2 = secondResistor;
                error = doCalculate(firstResistor, secondResistor);
                THRESHHOLD = Math.abs(error - Rtotal);
            }


        }
        Symbol s1 = new Symbol("Resistor");
        //Toast.makeText(this, currentR1+ "currentR1"+currentR2+"current R2"+ error+ "Error", Toast.LENGTH_SHORT).show();
        textViewTopDisplay.setText("Resistor Total: " + error);
        String r1 = s1.numberToSymbol(currentR1);
        String r2 = s1.numberToSymbol(currentR2);
        topR1.setText("Current R1: " + r1);
        topR2.setText("Current R2: " + r2);
    }
// method to calculate the total resistance
    public double doCalculate(double d1, double d2) {
        double den = d1 + d2;
        double num = d1 * d2;

        double total = (num / den);
        BigDecimal bd = new BigDecimal(total);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();


    }

    public boolean compare(double d1, double d2, double desired) {
        return (Math.abs(d1 - desired) > Math.abs(d2 - desired));
    }
}