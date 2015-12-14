package com.example.gkonosc.gislab;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.Layer;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISFeatureLayer;
import com.esri.android.map.event.OnSingleTapListener;
import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.android.map.ogc.WMSLayer;
import com.esri.android.runtime.ArcGISRuntime;
import com.esri.android.toolkit.map.MapViewHelper;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.SimpleMarkerSymbol;


public class MainActivity extends Activity implements LocationListener{

    MapView mMapView;
    private Graphic mIdentifiedGraphic;
    public ArcGISFeatureLayer mFeatureLayer;
    public ArcGISFeatureLayer mFeatureLayerDenkm;
    public ArcGISFeatureLayer mFeatureLayerGarten;
    public ArcGISFeatureLayer mFeatureLayerAussicht;
    public ArcGISFeatureLayer mFeatureLayerStops;
    public ArcGISFeatureLayer mFeatureLayerRoute;

    boolean mIsMapLoaded;
    String mFeatureServiceURL;
    WMSLayer wmsLayer;
    String wmsURL;
    public String visible;
    private WMSLayer oldWMS;
    final static int selectedTour = 1234;
    public int denkNo = 0;
    public int gartenNo = 0;
    public int aussichtNo = 0;
    public int stopsNo = 0;
    public int routeNo = 0;
    Point myPoint;
    public String destination;

    //References to GUI elements
    private Button kartenButton;
    private Button ebenenButton;
    private Button filterButton;
    private Button tourenButton;
    private Button stopTourButton;
    private RadioGroup kartenOption;
    private LinearLayout ebenenOption;

    private LocationManager locationManager;
    public Location currentLocation;
    public GraphicsLayer graphicsLayer = new GraphicsLayer();

    private MapViewHelper mMapViewHelper;




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArcGISRuntime.setClientId(getResources().getString(R.string.myClientID));

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

        // Set a tap listener on the map. -------------------------------------------------
        mMapView.setOnSingleTapListener(new OnSingleTapListener() {
            @Override
            public void onSingleTap(float x, float y) {
                searchForFeature(x, y);

                Log.d("tag", "Found graphic: " + mIdentifiedGraphic);

                if (mIdentifiedGraphic != null) {
                    showPopup(mIdentifiedGraphic);
                    if (kartenOption.getVisibility() == View.VISIBLE) {
                        kartenOption.setVisibility(View.GONE); }

                    else if (ebenenOption.getVisibility() == View.VISIBLE) {
                        ebenenOption.setVisibility(View.GONE);  }
                }

            }
        });
        // --------------------------------------------------------------------------------

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 5, this);
        currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        myPoint = GeometryEngine.project(currentLocation.getLongitude(), currentLocation.getLatitude(), SpatialReference.create(102100));

        graphicsLayer.addGraphic(new Graphic(myPoint, new SimpleMarkerSymbol(Color.BLUE,10, SimpleMarkerSymbol.STYLE.CIRCLE)));

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

    // A bunch of methods to determine if the thing a user clicked on is a feature
    // and show a popup, if so. -------------------------------------------------------
    // See https://geonet.esri.com/thread/77290.
    private void showPopup(Graphic feature) {
        final PopupWindow popUp = new PopupWindow(this);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        popUp.setContentView(inflater.inflate(R.layout.popup_feature, null, false));
        popUp.showAtLocation(findViewById(R.id.map), Gravity.FILL_HORIZONTAL, 0, 100);
        popUp.update(50, 50, 650, 500);

        TextView v = (TextView) popUp.getContentView().findViewById(R.id.textView3);
        Button b = (Button) popUp.getContentView().findViewById(R.id.btnRoute);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                destination = ((Point)mIdentifiedGraphic.getGeometry()).getX() + "," + ((Point)mIdentifiedGraphic.getGeometry()).getY();
                Log.d("POI coordinates: ", destination);

                // Routing task starts here
                Intent intent = new Intent(MainActivity.this, Routing.class);
                intent.putExtra("destination", destination);
                startActivity(intent);
                new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);

                popUp.dismiss();
            }
        });
        Log.d("tags", feature.getAttributes().toString());
        if (feature.getAttributeValue("OBJEKTBEZE") != null) {
            v.setText(" Denkmalpflegeinventarobjekt \n Bezeichnung: " + feature.getAttributeValue("OBJEKTBEZE").toString()+
                    "\n N\u00e4here Bezeichnung: "+ feature.getAttributeValue("NAEHEREBEZ").toString()+
                    "\n Baujahr: "+ feature.getAttributeValue("BAUJAHR").toString());

        }
        else if (feature.getAttributeValue("NAME")!= null) {
            v.setText(" Aussichtspunkt \n Name: " + feature.getAttributeValue("NAME").toString());
        }
        else if (feature.getAttributeValue("BEZEICHNUN")!= null) {
            v.setText(" Gartendenkmal \n Bezeichnung: " + feature.getAttributeValue("BEZEICHNUN").toString()+
                    "\n Typ: " + feature.getAttributeValue("TYP").toString());
        }
        else if (feature.getAttributeValue("Info")!= null) {
            v.setText(" Tour \n Halt Nummer " + feature.getAttributeValue("Nummer").toString()+
                    "\n Name: " + feature.getAttributeValue("Name").toString()+
                    "\n Info: " + feature.getAttributeValue("Info").toString());
        }
        else {
            v.setText("Kein Objekt gefunden");
        }

    }


    private void searchForFeature(float x, float y) {

        Point mapPoint = mMapView.toMapPoint(x, y);

        if (mapPoint != null) {

            for (Layer layer : mMapView.getLayers()) {
                Log.d("layer", "Checking layer: " + layer);
                if (layer == null)
                    continue;

                if (layer instanceof ArcGISFeatureLayer) {
                    Log.d("layer", "Layer is a feature layer: " + layer);
                    ArcGISFeatureLayer fLayer = (ArcGISFeatureLayer) layer;
                    // Get the Graphic at location x,y
                    mIdentifiedGraphic = getFeature(fLayer, x, y);
                } else
                    continue;
            }
        }
    }

    private Graphic getFeature(ArcGISFeatureLayer fLayer, float x, float y) {

        // Get the graphics near the Point.
        int[] ids = fLayer.getGraphicIDs(x, y, 10, 1);
        if (ids == null || ids.length == 0) {
            return null;
        }
        Graphic g = fLayer.getGraphic(ids[0]);
        return g;
    }
    // -------------------------------------------------------------------------------

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
        if (stopsNo==1){
        deselectLayer(mFeatureLayerRoute);
        deselectLayer(mFeatureLayerStops);
            stopsNo = 0;
        routeNo = 0;}
    }

    //Removes the tour from the map, needs editing
    private void triggerStopTourButtonAction(){
        stopTourButton.setVisibility(View.INVISIBLE);
        deselectLayer(mFeatureLayerRoute);
        deselectLayer(mFeatureLayerStops);
        stopsNo = 0;
        routeNo = 0;
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
        layerOrder();

        mMapView.removeLayer(oldWMS);
    }

    //Reorders the layers
    public void layerOrder(){
        if (gartenNo == 1){
            deselectLayer(mFeatureLayerGarten);
            layerSelected("Garten");
        }
        if (routeNo == 1){
            deselectLayer(mFeatureLayerRoute);
            layerSelected("tour01_route");
        }
        if (stopsNo == 1){
            deselectLayer(mFeatureLayerStops);
            layerSelected("tour01_stops");
        }
        if (denkNo == 1){
            deselectLayer(mFeatureLayerDenkm);
            layerSelected("Denkm");
        }
        if (aussichtNo == 1){
            deselectLayer(mFeatureLayerAussicht);
            layerSelected("Aussicht");
        }
    }

    //Responds when a click box is clicked, showing the different layers
    public void onCheckBoxClicked(View view) {
        boolean checked = ((CheckBox) view).isChecked();

        switch (view.getId()) {
            //Click box for Denkmalpflege of today
            case R.id.checkDenkm:
                if (checked){
                    layerSelected("Denkm");
                    denkNo = 1;
                    layerOrder();
                }
                else{
                    deselectLayer(mFeatureLayerDenkm);
                    denkNo = 0;
                }
                break;

            //Click box for Denkmalpflege of today
            case R.id.checkGarten:
                if (checked) {
                    layerSelected("Garten");
                    gartenNo = 1;
                    layerOrder();
                }
                else{
                    deselectLayer(mFeatureLayerGarten);
                    gartenNo = 0;
                }
                break;

            //Click box for Aussicht of today
            case R.id.checkAussicht:
                if (checked){
                    layerSelected("Aussicht");
                    aussichtNo = 1;
                    layerOrder();
                }
                else{
                    deselectLayer(mFeatureLayerAussicht);
                    aussichtNo = 0;
                }
                break;

        }
    }

    public void layerSelected(String layerName){
        mFeatureServiceURL = getStringURL(layerName, "URL_");
        if (layerName.equals("Denkm")){
            mFeatureLayerDenkm=createFeatureLayer(mFeatureServiceURL);
            mMapView.addLayer(mFeatureLayerDenkm);
        }
        if (layerName.equals("Aussicht")){
            mFeatureLayerAussicht=createFeatureLayer(mFeatureServiceURL);
            mMapView.addLayer(mFeatureLayerAussicht);
        }
        if (layerName.equals("Garten")){
            mFeatureLayerGarten = createFeatureLayer(mFeatureServiceURL);
            mMapView.addLayer(mFeatureLayerGarten);
        }
        if (layerName.equals("tour01_route")){
            mFeatureLayerRoute=createFeatureLayer(mFeatureServiceURL);
            mMapView.addLayer(mFeatureLayerRoute);
        }
        if (layerName.equals("tour01_stops")){
            mFeatureLayerStops=createFeatureLayer(mFeatureServiceURL);
            mMapView.addLayer(mFeatureLayerStops);
        }
        mMapView.addLayer(graphicsLayer);
    }

    public void deselectLayer(ArcGISFeatureLayer layer){
        mMapView.removeLayer(layer);
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

    public String getStringURL (String layerName, String layerType) {
        String layerURL = layerType+ layerName; //this is how the layerURL looks like in the strings.xml
        int identifier = getStringIdentifier(this, layerURL); //create an identifier to access the string from strings.xml with getString()
        String ServiceURL = this.getResources().getString(identifier);
        return ServiceURL;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == selectedTour) {
            if (resultCode == RESULT_OK) {
                stopTourButton.setVisibility(View.VISIBLE);
                String myValue = data.getStringExtra("tour");
                String route=myValue+"_route";
                String stops=myValue+"_stops";
                layerSelected(route);
                layerSelected(stops);
                stopsNo = 1;
                routeNo = 1;
                layerOrder();
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {

        currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        graphicsLayer.removeAll();

        Point myPoint = GeometryEngine.project(currentLocation.getLongitude(), currentLocation.getLatitude(), SpatialReference.create(102100));

        graphicsLayer.addGraphic(new Graphic(myPoint, new SimpleMarkerSymbol(Color.GREEN,10, SimpleMarkerSymbol.STYLE.CIRCLE)));

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

