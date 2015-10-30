package com.example.gkonosc.gislab;

import android.app.Activity;
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

    // charis
    ArcGISFeatureLayer mFeatureLayer;
    GraphicsLayer mGraphicsLayer;
    String mFeatureServiceURL;
    MapView mMapView;
    //

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

        /*//Define what Tour1Button will do on a click
        Tour1Button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {triggerTour1ButtonAction(); }
        });*/
    }

    //Finishing the activity
    private void triggerZuruckButtonAction(){
        this.finish();
    }

    /*private void triggerTour1ButtonAction(){
        // Get the feature service URL from values->strings.xml
        mFeatureServiceURL = this.getResources().getString(R.string.featureServiceURL);
        // Add Feature layer to the MapView
        mFeatureLayer = new ArcGISFeatureLayer(mFeatureServiceURL, ArcGISFeatureLayer.MODE.ONDEMAND);
        mMapView.addLayer(mFeatureLayer);
        // Add Graphics layer to the MapView
        mGraphicsLayer = new GraphicsLayer();
        mMapView.addLayer(mGraphicsLayer);
        this.finish();
    }*/

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
