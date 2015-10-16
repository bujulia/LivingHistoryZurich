package ch.ethz.ikg.gis.livinghistory;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SearchView;


public class StartMenu extends Activity {

    //References to GUI elements
    private Button kartenButton;
    private Button ebenenButton;
    private Button filterButton;
    private Button tourenButton;
    private SearchView searchOption;
    private RadioGroup kartenOption;
    private LinearLayout ebenenOption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_menu);

        //Initialize references to GUI elements
        kartenButton = (Button) findViewById(R.id.kartenButton);
        ebenenButton = (Button) findViewById(R.id.ebenenButton);
        filterButton = (Button) findViewById(R.id.filterButton);
        tourenButton = (Button) findViewById(R.id.tourenButton);
        kartenOption = (RadioGroup) findViewById(R.id.kartenOption);
        ebenenOption = (LinearLayout) findViewById(R.id.ebenenOption);


        //Define what kartenButton will do on a click
        kartenButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                triggerKartenButtonAction();
            }
        });

        //Define what ebenenButton will do on a click
        ebenenButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                triggerEbenenButtonAction();
            }
        });

        //Define what filterButton will do on a click
        filterButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                triggerFilterButtonAction();
            }
        });

        //Define what tourenButton will do on a click
        tourenButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                triggerTourenButtonAction();
            }
        });
    }

    //Opens or closes the list of possible karten options
    private void triggerKartenButtonAction(){
        if (kartenOption.getVisibility() == View.VISIBLE){
            kartenOption.setVisibility(View.GONE);
        }
        else{
            ebenenOption.setVisibility(View.GONE);
            kartenOption.setVisibility(View.VISIBLE);
        }
    }

    //Opens or closes the list of possible ebenen options
    private void triggerEbenenButtonAction(){
        if (ebenenOption.getVisibility() == View.VISIBLE){
            ebenenOption.setVisibility(View.GONE);
        }
        else{
            kartenOption.setVisibility(View.GONE);
            ebenenOption.setVisibility(View.VISIBLE);
        }
    }

    //Creates a new intent -> opens the filter menu
    private void triggerFilterButtonAction(){
        Intent intent = new Intent(this,filterMenu.class);
        startActivity(intent);
    }

    //Creates a new intent -> opens the touren menu
    private void triggerTourenButtonAction(){
        Intent intent = new Intent(this,tourenMenu.class);
        startActivity(intent);
    }

    //Responds when a radio button is clicked, showing a basemap for the year of choice
    public void onRadioButtonClicked(View view){
        boolean checked = ((RadioButton) view).isChecked();

        switch (view.getId()){
            //Radio button for the basemap of today
            case R.id.radioAktuell:
                if (checked)
                    //Calls the basemap
                break;

            //Radio button for the basemap of 1970
            case R.id.radio1970:
                if (checked)
                    //Calls the basemap
                break;

            //Radio button for the basemap of 1900
            case R.id.radio1900:
                if (checked)
                    //Calls the basemap
                break;

            //Radio button for the basemap of 1860
            case R.id.radio1860:
                if (checked)
                    //Calls the basemap
                break;

            //Radio button for the basemap of 1793
            case R.id.radio1793:
                if (checked)
                    //Calls the basemap
                break;

        }
;    }

    //Responds when a click box is clicked, showing the different layers
    public void onCheckBoxClicked(View view) {
        boolean checked = ((CheckBox) view).isChecked();

        switch (view.getId()) {
            //Click box for Denkmalpflege of today
            case R.id.checkDenkm:
                if (checked)
                    Log.d("StartMenu", "Denk pa");
                else
                    Log.d("StartMenu", "Denk av");
                break;

            //Click box for Denkmalpflege of today
            case R.id.checkGarten:
                if (checked)
                    Log.d("StartMenu", "Garten");
                else
                    Log.d("StartMenu", "Garten av");
                break;

            //Radio button for the basemap of 1900
            case R.id.checkAussicht:
                if (checked)
                    Log.d("StartMenu", "aussicht");
                else
                    Log.d("StartMenu", "aussicht av");
                break;

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_start_menu, menu);
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
