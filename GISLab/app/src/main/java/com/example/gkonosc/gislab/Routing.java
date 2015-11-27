package com.example.gkonosc.gislab;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.esri.core.tasks.na.RouteTask;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;






public class Routing extends AsyncTask <String, String, String> {

    String routeTaskURL = "http://route.arcgis.com/arcgis/rest/services/World/Route/NAServer/Route_World";
    RouteTask routeTask = RouteTask.createOnlineRouteTask(routeTaskURL, null);
    String extern = Environment.getExternalStorageDirectory().getPath();
    RouteTask routeTask = RouteTask.createLocalRouteTask(extern + "/ArcGIS/Samples/Routing/SanDiego.geodatabase", "Streets_ND");

    @Override
    protected String doInBackground(String... params) {

        String urlString=params[0]; // URL to call

        String resultToDisplay = "";

        InputStream in = null;

        // HTTP Get
        try {

            URL url = new URL(urlString);

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            in = new BufferedInputStream(urlConnection.getInputStream());

        } catch (Exception e ) {

            System.out.println(e.getMessage());

            return e.getMessage();

        }

        return resultToDisplay;
    }

    /*    URL url;

        //-----------------------
        // Send Request to server
        //-----------------------
        try {
            // First entry is the request
            url = new URL(urls[0]);
        }
        catch (MalformedURLException e){
            Log.d("myInfo", urls[0]);
            Log.d("myInfo", "URL Creation");
            Log.d("myInfo", e.getMessage());
            return e.getMessage();
        }

        InputStream input;

        // Try to connect
        try {
            input = url.openStream();
        }
        catch (IOException e) {
            Log.d("myInfo", "OpenStream");
            Log.d("myInfo", e.getMessage());
            return e.getMessage();
        }

        // Convert to StringBuilder
        String result = convertStreamToString(input);

        // Return result
        Log.d("myInfo", result);
        return result;
    }
*/
    @Override
    protected void onPostExecute(String result) {
        // Probably needed to iterate over all points and draw them separately...
        //GroundOverlay route = map.addGroundOverlay( );
        //Log.d("myInfo", "postexec");
    }

    /*// Method to convert an InputStream to a String
    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }*/
}