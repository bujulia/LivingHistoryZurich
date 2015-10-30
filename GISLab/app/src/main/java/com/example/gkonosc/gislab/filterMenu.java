package com.example.gkonosc.gislab;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;



public class filterMenu extends Activity {

    //References to GUI elements
    private Button anwendenButton;
    private Button zuruckButton;
    private EditText distInput;
    private EditText startInput;
    private EditText endInput;

    //To be added
    private ExpandableListView gartenList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filter_menu);

        //Initialize references to GUI elements
        anwendenButton = (Button) findViewById(R.id.anwendenButton);
        zuruckButton = (Button) findViewById(R.id.backButton);
        distInput = (EditText) findViewById(R.id.distInput);
        startInput = (EditText) findViewById(R.id.startInput);
        endInput = (EditText) findViewById(R.id.endInput);


        //Define what anwendenButton will do on a click
        anwendenButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                triggerAnwendenButtonAction();
            }
        });

        //Define what zuruckButton will do on a click
        zuruckButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {triggerZuruckButtonAction(); }
        });
    }

    //To be defined...
    private void triggerAnwendenButtonAction(){
        AlertDialog.Builder adb = new AlertDialog.Builder(this);

        //Read the content from the EditText elements
        String distString = distInput.getText().toString();
        String startString = startInput.getText().toString();
        String endString = endInput.getText().toString();

        //Tries to convert the content to integers
        try {
            Integer distInt = Integer.valueOf(distString);
            Integer startInt = Integer.valueOf(startString);
            Integer endInt = Integer.valueOf(endString);
        }catch (Exception e){
            AlertDialog ad = adb.create();
            ad.setMessage("Bitte geben Sie Zahlen, nicht Buchstaben");
            ad.show();
        }
    }

    //Finishing the activity
    private void triggerZuruckButtonAction(){
        this.finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_filter_menu, menu);
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
