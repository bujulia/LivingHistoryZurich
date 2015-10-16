package ch.ethz.ikg.gis.livinghistory;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_menu);

        //Initialize references to GUI elements
        kartenButton = (Button) findViewById(R.id.kartenButton);
        ebenenButton = (Button) findViewById(R.id.kartenButton);
        filterButton = (Button) findViewById(R.id.filterButton);
        tourenButton = (Button) findViewById(R.id.tourenButton);
        kartenOption = (RadioGroup) findViewById(R.id.kartenOption);

        kartenOption.setVisibility(View.INVISIBLE);



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

    //To be defined...
    private void triggerKartenButtonAction(){
        //kartenOption.setVisibility(View.VISIBLE);
    }

    //To be defined...
    private void triggerEbenenButtonAction(){
        //Nada
    }

    //To be defined...
    private void triggerFilterButtonAction(){
        Intent intent = new Intent(this,filterMenu.class);
        startActivity(intent);
    }

    //To be defined...
    private void triggerTourenButtonAction(){
        Intent intent = new Intent(this,tourenMenu.class);
        startActivity(intent);
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
