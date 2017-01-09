package com.example.surface.rcmage.Class;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

/**
 * Created by Surface on 9/12/2016.
 */



    public class ResistorDrawer extends View {
        Paint paint = new Paint();

        public ResistorDrawer(Context context) {
            super(context);
        }

        @Override
        public void onDraw(Canvas canvas) {
            paint.setColor(Color.BLACK);
            paint.setStrokeWidth(3);
            canvas.drawRect(30, 30, 80, 6, paint);
            paint.setStrokeWidth(0);
            paint.setColor(Color.YELLOW);
            canvas.drawRect(33, 60, 77, 77, paint );
            paint.setColor(Color.YELLOW);
            canvas.drawRect(33, 33, 77, 60, paint );

        }

    }



