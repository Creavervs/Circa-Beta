package com.example.surface.rcmage.Class;

import java.io.Serializable;

/**
 * Created by Surface on 8/12/2016.
 */

public class itemViewRecord implements Serializable {
    private Double frequency;
    private Double resistor;
    private Double capacitor;
    private Double error;
    private String band1;
    private String band2;
    private String band3;

private int sliderPosition;
    private int desiredFrequency;




    public itemViewRecord(Double frequency, Double resistor, Double capacitor,Double error,String band1,String band2,String band3,int sliderPosition,int desiredFrequency){
        this.frequency = frequency;
        this.capacitor= capacitor;
        this.resistor= resistor;
        this.error=error;
        this.band1=band1;
        this.band2 = band2;
        this.band3 = band3;
        this.sliderPosition=sliderPosition;
        this.desiredFrequency= desiredFrequency;
}
    public Double getFrequency() {
        return frequency;
    }

    public void setFrequency(Double frequency) {
        this.frequency = frequency;
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

    public Double getError() {
        return error;
    }

    public void setError(Double error) {
        this.error = error;
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

    public int getSliderPosition() {
        return sliderPosition;
    }

    public void setSliderPosition(int sliderPosition) {
        this.sliderPosition = sliderPosition;
    }

    public int getDesiredFrequency() {
        return desiredFrequency;
    }

    public void setDesiredFrequency(int desiredFrequency) {
        this.desiredFrequency = desiredFrequency;
    }
}
