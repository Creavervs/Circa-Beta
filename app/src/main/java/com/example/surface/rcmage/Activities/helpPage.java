package com.example.surface.rcmage.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.surface.rcmage.Class.DB_Handler;


public class helpPage extends AppCompatActivity {
    private DB_Handler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_page);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);


        // Spinner spinnerMenu = (Spinner)( MenuItemCompat.getActionView(item));

        //ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.menuItems, android.R.layout.simple_spinner_item);
        // adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //spinnerMenu.setAdapter(adapter);

        // set window theme i think for pop up backround i.e change colour or whatever
        // spinnerMenu.setPopupBackgroundResource(R.drawable.ic_action_name);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.item1:
                //startActivity(new Intent(MainActivity.this, newComponantSet.class));
                return true;
            case R.id.item2:
               // startActivity(new Intent(MainActivity.this, helpPage.class));
                return true;
            case R.id.item3:
               // startActivity(new Intent(MainActivity.this, helpPage.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

        //


    }

}
