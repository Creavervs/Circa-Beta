package com.example.surface.rcmage.Class;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

/**
 * Created by Surface on 14/12/2016.
 */

    // This class is used for drawing the resistor body and bands. It takes the three bands as a argument
    //the resistor is then drawn given the canvas of the imageview or backround image and pass in
    // the height and width of the current view. The canvas is returned with the appropriate resistor
    // body and band colours drawn onto it.

public class PaintResistor {
    String band1;
    String band2;
    String band3;

    public  PaintResistor(String band1, String band2, String band3) {
        this.band1 = band1;
        this.band2 = band2;
        this.band3 = band3;
    }

    public String getBand1() {

        return band1;
    }

    public String getBand2() {
        return band2;
    }

    public String getBand3() {
        return band3;
    }

    // predefined colours of the band
    public Paint setCurrentColor(String colorName) {
        Paint selectedColor = new Paint();
        selectedColor.setStrokeWidth(25);
        switch (colorName) {
            case ("Black"):
                selectedColor.setColor(Color.BLACK);
                return selectedColor;
            case ("Brown"):
                selectedColor.setColor(Color.parseColor("#654321"));
                return selectedColor;
            case ("Red"):
                selectedColor.setColor(Color.RED);
                return selectedColor;
            case ("Orange"):
                selectedColor.setColor(Color.parseColor("#FF8C00"));
                return selectedColor;
            case ("Yellow"):
                selectedColor.setColor(Color.parseColor("#FFFF00"));
                return selectedColor;
            case ("Green"):
                selectedColor.setColor(Color.GREEN);
                return selectedColor;
            case ("Blue"):
                selectedColor.setColor(Color.BLUE);
                return selectedColor;
            case ("Violet"):
                selectedColor.setColor(Color.parseColor("#9400D3"));
                return selectedColor;
            case ("Grey"):
                selectedColor.setColor(Color.GRAY);
                return selectedColor;
            case ("White"):
                selectedColor.setColor(Color.WHITE);
                return selectedColor;
            case ("Gold"):
                selectedColor.setColor(Color.parseColor("#FFD700"));
                return selectedColor;
            case ("Tan"):
                selectedColor.setColor(Color.rgb(222, 184, 135));
                selectedColor.setStrokeWidth(3);
                return selectedColor;
        }
        // Case in which we have a muck up with the colours. Signifys a dead band so a problem is found
        selectedColor.setColor(Color.rgb(222, 184, 135));
        return selectedColor;
    }
// Draws the resistors and bands at a fixed place on the screen.
    public Canvas drawResistorBody(Canvas canvas1,int xWidth,int yHeight){
        Paint p1;
        float x1,y1,width,height;
        final int xScale = xWidth;
        final int yScale = yHeight;

        p1=setCurrentColor("Black");
        p1.setStrokeWidth(20);

        x1=0;
        y1=0.46f*yScale;
        width = x1+(1f*xScale);
        height = y1+(.06f*yScale);
        canvas1.drawRect(x1,y1,width,height,p1);

        p1 =  setCurrentColor("Tan");;
        x1=.2f*xScale;
        y1=0.4f*yScale;
        width = x1 +(.6f*xScale);
        height = y1 +(.2f*yScale);
        canvas1.drawRect(x1,y1,width,height,p1);

        x1=.65f*xScale;
        y1=0.35f*yScale;
        width = x1 +(.2f*xScale);
        height = y1 +(.3f*yScale);

// reminder to use canvas1,drawRoundRect(rect,rx,ry,p1)     as this does same thing but was introduced ap1
        RectF rect = new RectF((int)x1,(int)y1,(int)width,(int)height);
        canvas1.drawRoundRect(rect,.1f*xScale,.5f*yScale,p1);

        x1=.15f*xScale;
        y1=0.35f*yScale;
        width = x1 +(.2f*xScale);
        height = y1 +(.3f*yScale);
// reminder to use canvas1,drawRoundRect(rect,rx,ry,p1)     as this does same thing but was introduced ap1
        rect = new RectF((int)x1,(int)y1,(int)width,(int)height);
        canvas1.drawRoundRect(rect,.1f*xScale,.5f*yScale,p1);
//Band1
        p1.setStrokeWidth(14);
        p1 = setCurrentColor(getBand1());
        x1=.25f*xScale;
        y1=0.35f*yScale;
        height = y1 +(.3f*yScale);
        canvas1.drawLine(x1,y1,x1,height,p1);

        //Band2
        p1.setStrokeWidth(14);
        p1 = setCurrentColor(getBand2());
        x1=0.4f*xScale;
        y1=0.4f*yScale;
        height = y1 +(.2f*yScale);
        canvas1.drawLine(x1,y1,x1,height,p1);
        //Band3
        p1.setStrokeWidth(14);
        p1 = setCurrentColor(getBand3());
        x1=0.5f*xScale;
        y1=0.4f*yScale;
        height = y1 +(.2f*yScale);
        canvas1.drawLine(x1,y1,x1,height,p1);
        //Band4
        p1.setStrokeWidth(14);
        p1 = setCurrentColor("Gold");
        x1=0.6f*xScale;
        y1=0.4f*yScale;
        height = y1 +(.2f*yScale);
        canvas1.drawLine(x1,y1,x1,height,p1);

       return canvas1;
    }
}
