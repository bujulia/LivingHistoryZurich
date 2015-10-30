package com.example.gkonosc.gislab;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISFeatureLayer;


public class tourenMenu extends Activity {

    //References to GUI elements
    private Button zuruckButton;
    private ImageButton Tour1Button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.touren_menu);

        //Initialize references to GUI elements
        zuruckButton = (Button) findViewById(R.id.backButton);
        Tour1Button = (ImageButton) findViewById(R.id.imageButton);

        //Define what zuruckButton will do on a click
        zuruckButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {triggerZuruckButtonAction(); }
        });

        //Define what Tour1Button will do on a click
        Tour1Button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {triggerTour1ButtonAction(); }
        });
    }

    //Finishing the activity
    private void triggerZuruckButtonAction(){
        this.finish();
    }

    private void triggerTour1ButtonAction(){
        setResult(1);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_touren_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
