package com.example.surface.rcmage.Class;

import java.io.Serializable;

/**
 * Created by Surface on 8/12/2016.
 */

public class capRecord implements Serializable {

    private Double value;
    private String type;


public capRecord( Double value, String type){

    this.value= value;
    this.type = type;

}

    public Double getValue() {
        return value;

    }

    public void setValue(Double value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}