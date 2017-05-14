/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hostandguest.util;

import com.hostandguest.entities.Property;
import com.hostandguest.services.PropertyService;
import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.MapComponentInitializedListener;
import com.lynden.gmapsfx.javascript.event.GMapMouseEvent;
import com.lynden.gmapsfx.javascript.event.UIEventType;
import com.lynden.gmapsfx.javascript.object.Animation;
import com.lynden.gmapsfx.javascript.object.GoogleMap;
import com.lynden.gmapsfx.javascript.object.InfoWindow;
import com.lynden.gmapsfx.javascript.object.InfoWindowOptions;
import com.lynden.gmapsfx.javascript.object.LatLong;
import com.lynden.gmapsfx.javascript.object.MapOptions;
import com.lynden.gmapsfx.javascript.object.MapTypeIdEnum;
import com.lynden.gmapsfx.javascript.object.Marker;
import com.lynden.gmapsfx.javascript.object.MarkerOptions;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.json.JSONObject;

/**
 *
 * @author BEYRAM-BG
 */
public class MainAppGoogleMap extends Application implements MapComponentInitializedListener {

GoogleMapView mapView;
GoogleMap map;

    @Override
    public void start(Stage stage) throws Exception {
        mapView = new GoogleMapView();
        mapView.addMapInializedListener(this);
        Scene scene = new Scene(mapView);
        stage.setTitle("Host and Guest - Google Maps ");
        stage.setScene(scene);
        stage.show();
    }


    @Override
    public void mapInitialized() {
        MapOptions mapOptions = new MapOptions();
        mapOptions.center(new LatLong(36.7950758,10.0031897))
                .mapType(MapTypeIdEnum.TERRAIN)
                .overviewMapControl(false)
                .panControl(false)
                .rotateControl(false)
                .scaleControl(false)
                .streetViewControl(false)
                .zoomControl(true)
                .zoom(5);
        map = mapView.createMap(mapOptions);
        Iterator it = getHashMapFromProperty().entrySet().iterator();
        while (it.hasNext()) {
        Map.Entry pair = (Map.Entry)it.next();
        MarkerOptions markerOptions = new MarkerOptions();
        Double lat = ((JSONObject)pair.getValue()).getDouble("lat");
        Double lng = ((JSONObject)pair.getValue()).getDouble("lng");
        markerOptions.position( new LatLong(lat, lng) )
                    .visible(Boolean.TRUE)
                    .title(" "+((Property)pair.getKey()).getPrice())
                    .visible(true)
                    .animation(Animation.BOUNCE);
        Marker marker = new Marker( markerOptions );
        InfoWindowOptions infoOptions = new InfoWindowOptions();
        infoOptions.content("Property Description: <b>"+((Property)pair.getKey()).getDescription()+"</b> <br> Property Price:<b> "+((Property)pair.getKey()).getPrice()+"</b> TND");          
        InfoWindow window = new InfoWindow(infoOptions);
        map.addMarker(marker);
        map.addUIEventHandler(marker, UIEventType.click, e -> { 
            window.open(map, marker);
        });
        
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
    
    public HashMap<Property,JSONObject> getHashMapFromProperty(){
        HashMap hm = new HashMap();
        PropertyService ps = new PropertyService();
        ArrayList<Property> listProp = ps.getAllProperty();
        for(Property p : listProp){
            hm.put(p, getLatLongObject(p.getLocation()));
        }
        System.out.println("get LatLong from Google Api 100%");
        return hm;
    }
    
    public JSONObject getLatLongObject(String place){
        JSONObject lnglat = null;
        try {
            
            URL url = new URL("http://maps.googleapis.com/maps/api/geocode/json?address="+URLEncoder.encode(place, "UTF-8")+"&sensor=true");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            
            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }
            
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));
            String output = "";
            String res = "" ;
            while (( output= br.readLine()) != null) {
                res += output;
            }
            conn.disconnect();
            JSONObject obj = new JSONObject(res);
            lnglat = obj.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location") ;
        } catch (MalformedURLException e) {
        } catch (IOException e) {
        }
        return lnglat ;
    } 
}