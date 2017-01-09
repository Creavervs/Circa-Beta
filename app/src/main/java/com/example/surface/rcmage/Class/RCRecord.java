package com.example.surface.rcmage.Class;
import android.media.Image;

import java.io.Serializable;

/**
 * Created by Surface on 29/11/2016.
 *
 * The function of this class is to create a record of the componant for the database. The DB_Handler will take care of
 * the database while this class provides the information about the object.
 *
 * Wishful programming would like to use the set name ( E12 resistors/ custom set/E24 series:) and map to the Rc record object.
 * based on whether it is a resistor or capacitor use this set in a temporary arraylist in the main activity
 *
 * These fields may change in the future and possibly look at % tolerance of componant or type i.e electrolytic, ceramic
 *
 */

public class RCRecord implements Serializable {

    private Double value;
    private String band1;
    private String band2;
    private String band3;

    //private Image image;
// Resistor record
    public RCRecord(Double value, String band1, String band2, String band3) {

        this.value = value;
        this.band1 = band1;
        this.band2 = band2;
        this.band3 = band3;

        //this.image = image;
    }


    public Double getValue() {

        return value;
    }

    public void setValue(Double value) {
        this.value = value;
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
}