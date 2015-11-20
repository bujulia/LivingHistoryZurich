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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SearchView;

import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISFeatureLayer;
import com.esri.android.map.ags.ArcGISLayerInfo;
import com.esri.android.map.ags.ArcGISPopupInfo;
import com.esri.android.map.ags.ArcGISTiledMapServiceLayer;
import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.android.map.ogc.WMSLayer;
import com.esri.android.map.popup.Popup;
import com.esri.android.map.popup.PopupContainer;
import com.esri.android.toolkit.map.MapViewHelper;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.FeatureSet;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.esri.core.tasks.ags.query.Query;
import com.esri.core.tasks.ags.query.QueryTask;
import com.esri.core.tasks.na.NAFeaturesAsFeature;
import com.esri.core.tasks.na.StopGraphic;



public class MainActivity extends Activity implements LocationListener{

public class MainActivity extends Activity implements LocationListener{

    MapView mMapView;
    public ArcGISFeatureLayer mFeatureLayer;
    GraphicsLayer mGraphicsLayer;
    boolean mIsMapLoaded;
    String mFeatureServiceURL;
    String mMapServiceURL;
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
    private Button stopTourButton;
    private RadioGroup kartenOption;
    private LinearLayout ebenenOption;

    private AutoCompleteTextView autoComplete;
    private ArrayAdapter<String> adapter;

    private LocationManager locationManager;
    public Location currentLocation;
    public GraphicsLayer graphicsLayer = new GraphicsLayer();

    public static int WKID_WGS84;

    private MapViewHelper mMapViewHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // after the content of this activity is set
        // the map can be accessed from the layout
        mMapView = (MapView)findViewById(R.id.map);

        createWMSURL("Uebersichtsplan");

        mMapView.addLayer(graphicsLayer);


        mMapView.addLayer(graphicsLayer);

        mMapView.setOnStatusChangedListener(new OnStatusChangedListener() {
            public void onStatusChanged(Object source, STATUS status) {
                if ((source == mMapView) && (status == OnStatusChangedListener.STATUS.INITIALIZED)) {
                    mIsMapLoaded = true;
                    mMapViewHelper = new MapViewHelper(mMapView);
                }
            }
        });

        //Initialize references to GUI elements
        kartenButton = (Button) findViewById(R.id.kartenButton);
        ebenenButton = (Button) findViewById(R.id.ebenenButton);
        filterButton = (Button) findViewById(R.id.filterButton);
        tourenButton = (Button) findViewById(R.id.tourenButton);
        stopTourButton = (Button) findViewById(R.id.stopTourButton);
        kartenOption = (RadioGroup) findViewById(R.id.kartenOption);
        ebenenOption = (LinearLayout) findViewById(R.id.ebenenOption);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 5, this);
        currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        Point myPoint = GeometryEngine.project(currentLocation.getLongitude(), currentLocation.getLatitude(), SpatialReference.create(102100));

        graphicsLayer.addGraphic(new Graphic(myPoint, new SimpleMarkerSymbol(Color.BLUE,10, SimpleMarkerSymbol.STYLE.CIRCLE)));


        Point myPoint = GeometryEngine.project(currentLocation.getLongitude(), currentLocation.getLatitude(), SpatialReference.create(102100));

        graphicsLayer.addGraphic(new Graphic(myPoint, new SimpleMarkerSymbol(Color.BLUE,10, SimpleMarkerSymbol.STYLE.CIRCLE)));
        /*

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

        //Define what stopTourButton will do on a click
        stopTourButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                triggerStopTourButtonAction();
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
        //myIntent.setAction(Intent.ACTION_VIEW);
        startActivityForResult(myIntent, selectedTour);
    }

    //Removes the tour from the map, needs editing
    private void triggerStopTourButtonAction(){
        stopTourButton.setVisibility(View.INVISIBLE);
        //call method that removes tour layer
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

        createWMSURL(visible);
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
                if (checked) {
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

    public void onLayerSelected2(String layerName){
        ArcGISTiledMapServiceLayer tiledLayer = new ArcGISTiledMapServiceLayer("http://tiles.arcgis.com/tiles/i9MtZ1vtgD3gTnyL/arcgis/rest/services/OGD_data/MapServer");
        mMapView.addLayer(tiledLayer);

        //********************************** popup part *************************************
        PopupContainer popupContainer = new PopupContainer(mMapView);
        Envelope env = tiledLayer.getFullExtent();
        ArcGISLayerInfo layerInfo = new ArcGISLayerInfo();
        SpatialReference sr = SpatialReference.create(WKID_WGS84);
        Popup popup;
        layerInfo.getLayers();

        int layerID=layerInfo.getId();
        String layerUrl = tiledLayer.getQueryUrl(layerID);
        if (layerUrl == null)
            layerUrl = tiledLayer.getUrl() + "/" + layerID;
        ArcGISPopupInfo popupInfo = tiledLayer.getPopupInfo(layerID);
        Query query = new Query();
        query.setInSpatialReference(sr);
        query.setOutSpatialReference(sr);
        query.setGeometry(env);
        query.setOutFields(new String[] {"*"});
        QueryTask queryTask = new QueryTask(layerUrl);
        try {
            FeatureSet results = queryTask.execute(query);
            for (Graphic graphic : results.getGraphics()) {
                popup = tiledLayer.createPopup(mMapView, layerID, graphic);
                popupContainer.addPopup(popup);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        PopupDialog popupDialog = new PopupDialog(mMapView.getContext(), popupContainer);
        popupDialog.show();
    }

    // method used to set a selected layer visible
    public void onLayerSelected(String layerName){
        String layerURL_feature = "URL_"+layerName; //this is how the layerURL looks like in the strings.xml
        int identifier = getStringIdentifier(this, layerURL_feature); //create an identifier to access the string from strings.xml with getString()
        mFeatureServiceURL = this.getResources().getString(identifier);
        // Add Feature layer to the MapView
        mFeatureLayer=createFeatureLayer(mFeatureServiceURL);
        mMapView.addLayer(mFeatureLayer);
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
        mFeatureLayer = new ArcGISFeatureLayer(featureServiceURL, ArcGISFeatureLayer.MODE.ONDEMAND);
        return mFeatureLayer;
    }

    //Method to create the WMS URL
    public void createWMSURL(String layer) {
        //Sets the WMS layer
        wmsURL = "http://www.gis.stadt-zuerich.ch/maps/services/wms/WMS-ZH-STZH-OGD/MapServer/WMSServer";
        wmsLayer = new WMSLayer(wmsURL);
        wmsLayer.setImageFormat("image/png");
        //Starts the WMS layer
        String[] visibleLayers = {layer};
        wmsLayer.setVisibleLayer(visibleLayers);
        mMapView.addLayer(wmsLayer);
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
        if(requestCode == selectedTour) {
            if (resultCode == RESULT_OK) {
                stopTourButton.setVisibility(View.VISIBLE);
                String myValue = data.getStringExtra("tour");
                onLayerSelected(myValue);
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {

        currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        graphicsLayer.removeAll();

        Point myPoint = GeometryEngine.project(currentLocation.getLongitude(), currentLocation.getLatitude(), SpatialReference.create(102100));

        graphicsLayer.addGraphic(new Graphic(myPoint, new SimpleMarkerSymbol(Color.BLUE,10, SimpleMarkerSymbol.STYLE.CIRCLE)));

        Point myPoint = GeometryEngine.project(currentLocation.getLongitude(), currentLocation.getLatitude(), SpatialReference.create(102100));

        graphicsLayer.addGraphic(new Graphic(myPoint, new SimpleMarkerSymbol(Color.BLUE,10, SimpleMarkerSymbol.STYLE.CIRCLE)));

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

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

