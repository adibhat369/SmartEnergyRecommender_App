//package com.monash.user.smarter;
//
//import android.app.Fragment;
//import android.content.SharedPreferences;
//import android.location.Address;
//import android.location.Geocoder;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.preference.PreferenceManager;
//import android.support.annotation.Nullable;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.content.Context;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.Spinner;
//import android.widget.TextView;
//
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//import org.w3c.dom.Text;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Locale;
//import com.mapbox.mapboxsdk.MapboxAccountManager;
//import com.mapbox.mapboxsdk.annotations.Icon;
//import com.mapbox.mapboxsdk.annotations.IconFactory;
//import com.mapbox.mapboxsdk.annotations.Marker;
//import com.mapbox.mapboxsdk.annotations.MarkerOptions;
//import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
//import com.mapquest.mapping.maps.OnMapReadyCallback;
//import com.mapquest.mapping.maps.MapboxMap;
//import com.mapquest.mapping.maps.MapView;
//
//import javax.net.ssl.HttpsURLConnection;
//
//
//public class Maps extends Fragment{
//
//    View vMaps;
//    private com.mapbox.mapboxsdk.geometry.LatLng position;
//
//    private MapboxMap mMapboxMap;
//    private MapView mMapView;
//    String address;
//    int spinner_pos=0;
//    List<String> spinner_values;
//    String resid="";
//    Double hourly_threshold = 1.5;
//    Double daily_threshold = 21.0;
//    MarkerOptions markerOptions;
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
//            savedInstanceState) {
//        vMaps = inflater.inflate(R.layout.fragment_maps, container, false);
//        getActivity().setTitle("MAPS");
//
//        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
//        String lat = sharedPreferences.getString("latitude","");
//        String lng = sharedPreferences.getString("longitude","");
//        position = new com.mapbox.mapboxsdk.geometry.LatLng(Double.parseDouble(lat),Double.parseDouble(lng));
//        String res_details = sharedPreferences.getString("res_details", "");
//        try {
//            JSONObject res_json = new JSONObject(res_details);
//            resid = res_json.getString("resid");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        //g.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//        Context con = getActivity();
//       // Double[] latlong = getLocationFromAddress(con,"204 Jasper Road, Melbourne");
//        //String valeus = latlong[0].toString() + "-" + latlong[1];
//
//        //position = new com.mapbox.mapboxsdk.geometry.LatLng(-37.914546,145.042584);
//        //TextView txtview = (TextView) vMaps.findViewById(R.id.wassup);
//        //txtview.setText(valeus);
//
//        Spinner spinner_type = (Spinner) vMaps.findViewById(R.id.spinner_view_maps);
//        spinner_values = new ArrayList<String>();
//        spinner_values.add("Hourly");
//        spinner_values.add("Daily");
//
//
//
//        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, spinner_values);
//        // Drop down layout style - list view with radio button
//        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        // attaching data adapter to spinner
//        spinner_type.setAdapter(dataAdapter);
//        spinner_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
//                spinner_pos = position;
//                GetMapvalueasync map_view = new GetMapvalueasync();
//                map_view.execute();
//
//
//                }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parentView) {
//                // your code here
//            }
//
//        });
//
//        //Initialise mapbox's mapview
//        MapboxAccountManager.start(getActivity());
//
//        mMapView = (MapView) vMaps.findViewById(R.id.mapquestMapView);
//        mMapView.onCreate(savedInstanceState);
//        markerOptions = new MarkerOptions();
//
//
//
//        return vMaps;
//    }
//
//    public void rendermap(final Float mapvals) {
//
//
//
//        mMapView.getMapAsync(new OnMapReadyCallback() {
//
//            @Override
//            public void onMapReady(MapboxMap mapboxMap) {
//                mMapboxMap = mapboxMap;
//                mMapboxMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 11));
//                addMarker(mMapboxMap,mapvals);
//            }
//        });
//
//
//
//    }
//
//    private void addMarker(MapboxMap mapboxMap,Float mapvals) {
//        //Add green marker, default marker is red
//        Icon icon = IconFactory.getInstance(getActivity()).fromResource(R.drawable.greenmarker);
//        markerOptions.position(position);
//        if(spinner_values.get(spinner_pos) == "Hourly") {
//            markerOptions.title("Current Hourly Usage");
//            if(mapvals < hourly_threshold)
//            markerOptions.setIcon(icon);
//        }
//
//        else {
//            markerOptions.title("Current Daily Usage");
//            if(mapvals < daily_threshold)
//                markerOptions.setIcon(icon);
//        }
//        markerOptions.snippet(mapvals.toString()+ " KWH");
//        mapboxMap.addMarker(markerOptions);
//    }
//
//
//    @Override
//    public void onResume()
//    { super.onResume(); mMapView.onResume(); }
//    @Override
//    public void onPause()
//    { super.onPause(); mMapView.onPause(); }
//    @Override
//    public void onDestroy()
//    { super.onDestroy(); mMapView.onDestroy(); }
//    @Override
//    public void onSaveInstanceState(Bundle outState)
//    { super.onSaveInstanceState(outState);
//        mMapView.onSaveInstanceState(outState);
//    }
//
//
//
//    public class GetMapvalueasync extends AsyncTask<String, Void, String[]> {
//        //Get hourly or daily data based on address and date is hard coded
//
//        @Override
//        protected String[] doInBackground(String... params) {
//            String response;
//            try {
//
//                RestClient rc = new RestClient();
//                if(spinner_values.get(spinner_pos) == "Hourly")
//                    response = rc.getValues("/smarterwebservices.electricityusage/findDailyorHourlyUsageofAppliances/" + resid + "/2018-03-17/"+spinner_values.get(spinner_pos));
//                else
//                    response = rc.getValues("/smarterwebservices.electricityusage/findDailyorHourlyUsageofAppliances/" + resid + "/2018-03-17/"+spinner_values.get(spinner_pos));
//                Log.d("response", "" + response);
//                return new String[]{response};
//            } catch (Exception e) {
//                return new String[]{"error"};
//            }
//        }
//
//
//        @Override
//        protected void onPostExecute(String... result) {
//            try {
//                Calendar cal = Calendar.getInstance();
//                int cur_hour = cal.get(Calendar.HOUR_OF_DAY);
//
//                Float mapvals;
//
//                JSONArray jarr = new JSONArray(result[0]);
//
//                if(spinner_values.get(spinner_pos) == "Hourly") {
//                    //JSONArray jarr2= null;
//
//
//                        JSONObject jsonObject = jarr.getJSONObject(cur_hour);
//                        mapvals= (float) jsonObject.getDouble("TotalUsage");
//
//
//
//                }
//                else {
//                    //Generating data only for available data
//                    //for (Integer i = 0; i <jarr.length(); i++) {
//                        JSONObject jsonObject = jarr.getJSONObject(0);
//                        mapvals=(float) jsonObject.getDouble("TotalUsage");
//
//
//
//                }
//
//
//                rendermap(mapvals);
//
//
//
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//                //invalidatepiechart();
//            }
//
//        }
//
//
//    }
//
//
//
//}
