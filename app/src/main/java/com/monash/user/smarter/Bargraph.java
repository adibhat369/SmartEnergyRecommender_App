package com.monash.user.smarter;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toolbar;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class Bargraph extends AppCompatActivity {
    String resid="";
    int spinner_pos = 0;
    String view_type="";
    BarChart barChart;
    List<String> spinner_actual_values = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bargraph);


        this.setTitle("BAR GRAPH");

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String res_details = sharedPreferences.getString("res_details", "");
        try {
            JSONObject res_json = new JSONObject(res_details);
            resid = res_json.getString("resid");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Iniitialise spinner for bar graph

         Spinner spinner_type = (Spinner) findViewById(R.id.spinner_view);
        List<String> spinner_values = new ArrayList<String>();
        spinner_values.add("Last 4 hours");
        spinner_values.add("Last 3 days");

        spinner_actual_values.add("Hourly");
        spinner_actual_values.add("Daily");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinner_values);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinner_type.setAdapter(dataAdapter);
        spinner_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                spinner_pos = position;
                GetBarvalueasync bar = new GetBarvalueasync();
                bar.execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });



        barChart = (BarChart) findViewById(R.id.bar_graph);


    }

    public void renderbarchart(HashMap<String,Float> barvals) {

        //Add the data into the bar graph and display
        List<BarEntry> entries = new ArrayList<>();
        for (HashMap.Entry<String, Float> entry : barvals.entrySet()) {
            entries.add(new BarEntry(Float.parseFloat(entry.getKey()),entry.getValue()));

        }

        BarDataSet set = new BarDataSet(entries, "BarDataSet");
        set.setColors(ColorTemplate.COLORFUL_COLORS);
        BarData bardata = new BarData(set);
        bardata.setBarWidth(0.7f); // set custom bar width
        barChart.setData(bardata);
        barChart.setFitBars(true); // make the x-axis fit exactly all bars
        barChart.invalidate();



    }
    //Get data for bar graph asynchronously
    public class GetBarvalueasync extends AsyncTask<String, Void, String[]> {

        @Override
        protected void onPreExecute() {
            System.out.println("Starting");
            super.onPreExecute();

        }

        @Override
        protected String[] doInBackground(String... params) {
            String response;
            try {

                RestClient rc = new RestClient();
                if(spinner_actual_values.get(spinner_pos) == "Hourly")
                response = rc.getValues("/smarterwebservices.electricityusage/findDailyorHourlyUsageofAppliances/" + resid + "/2018-03-17/"+spinner_actual_values.get(spinner_pos));
                else
                    response = rc.getValues("/smarterwebservices.electricityusage/findlastthreeUsageofAppliances/" + resid + "/2018-03-17/"+spinner_actual_values.get(spinner_pos));
                Log.d("response", "" + response);
                return new String[]{response};
            } catch (Exception e) {
                return new String[]{"error"};
            }
        }


        @Override
        protected void onPostExecute(String... result) {
            try {
                Calendar cal = Calendar.getInstance();
                int cur_hour = cal.get(Calendar.HOUR_OF_DAY);
                Integer cur_day = 15; //Hard coding for data
                HashMap<String, Float> barvals = new HashMap<>();
                JSONArray jarr = new JSONArray(result[0]);

                if(spinner_actual_values.get(spinner_pos) == "Hourly") {

                    //Getting data only for last 4 hours
                    for (int i = cur_hour; i > 0 && i > cur_hour - 4; i--) {
                        JSONObject jsonObject = jarr.getJSONObject(i);
                        barvals.put(jsonObject.getString("UsageHour"), (float) jsonObject.getDouble("TotalUsage"));


                    }
                }
                else {
                    //Generating data only for available data
                    for (Integer i = 0; i <jarr.length(); i++) {
                        JSONObject jsonObject = jarr.getJSONObject(i);
                        barvals.put(cur_day.toString(), (float) jsonObject.getDouble("TotalUsage"));
                        cur_day++;
                    }

                }

                renderbarchart(barvals);




            } catch (JSONException e) {
                e.printStackTrace();
                //invalidatepiechart();
            }

        }


    }

}
