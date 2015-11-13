package com.example.gkonosc.gislab;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISFeatureLayer;
import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.android.map.ogc.WMSLayer;
import com.esri.core.geometry.CoordinateConversion;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.CallbackListener;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.MarkerSymbol;
import com.esri.core.symbol.SimpleLineSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.esri.core.symbol.Symbol;
import com.esri.core.tasks.identify.IdentifyParameters;
import com.esri.core.tasks.identify.IdentifyResult;
import com.esri.core.tasks.identify.IdentifyTask;
import com.esri.core.tasks.na.NAFeaturesAsFeature;
import com.esri.core.tasks.na.StopGraphic;

import java.util.Timer;


public class MainActivity extends Activity implements LocationListener{

    MapView mMapView;
    public ArcGISFeatureLayer mFeatureLayer;
    GraphicsLayer mGraphicsLayer;
    boolean mIsMapLoaded;
    String mFeatureServiceURL;
    WMSLayer wmsLayer;
    String wmsURL;
    public String visible;
    private WMSLayer oldWMS;
    final static int selectedTour = 1234;

    //References to GUI elements
    private Button kartenButton;
    private Button ebenenButton;
    private Button filterButton;
    private Button tourenButton;
    private SearchView searchOption;
    private RadioGroup kartenOption;
    private LinearLayout ebenenOption;

    private AutoCompleteTextView autoComplete;
    private ArrayAdapter<String> adapter;

    private LocationManager locationManager;
    public Location currentLocation;
    public GraphicsLayer graphicsLayer = new GraphicsLayer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // after the content of this activity is set
        // the map can be accessed from the layout
        mMapView = (MapView)findViewById(R.id.map);



        // set up the wms url
        wmsURL = "http://www.gis.stadt-zuerich.ch/maps/services/wms/WMS-ZH-STZH-OGD/MapServer/WMSServer";
        wmsLayer = new WMSLayer(wmsURL);
        wmsLayer.setImageFormat("image/png");
        // starting wms layer
        String[] visibleLayers = {"Uebersichtsplan"};
        wmsLayer.setVisibleLayer(visibleLayers);
        mMapView.addLayer(wmsLayer);


        mMapView.addLayer(graphicsLayer);

        mMapView.setOnStatusChangedListener(new OnStatusChangedListener() {
            public void onStatusChanged(Object source, STATUS status) {
                if ((source == mMapView) && (status == OnStatusChangedListener.STATUS.INITIALIZED)) {
                    mIsMapLoaded = true;
                }
            }
        });

        //Initialize references to GUI elements
        kartenButton = (Button) findViewById(R.id.kartenButton);
        ebenenButton = (Button) findViewById(R.id.ebenenButton);
        filterButton = (Button) findViewById(R.id.filterButton);
        tourenButton = (Button) findViewById(R.id.tourenButton);
        kartenOption = (RadioGroup) findViewById(R.id.kartenOption);
        ebenenOption = (LinearLayout) findViewById(R.id.ebenenOption);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 5, this);
        currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        Point myPoint = GeometryEngine.project(currentLocation.getLongitude(), currentLocation.getLatitude(), SpatialReference.create(102100));

        graphicsLayer.addGraphic(new Graphic(myPoint, new SimpleMarkerSymbol(Color.BLUE,10, SimpleMarkerSymbol.STYLE.CIRCLE)));


        //************************************** Autocomplete ***********************************//

        // get the defined string-array
        final String[] colors = getResources().getStringArray(R.array.colorList);
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,colors);
        autoComplete = (AutoCompleteTextView) findViewById(R.id.autoComplete);
        // set adapter for the auto complete fields
        autoComplete.setAdapter(adapter);
        // specify the minimum type of characters before drop-down list is shown
        autoComplete.setThreshold(1);
        autoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // onObjectZoom();   <-- Define a function that is called when the user clicks on an item from the dropdown
                String itemString = parent.getItemAtPosition(position).toString();
                Toast toast = Toast.makeText(getApplicationContext(), itemString, Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        /*
        // Trying something with identify, not working
        mFeatureServiceURL = this.getResources().getString(R.string.URL_Garten);
        mFeatureLayer = new ArcGISFeatureLayer(mFeatureServiceURL, ArcGISFeatureLayer.MODE.ONDEMAND);

        IdentifyTask identifyTask = new IdentifyTask(mFeatureServiceURL);
        IdentifyParameters identifyparam = new IdentifyParameters();
        identifyparam.setTolerance(10);

        identifyTask.execute(identifyparam, new CallbackListener<IdentifyResult[]>() {

            @Override
            public void onError(Throwable e) {
                // handle/display error as desired
            }

            @Override
            public void onCallback(IdentifyResult[] identifyResults) {
                // go through the returned result array
                for (int i = 0; i < identifyResults.length; i++) {
                    IdentifyResult result = identifyResults[i];
                    String resultString =
                            result.getAttributes().get(result.getDisplayFieldName())
                                    + " (" + result.getLayerName() + ")";
                }
            }
        }); */
        //************************************** Autocomplete ***********************************//


        //Define what kartenButton will do on a click
        kartenButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                triggerKartenButtonAction();
            }
        });

        //Define what ebenenButton will do on a click
        ebenenButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                triggerEbenenButtonAction();
            }
        });

        //Define what filterButton will do on a click
        filterButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                triggerFilterButtonAction();
            }
        });

        //Define what tourenButton will do on a click
        tourenButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                triggerTourenButtonAction();
            }
        });
    }

    //Opens or closes the list of possible Karten options
    private void triggerKartenButtonAction(){
        if (kartenOption.getVisibility() == View.VISIBLE){
            kartenOption.setVisibility(View.GONE);
        }
        else{
            ebenenOption.setVisibility(View.GONE);
            kartenOption.setVisibility(View.VISIBLE);
        }
    }

    //Opens or closes the list of possible Ebenen options
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
        Intent myIntent = new Intent(getBaseContext(),tourenMenu.class);
        myIntent.setAction(Intent.ACTION_VIEW);
        startActivityForResult(myIntent, selectedTour);
    }

    //Responds when a radio button is clicked, showing a basemap for the year of choice
    public void onRadioButtonClicked(View view){
        boolean checked = ((RadioButton) view).isChecked();
        oldWMS = wmsLayer;

        switch (view.getId()){
            //Radio button for the basemap of today
            case R.id.radioAktuell:
                if (checked)
                    visible = "Uebersichtsplan";
                    break;

            //Radio button for the basemap of 1970
            case R.id.radio1970:
                if (checked)
                    visible = "Uebersichtsplan_1970";
                    break;

            //Radio button for the basemap of 1900
            case R.id.radio1900:
                if (checked)
                    visible = "Stadtplan_1900";
                    break;

            //Radio button for the basemap of 1860
            case R.id.radio1860:
                if (checked)
                    visible = "Stadtplan_1860";
                    break;

            //Radio button for the basemap of 1793
            case R.id.radio1793:
                if (checked)
                    visible = "Stadtplan_1793";
                    break;
        }

        // set up the wms url
        wmsURL = "http://www.gis.stadt-zuerich.ch/maps/services/wms/WMS-ZH-STZH-OGD/MapServer/WMSServer";
        wmsLayer = new WMSLayer(wmsURL);
        wmsLayer.setImageFormat("image/png");
        String[] newVisibleLayer = {visible};
        wmsLayer.setVisibleLayer(newVisibleLayer);
        mMapView.addLayer(wmsLayer);
        mMapView.addLayer(graphicsLayer);
        mMapView.removeLayer(oldWMS);
    }

    //Responds when a click box is clicked, showing the different layers
    public void onCheckBoxClicked(View view) {
        boolean checked = ((CheckBox) view).isChecked();

        switch (view.getId()) {
            //Click box for Denkmalpflege of today
            case R.id.checkDenkm:
                if (checked){
                    onLayerSelected("Denkm");
                    }
                else{
                    Log.d("StartMenu", "denkm off");
                    onLayerDeselected();}
                break;

            //Click box for Denkmalpflege of today
            case R.id.checkGarten:
                if (checked){
                    onLayerSelected("Garten");}
                else{
                    Log.d("StartMenu", "garten off");
                    onLayerDeselected();}
                break;

            //Click box for Aussicht of today
            case R.id.checkAussicht:
                if (checked){
                    onLayerSelected("Aussicht");}
                else{
                    Log.d("StartMenu", "aussicht off");
                    onLayerDeselected();}
                break;

        }
    }

    // method used to set a selected layer visible
    public void onLayerSelected(String layerName){
        String layerURL = "URL_"+layerName; //this is how the layerURL looks like in the strings.xml
        int identifier = getStringIdentifier(this, layerURL); //create an identifier to access the string from strings.xml with getString()
        mFeatureServiceURL = this.getResources().getString(identifier);
        // Add Feature layer to the MapView
        mFeatureLayer=createFeatureLayer(mFeatureServiceURL);
        mMapView.addLayer(mFeatureLayer);
        // Add Graphics layer to the MapView
        mGraphicsLayer = new GraphicsLayer();
        mMapView.addLayer(mGraphicsLayer);
        mMapView.addLayer(graphicsLayer);
    }

    // method used to remove a layer which isn't selected <-- needs to be defined properly is not working yet!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    public void onLayerDeselected(){
        //mMapView.removeLayer(mGraphicsLayer);
    }

    // method to create a StringIdentifier to access the strings.xml file
    public static int getStringIdentifier(Context context, String name) {
        return context.getResources().getIdentifier(name, "string", context.getPackageName());
    }

    // method to get create a FeatureLayer
    public ArcGISFeatureLayer createFeatureLayer(String featureServiceURL){
        mFeatureLayer = new ArcGISFeatureLayer(mFeatureServiceURL, ArcGISFeatureLayer.MODE.ONDEMAND);
        return mFeatureLayer;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == selectedTour) {
        // Get the feature service URL from values->strings.xml
        mFeatureServiceURL = this.getResources().getString(R.string.URL_tour01_route);
        // Add Feature layer to the MapView
        mFeatureLayer = new ArcGISFeatureLayer(mFeatureServiceURL, ArcGISFeatureLayer.MODE.ONDEMAND);
        mMapView.addLayer(mFeatureLayer);
        // Add Graphics layer to the MapView
        mGraphicsLayer = new GraphicsLayer();
        mMapView.addLayer(mGraphicsLayer);
        mMapView.addLayer(graphicsLayer);
        }
    }

    @Override
    public void onLocationChanged(Location location) {

        currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        graphicsLayer.removeAll();

        Point myPoint = GeometryEngine.project(currentLocation.getLongitude(), currentLocation.getLatitude(), SpatialReference.create(102100));

        graphicsLayer.addGraphic(new Graphic(myPoint, new SimpleMarkerSymbol(Color.BLUE,10, SimpleMarkerSymbol.STYLE.CIRCLE)));

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}

