package com.example.gkonosc.gislab;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.android.map.ogc.WMSLayer;
import com.esri.core.geometry.CoordinateConversion;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.io.UserCredentials;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.SimpleLineSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.esri.core.tasks.na.CostAttribute;
import com.esri.core.tasks.na.NAFeaturesAsFeature;
import com.esri.core.tasks.na.NetworkDescription;
import com.esri.core.tasks.na.Route;
import com.esri.core.tasks.na.RouteParameters;
import com.esri.core.tasks.na.RouteResult;
import com.esri.core.tasks.na.RouteTask;
import com.esri.core.tasks.na.StopGraphic;

import java.util.List;

public class Routing extends Activity implements LocationListener{

    public String importDestination;
    String coordinates;
    private LocationManager locationManager;
    private Location currentLocation;
    WMSLayer wmsLayer;
    String wmsURL;
    MapView mMapView;
    public GraphicsLayer graphicsLayer = new GraphicsLayer();
    Point myPoint = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.routing);

        // Get coordinates of the selected POI as input
        Intent intent = getIntent();
        importDestination = intent.getStringExtra("destination");

        Log.d("Import Destination: ", importDestination);

        mMapView = (MapView) findViewById(R.id.map);
        mMapView.enableWrapAround(true);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 5, this);

        currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        // If the currentLocation is empty a message appears
        if (currentLocation == null) {
            Context context = getApplicationContext();
            CharSequence text = "Please enable GPS";
            int duration = Toast.LENGTH_SHORT;
            Toast.makeText(context, text, duration).show();
        } else { // The coordinates of the current location of the user are
            // saved
            coordinates = "" + currentLocation.getLatitude() + ","
                    + currentLocation.getLongitude();
            // System.out.println(startingPoint);
        }

        myPoint = GeometryEngine.project(currentLocation.getLongitude(), currentLocation.getLatitude(), SpatialReference.create(102100));
        graphicsLayer.addGraphic(new Graphic(myPoint, new SimpleMarkerSymbol(Color.BLUE, 10, SimpleMarkerSymbol.STYLE.CIRCLE)));
        //coordinates =  "" + currentLocation.getLatitude() + "," + currentLocation.getLongitude();

        wmsURL = "http://www.gis.stadt-zuerich.ch/maps/services/wms/WMS-ZH-STZH-OGD/MapServer/WMSServer";
        wmsLayer = new WMSLayer(wmsURL);
        wmsLayer.setImageFormat("image/png");
        //Starts the WMS layer
        String[] visibleLayers = {"Uebersichtsplan"};
        wmsLayer.setVisibleLayer(visibleLayers);
        mMapView.addLayer(wmsLayer);
        mMapView.addLayer(graphicsLayer);

        RouteCalculator myAsync = new RouteCalculator();
        myAsync.execute();
    }

    // Display route
    public class RouteCalculator extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... params) {

            UserCredentials creds = new UserCredentials();
            creds.setUserAccount("IKGStud14", "i2rJVYT6c7");
            String routeTaskURL = "http://route.arcgis.com/arcgis/rest/services/World/Route/NAServer/Route_World";

            try {
                RouteTask routeTask = RouteTask.createOnlineRouteTask(routeTaskURL, creds);

                RouteParameters routeParams = routeTask.retrieveDefaultRouteTaskParameters();
                NetworkDescription description = routeTask.getNetworkDescription();
                List<CostAttribute> costAttributes = description.getCostAttributes();

                if (costAttributes.size() > 0)
                    routeParams.setImpedanceAttributeName(costAttributes.get(0)
                            .getName());

                NAFeaturesAsFeature naFeatures = new NAFeaturesAsFeature();

                Geometry mLocation = CoordinateConversion.decimalDegreesToPoint(coordinates, SpatialReference.create(SpatialReference.WKID_WGS84_WEB_MERCATOR));

                Log.d("My Location: ", mLocation.toString());

                // That's why we can directly create a point and use this for the routing
                Geometry mDestination = new Point(Double.parseDouble(importDestination.split(",")[0]), Double.parseDouble(importDestination.split(",")[1]));
                graphicsLayer.addGraphic(new Graphic(mDestination, new SimpleMarkerSymbol(Color.RED, 10, SimpleMarkerSymbol.STYLE.CIRCLE)));

                Log.d("My Destination: ", mDestination.toString());

                StopGraphic startPnt = new StopGraphic(mLocation);
                StopGraphic endPnt = new StopGraphic(mDestination);

                naFeatures.setFeatures(new Graphic[] { startPnt, endPnt });

                routeParams.setStops(naFeatures);

                naFeatures.setSpatialReference(mMapView.getSpatialReference());
                routeParams.setOutSpatialReference(mMapView.getSpatialReference());

                RouteResult mResults = routeTask.solve(routeParams);
                List<Route> routes = mResults.getRoutes();
                Route mRoute = routes.get(0);

                Geometry routeGeom = mRoute.getRouteGraphic().getGeometry();
                Graphic symbolGraphic = new Graphic(routeGeom,new SimpleLineSymbol(Color.BLUE, 3));

                GraphicsLayer gLayer = new GraphicsLayer();
                gLayer.addGraphic(symbolGraphic);
                mMapView.addLayer(gLayer);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.unpause();
    }

    @Override
    public void onLocationChanged(Location location) {
        currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    }

    // Stopping the locationManager (deactivates GPS when the Activity is
    // closed)
    protected void onStop() {
        super.onStop();
        locationManager.removeUpdates(this);
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
