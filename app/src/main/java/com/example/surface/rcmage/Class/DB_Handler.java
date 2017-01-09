package com.example.surface.rcmage.Class;

import android.content.ContentValues;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;


import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;




/**
 * Created by Surface on 29/11/2016.
 */

public class DB_Handler extends SQLiteOpenHelper {

    private int count;

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "RC";
    private static final String DATABASE_PATH = "/data/data/com.example.surface.rcmage/databases/";

    //Table Names
    private static final String Res = "RESISTOR";
    private static final String Cap = "CAPACITOR";


    // Table Columns names generic
    private static final String KEY_Type = "TYPE";
    private static final String KEY_Value = "VALUE";

    //Table Columns Resistor
    private static final String KEY_Bands = "BANDS";
    //Table Columns Capacitor
    private static final String KEY_Series = "SERIES";

    // Table Creation STRINGS
    private String CREATE_TABLE_RESISTOR = "CREATE TABLE " + Res + "(" + "ID INTEGER PRIMARY KEY ,"
            + KEY_Series + " TEXT,"
            + KEY_Value + " REAL ,"
            + KEY_Bands + " TEXT" + ");";

    private String CREATE_TABLE_CAPACITOR = "CREATE TABLE " + Cap + "(" + "ID INTEGER PRIMARY KEY ,"
            + KEY_Series + " TEXT,"
            + KEY_Value + " REAL ,"
            + KEY_Type + " TEXT" + ");";


    public DB_Handler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // creates the database by using the definition of sqlite database. Integers are used and does not consist of
        // any keys in otder to get out info. DB should work such that
        //               DB name = RC
        //
        //                    TABLE NAME RESISTOR (Res)
        //              _______________________________________
        //             |ID       |Series   |Value   |Bands    |
        //             |_________|_________|________|_________|

        // Bitmap images are byte arrays. need a way to pack/ unpack each time or designate a class to draw the resistors.

        // ID table never fills and relies on the database to make own numbers to assign primary key
        // Value table is the value of the resistor
        // Series is which drop down box it will belong to and will use E12,E24,Custom 1, Custom 2, Custom 3
        // Bands is the colour of resistor bands and will be as a string seperated by commas.

        //                      TABLE NAME CAPACITOR (Cap)

        //             |ID       |Series   |Value   |Type     |
        //             |_________|_________|________|_________|
        // ID table never fills and relies on the database to make own numbers to assign primary key
        // Value table is the value of the Capacitor
        // Series is which drop down box it will belong to and will use E6,E12,Custom 1, Custom 2, Custom 3
        // Type refers to the construction of the capacitor. e.g electrolytic, ceramic and will be used for tolerances.

// BIG NOTE:: EVERYTHING IN THE STRING HAS TO BE CAPITALS LIKE THIS OTHERWISE TABLES NOT CREATED//

        db.execSQL(CREATE_TABLE_CAPACITOR);
        db.execSQL(CREATE_TABLE_RESISTOR);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
// Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + Res);
        db.execSQL("DROP TABLE IF EXISTS " + Cap);
// Creating tables again
        onCreate(db);
    }

    public void insert_Row_Resistor(RCRecord r1, String name) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_Series, name);
        contentValues.put(KEY_Value, r1.getValue());
        String bands = r1.getBand1() + "," + r1.getBand2() + "," + r1.getBand3();
        contentValues.put(KEY_Bands, bands);
        this.getWritableDatabase().insertOrThrow(Res, "", contentValues);

    }

    public void insert_Row_Capacitor(capRecord c1, String name) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_Series, name);
        contentValues.put(KEY_Value, c1.getValue());
        contentValues.put(KEY_Type, c1.getType());
        this.getWritableDatabase().insertOrThrow(Cap, "", contentValues);
    }

    public Boolean contains(String tableName, String name) {
        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT * FROM " + tableName, null);
        while (cursor.moveToNext()) {
            if (cursor.getString(1).equals(name)) {
                cursor.close();
                return true;
            }
        }
        cursor.close();
        return false;
    }

    /**
     * Check if the database exist and can be read.
     *
     * @return true if it exists and can be read, false if it doesn't
     */

    public boolean isDbPresent() {
        boolean checkFlag = true;
        SQLiteDatabase testDb;
        String pathName = DATABASE_PATH + DATABASE_NAME;
        try {
            testDb = SQLiteDatabase.openDatabase(pathName, null,
                    SQLiteDatabase.OPEN_READWRITE);
        } catch (SQLiteException sqlException) {
            checkFlag = false;
        }
        return checkFlag;
    }


    public void delete_Entry(String value, String column, String tableName) {

        // needs work
        if (tableName.equals(Res)) {
            this.getWritableDatabase().delete(Res, column + "= '" + value + "'", null);
        } else if (tableName.equals(Cap)) {
            this.getWritableDatabase().delete(Cap, column + "= '" + value + "'", null);

        }
    }

    public void update_Entry(String old_Value, String new_Value, String column) {
        this.getWritableDatabase().execSQL("UPDATE STUDENTS SET " + column + "='" + new_Value + "'WHERE " + column + "='" + old_Value + "'");

    }

    public void list_all(TextView textView) {
        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT * FROM " + Cap, null);
        while (cursor.moveToNext()) {
            textView.append(cursor.getString(1) + cursor.getString(2) + cursor.getString(3));
        }
        cursor.close();
    }

    // Initial load for the first time running the app. Loads up the table defined in the assets for the resistor values
    public void LoadResistorTable(AssetManager am) {
        String fileName = "text.txt";
        try {
            BufferedReader buffer = new BufferedReader(
                    new InputStreamReader(am.open(fileName)));
            String line;

            try {
                while ((line = buffer.readLine()) != null) {
                    String[] columns = line.split(",");
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(KEY_Series, columns[0].trim());
                    contentValues.put(KEY_Value, columns[1].trim());
                    String colorValue = columns[2].trim() + "," + columns[3].trim() + "," + columns[4].trim();
                    contentValues.put(KEY_Bands, colorValue);
                    this.getWritableDatabase().insertWithOnConflict(Res, "", contentValues, SQLiteDatabase.CONFLICT_REPLACE);
                }
            } catch (IOException e) {
                //textview.append(e.toString());
            }

        } catch (IOException e) {
            // textview.append(e.toString());
        }

    }

    // Initial load for the first time running the app. Loads up the table defined in the assets for the Capacitor values
    public void LoadCapacitorTable(AssetManager am) {
        String fileName = "text1.txt";
        try {
            BufferedReader buffer = new BufferedReader(
                    new InputStreamReader(am.open(fileName)));
            String line;

            try {
                while ((line = buffer.readLine()) != null) {
                    String[] columns = line.split(",");
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(KEY_Series, columns[0].trim());
                    contentValues.put(KEY_Value, columns[1].trim());
                    contentValues.put(KEY_Type, columns[2].trim());
                    this.getWritableDatabase().insertWithOnConflict(Cap, "", contentValues, SQLiteDatabase.CONFLICT_REPLACE);
                }
            } catch (IOException e) {
                //textview.append(e.toString());
            }

        } catch (IOException e) {
            //textview.append(e.toString());
        }
    }

    /**
     * Provides a way to set the current arraylist to be used for doing calculations for the resistors
     **/

    public List<RCRecord> SetLocalResistor(String selected) {
        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT * FROM " + Res, null);
        List<RCRecord> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            if (cursor.getString(1).equals(selected)) {
                double value = cursor.getDouble(2);
                String[] columns = cursor.getString(3).split(",");
                String band1 = columns[0].trim();
                String band2 = columns[1].trim();
                String band3 = columns[2].trim();
                RCRecord rc = new RCRecord(value, band1, band2, band3);
                list.add(rc);
            }
        }
        cursor.close();
        Collections.sort(list, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                RCRecord p1 = (RCRecord) o1;
                RCRecord p2 = (RCRecord) o2;
                return p1.getValue().compareTo(p2.getValue());
            }
        });
        return list;

    }

    /**
     * Provides a way to set the current arraylist to be used for doing calculations for the Capacitors
     **/


    public List<capRecord> SetLocalCapacitor(String selected) {
        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT * FROM " + Cap, null);
        List<capRecord> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            if (cursor.getString(1).equals(selected)) {
                double value = cursor.getDouble(2);
                String type = cursor.getString(3);
                capRecord cr = new capRecord(value, type);
                list.add(cr);
            }
        }
        cursor.close();
        Collections.sort(list, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                capRecord p1 = (capRecord) o1;
                capRecord p2 = (capRecord) o2;
                return p1.getValue().compareTo(p2.getValue());
            }
        });
        return list;

    }

    public List<String> getSetFromDataBase(String tableName) {
        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT * FROM " + tableName, null);
        List<String> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            String entry = cursor.getString(1);
            if (!list.contains(entry)) {
                list.add(entry);
            }
        }
        cursor.close();
        return list;
    }

    public RCRecord getBandColourFromDatabase(String colour) {
        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT * FROM " + Res, null);
        List<String> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            if (cursor.getString(3).equals(colour)) {
                String entry = cursor.getString(1);
                double value = cursor.getDouble(2);
                String[] columns = cursor.getString(3).split(",");
                String band1 = columns[0].trim();
                band1= band1.replace(",","");
                String band2 = columns[1].trim();
                band2= band2.replace(",","");
                String band3 = columns[2].trim();
                RCRecord rc = new RCRecord(value, band1, band2, band3);
                return rc;
            }
        }
        return null;
    }

    /**
     * This method is called when new is selected. What we want to do is find the biggest increment
     * of values to be used i.e E24 series and display them to the user so that he can make his new
     * set of componants. this fills a local arraylist with all the values.
     */
    public List<itemNewComp> getitemNewComp(String tableName) {
        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT * FROM " + tableName, null);
        List<itemNewComp> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            String entry = cursor.getString(1);

            if (entry.equals("E24 Resistor")) {
                if (tableName.equals("Resistor")) {
                    double value = cursor.getDouble(2);
                    String[] columns = cursor.getString(3).split(",");
                    String band1 = columns[0].trim();
                    String band2 = columns[1].trim();
                    String band3 = columns[2].trim();
                    itemNewComp ir = new itemNewComp(value, 0.0, "", band1, band2, band3, false);
                    list.add(ir);
                }
                    else {
                        // need to log something here dunno how to do
                    }
                }
            if (entry.equals("E24 Capacitor")) {
                if (tableName.equals("Capacitor")) {
                    double value = cursor.getDouble(2);
                    String type = cursor.getString(3);
                    itemNewComp ir = new itemNewComp(0.0, value, type, "", "", "", false);
                    list.add(ir);
                } else {
                    // need to log something here dunno how to do
                }
            }
        }
        cursor.close();
        return list;
    }

    public List<Double> SetLocalValueArrayList(String Tablename, String selected) {
        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT * FROM " + Tablename, null);
        List<Double> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            if (cursor.getString(1).equals(selected)) {
                double value = cursor.getDouble(2);
                list.add(value);
            }
        }

        cursor.close();

        return list;
    }

    public void loadDbFromItemNewCompResistor(String currentName, List<itemNewComp> currentItemList) {
        for (itemNewComp item : currentItemList) {
            if(!containsValue(Cap,currentName,item.getCapacitor())) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(KEY_Series, currentName);
                contentValues.put(KEY_Value, item.getResistor());
                String bands = item.getBand1() + "," + item.getBand2() + "," + item.getBand3();
                contentValues.put(KEY_Bands, bands);
                this.getWritableDatabase().insertOrThrow(Res, "", contentValues);
            }
        }
    }

    public void loadDbFromItemNewCompCapacitor(String currentName, List<itemNewComp> currentItemList) {
        for (itemNewComp item : currentItemList) {
           if(!containsValue(Res,currentName,item.getResistor())){

                ContentValues contentValues = new ContentValues();
                contentValues.put(KEY_Series, currentName);
                contentValues.put(KEY_Value, item.getCapacitor());
                contentValues.put(KEY_Type, item.getType());
                this.getWritableDatabase().insertOrThrow(Cap,  "", contentValues);
            }

        }

    }
    public Boolean containsValue(String tableName, String name,Double value) {
        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT * FROM " + tableName, null);
        while (cursor.moveToNext()) {
            if (cursor.getString(1).equals(name)) {
                if (cursor.getDouble(2) == value) {
                    cursor.close();
                    return true;
                }
            }
        }
        cursor.close();
        return false;
    }

    // This method will select the second band color and return an array based off the choice of the user in the first band.
    //The idea is that will not have doubleups so need to run a contains() method to ensure incorrect adding of strings
    public List<String> SetLocalColorband2ArrayList( String selected, String keySet) {
        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT * FROM " + Res, null);
        List<String> list = new ArrayList<>();
        selected=selected.replace(",","");
        while (cursor.moveToNext()) {
            if(cursor.getString(1).equals(keySet)) {
                String[] columns = cursor.getString(3).split(",");
                String band1 = columns[0].trim();
                band1 = band1.replace(",","");
                if (band1.equals(selected)) {
                    String band2 = columns[1].trim();
                    band2 = band2.replace(",","");
                    if (!list.contains(band2)) {
                        list.add(band2);
                    }

                }
            }
        }

        cursor.close();

        return list;
    }
}





