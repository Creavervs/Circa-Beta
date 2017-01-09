package com.example.surface.rcmage.Class;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Surface on 10/12/2016.
 */

public class itemNewComp {

    private Double resistor;// Resistor class
    private Double capacitor;// capacitor class
    private String type; // capacitor class
    private String band1;// Resistor class
    private String band2;// Resistor class
    private String band3;// Resistor class
    private Boolean selected;

    public itemNewComp(Double resistor, Double capacitor, String type, String band1, String band2, String band3,Boolean selected) {
        this.resistor = resistor;
        this.capacitor = capacitor;
        this.type = type;
        this.band1 = band1;
        this.band2 = band2;
        this.band3 = band3;
        this.selected=selected;
    }

    public Double getResistor() {
        return resistor;
    }

    public void setResistor(Double resistor) {
        this.resistor = resistor;
    }

    public Double getCapacitor() {
        return capacitor;
    }

    public void setCapacitor(Double capacitor) {
        this.capacitor = capacitor;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBand1() {
        return band1;
    }

    public void setBand1(String band1) {
        this.band1 = band1;
    }

    public String getBand2() {
        return band2;
    }

    public void setBand2(String band2) {
        this.band2 = band2;
    }

    public String getBand3() {
        return band3;
    }

    public void setBand3(String band3) {
        this.band3 = band3;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected() {this.selected = !selected;
    }
    public void setSelectedTrue() {this.selected = true;
    }

}
