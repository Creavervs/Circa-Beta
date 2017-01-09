package com.example.surface.rcmage.Class;

import java.text.DecimalFormat;

/**
 * Created by Surface on 14/12/2016.
 */
// This class is used to find the units for resistors or capacitors based on the value passed in
    // To start using this class we need to first make a new symbol object passing in the name " Resistor"
    // or "Capacitor". This could later be extended to use inductors if the need is required.

    // Algorithm: Recursion call: Get the current number and divide by 10. If its greater than 1 increase
    // the count by 1 and call the function again. If less than 1 return the count which in turn should
    // return how many times this call was made. This in turn counts the number of zeros present.
    // for a capacitor we are multiplying by 10 until the value is greater than 1.
public class Symbol {
            String name;

            public Symbol(String name){

        this.name = name;
    }
    // Algorithm: Recursion call: Get the current number and divide by 10. If its greater than 1 increase
    // the count by 1 and call the function again. If less than 1 return the count which in turn should
    // return how many times this call was made.
    // for a capacitor we are multiplying by 10 until the value is greater than 1.
    public String numberToSymbol(Double value){
        if(name.equals("Resistor")) {
            int zeroCount = (counterResistor(value, 0));
            switch (zeroCount) {
                case (0):
                    return value.toString();
                case (1):
                    return value.toString();
                case (2):
                    return value.toString();
                case (3):
                    return value / 1000 + " k";
                case (4):
                    return value / 1000 + " k";
                case (5):
                    return value / 1000 + " k";
                case (6):
                    return value / 1000000 + " M";
            }
        }
            else if (name.equals("Capacitor")) {
            int zeroCount2 = (counterCapacitor(value, 0));
            DecimalFormat format = new DecimalFormat("0.#");
            DecimalFormat format1= new DecimalFormat("0.##");

            switch (zeroCount2) {
                case (0):
                    return "F";
                case (1):
                    return value.toString()+" F";
                case (2):
                    return value.toString() + " F";
                case (3):
                    return format.format(value*1000000) + " \u338C";
                case (4):
                    return format.format(value*1000000) + " \u338C";
                case (5):
                    return format1.format(value*1000000) + " \u338C";
                case (6):
                    return format.format(value*1000000000) + " \u338B";
                case(7):
                    return format.format(value*1000000000) + " \u338B";
                case(8):
                    return format1.format(value*1000000000) + " \u338B";
                case(9):
                    return format.format(value*1000000000000.0) + " \u338A";
                case(10):
                    return format.format(value*1000000000000.0) +  " \u338A";
                case(11):
                    return format1.format(value*1000000000000.0) +  " \u338A";
                case(12):
                    return format1.format(value*1000000000000.0) +  " \u338A";
            }
        }
        else{
            return "wrong string passed";
        }
        return "error loading value";
    }

    public int counterResistor(Double value,int count){
        value = value/10;
        if(value>.99){
            count++;
            return counterResistor(value,count);
        }
        return count;


    }

    public int counterCapacitor(Double value,int count){
        value = value*10;
        if(value<.99){
            count++;
            return counterCapacitor(value,count);
        }
        return count;


    }
// early stages of Resistor call.
    public Double changeInValue(Double value){
        int zeroCount3 = (counterCapacitor(value, 0));

        switch (zeroCount3) {
        case (0):
        return value;
        case (1):
        return value;
        case (2):


            return 100.0;

        case (3):

            return .0001;
        case (4):

            return .000001;
        case (5):

            return .0000001;
        case (6):

            return .00000001;

        case(7):

            return .000000001 ;
        case(8):

            return .0000000001 ;
        case(9):

            return .00000000001;
        case(10):

               return  .000000000001;

        case(11):

                return .0000000000001;

    }
return 100.0;
    }

}
