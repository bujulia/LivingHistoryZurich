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
            public void onClick(View v) {
                triggerZuruckButtonAction();
            }
        });
    }

    //To be defined...
    private void triggerAnwendenButtonAction(){
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        AlertDialog ad = adb.create();

        distanceInput(ad);
        yearInput(ad);

    }

    //Retrieves the search distance and tries to catches exceptions
    private void distanceInput(AlertDialog ad) {
        //Reads the content from the EditText elements
        String distString = distInput.getText().toString();
        //Checks if the distance is an empty string, if yes nothing is done
        if (distString.matches("")) {
            //Ignore
        }
        else{
            //Tries to convert the distance to integers and checks if the distance if more than 1 meter
            try {
                Integer distInt = Integer.valueOf(distString);
                if (distInt < 1){
                    ad.setMessage(getResources().getString(R.string.distanz_groesser));
                    ad.show();
                }
                else{
                    filterDistance(distInt);
                }
            } catch (Exception e) {
                ad.setMessage(getResources().getString(R.string.zahlen_angeben));
                ad.show();
            }
        }
    };

    //Retrieves the years and tries to catches exceptions
    //Can be improved so it won't be that much of code repetition...
    private void yearInput(AlertDialog ad){
        String startString = startInput.getText().toString();
        String endString = endInput.getText().toString();
        //Checks if the start year and the end year are empty strings, if yes nothing is done
        if (startString.matches("") && endString.matches("")) {
            //Ignore
        }
        else {
            //Checks if the start year if an empty string, if yes the start year is set to the year 1000
            if (startString.matches("")) {
                try {
                    Integer startInt = 1000;
                    Integer endInt = Integer.valueOf(endString);
                    if (endInt < 0){
                        ad.setMessage(getResources().getString(R.string.jahr_groesser));
                        ad.show();
                    }
                    else{
                        filterYears(startInt,endInt);
                    }
                } catch (Exception e) {
                    ad.setMessage(getResources().getString(R.string.zahlen_angeben));
                    ad.show();
                }
            }
            //Checks if the end year if an empty string, if yes the end year is set to the year 2015
            else if (endString.matches("")) {
                try {
                    Integer startInt = Integer.valueOf(startString);
                    Integer endInt = 2015;
                    if (startInt < 0){
                        ad.setMessage(getResources().getString(R.string.jahr_groesser));
                        ad.show();
                    }
                    else{
                        filterYears(startInt,endInt);
                    }
                } catch (Exception e) {
                    ad.setMessage(getResources().getString(R.string.zahlen_angeben));
                    ad.show();
                }
            }
            else {
                try {
                    Integer startInt = Integer.valueOf(startString);
                    Integer endInt = Integer.valueOf(endString);
                    if (startInt < 0 || endInt < 0){
                        ad.setMessage(getResources().getString(R.string.jahr_groesser));
                        ad.show();
                    }
                    else{
                        filterYears(startInt,endInt);
                    }
                } catch (Exception e) {
                    ad.setMessage(getResources().getString(R.string.zahlen_angeben));
                    ad.show();
                }
            }
        }
    };

    //Filters the distance for the layers
    private void filterDistance(Integer distance){
        //To be fixed
        //this.finish();
    }

    //Filters Denkmalobjekte layer with the year
    private void filterYears(Integer start, Integer end){
        //To be fixed
        //this.finish();
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
