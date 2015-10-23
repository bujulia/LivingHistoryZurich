/* Copyright 2015 Esri
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.esri.arcgis.android.samples.wmslayer;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.esri.android.map.MapView;
import com.esri.android.map.ogc.WMSLayer;

/**
 * Sample showing an Esri Basemap with an WMS layer overlay
 * The WMS Layer is provided by:
 *    <a href="http://openweathermap.org/">Open Weather Map</a>
 */
public class MainActivity extends Activity {

    MapView mMapView;
    WMSLayer wmsLayer;
    String wmsURL;

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
        // available layers
        String[] visibleLayers = {"Uebersichtsplan"};
      //  String[] visibleLayers = {"Uebersichtsplan", "Uebersichtsplan_1970", "Uebersichtsplan_1900", "Uebersichtsplan_1860", "Uebersichtsplan_1793"};

        //String[] visibleWMS = {};

/*        if (visibleWMS.equals("Uebersichtsplan")) {
            wmsLayer.setVisibleLayer(visibleWMS);
            mMapView.addLayer(wmsLayer);
        } else if (visibleWMS.equals("Uebersichtsplan_1970")) {
            wmsLayer.setVisibleLayer(visibleWMS);
            mMapView.addLayer(wmsLayer);
        } else if (visibleWMS.equals("Uebersichtsplan_1900")) {
            wmsLayer.setVisibleLayer(visibleWMS);
            mMapView.addLayer(wmsLayer);
        } else if (visibleWMS.equals("Uebersichtsplan_1860")) {
            wmsLayer.setVisibleLayer(visibleWMS);
            mMapView.addLayer(wmsLayer);
        } else if (visibleWMS.equals("Uebersichtsplan_1793")) {
            wmsLayer.setVisibleLayer(visibleWMS);
            mMapView.addLayer(wmsLayer);
        }
*/
        wmsLayer.setVisibleLayer(visibleLayers);
        //wmsLayer.setOpacity();
        mMapView.addLayer(wmsLayer);

        /* String URL = "http://services1.arcgis.com/i9MtZ1vtgD3gTnyL/ArcGIS/rest/services/Tour01/FeatureServer/2";
        ArcGISFeatureLayer fLayer = new ArcGISFeatureLayer(URL, MODE.ONDEMAND);
        */

        // Set the Esri logo to be visible, and enable map to wrap around date line.
        mMapView.setEsriLogoVisible(true);
        mMapView.enableWrapAround(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
