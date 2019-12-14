package com.monash.user.smarter;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.support.v7.app.AppCompatActivity;

public class LineGraph extends AppCompatActivity {
    String resid = "";
    int spinner_pos = 0;
    String view_type = "";
    GraphView graph;
    List<String> spinner_actual_values = new ArrayList<String>();
    LineGraphSeries<DataPoint> series;
    LineGraphSeries<DataPoint> series2;
    boolean firsttime_flag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_graph);
        this.setTitle("LINE GRAPH");

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String res_details = sharedPreferences.getString("res_details", "");
        try {
            JSONObject res_json = new JSONObject(res_details);
            resid = res_json.getString("resid");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Attach spinner to line graph screen

        Spinner spinner_type = (Spinner) findViewById(R.id.spinner_view_line);
        List<String> spinner_values = new ArrayList<String>();
        spinner_values.add("Last 3 hours");
        spinner_values.add("Last 2 days");

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
                Getlinevalueasync linesync = new Getlinevalueasync();
                linesync.execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        graph = (GraphView) findViewById(R.id.graph);

    }

    public void renderlinechart(List<Float> temps, List<Float> usages, List<Float> xvals) {

        //Create 2 sets of series with data points

        series = new LineGraphSeries<>();
        series2 = new LineGraphSeries<>();
        DataPoint[] entries = new DataPoint[temps.size()];
        for (int index = 0; index < temps.size(); index++) {
            entries[index] = new DataPoint(xvals.get(index), temps.get(index));
            series.appendData(entries[index], true, 5);
        }


        // LineGraphSeries<DataPoint> series2 = new LineGraphSeries<>();
        DataPoint[] entries2 = new DataPoint[usages.size()];
        for (int index = 0; index < usages.size(); index++) {
            entries2[index] = new DataPoint(xvals.get(index), usages.get(index));
            series2.appendData(entries2[index], true, 5);
        }
        //Attach second scale
        graph.getSecondScale().addSeries(series2);
// the y bounds are always manual for second scale
        if (spinner_actual_values.get(spinner_pos) == "Hourly") {
            graph.getSecondScale().setMinY(0);
            graph.getSecondScale().setMaxY(5);

            graph.getGridLabelRenderer().setVerticalLabelsSecondScaleColor(Color.RED);

            graph.getViewport().setXAxisBoundsManual(true);
            graph.getViewport().setMinX(14);
            graph.getViewport().setMaxX(18);
            graph.getViewport().setScrollable(true);
            graph.getViewport().setScrollableY(true);


// set manual Y bounds
            graph.getViewport().setYAxisBoundsManual(true);
            graph.getViewport().setMinY(16);
            graph.getViewport().setMaxY(19);
            graph.getLegendRenderer().setVisible(true);
            graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.BOTTOM);
        } else {
            graph.getSecondScale().setMinY(8);
            graph.getSecondScale().setMaxY(25);

            graph.getGridLabelRenderer().setVerticalLabelsSecondScaleColor(Color.RED);

            graph.getViewport().setXAxisBoundsManual(true);
            graph.getViewport().setMinX(15);
            graph.getViewport().setMaxX(21);
            graph.getViewport().setScrollable(true);
            graph.getViewport().setScrollableY(true);


// set manual Y bounds
            graph.getViewport().setYAxisBoundsManual(true);
            graph.getViewport().setMinY(17);
            graph.getViewport().setMaxY(23);

        }
        if (firsttime_flag)
            series.setTitle("Temperature");
        series.setColor(Color.GREEN);

        series.setDrawDataPoints(true);
        series.setDataPointsRadius(10);
        series.setThickness(8);
        if (firsttime_flag) {
            series2.setTitle("Usage");
            firsttime_flag = false;
        }

        series2.setColor(Color.RED);
        series2.setDrawDataPoints(true);
        series2.setDataPointsRadius(10);
        series2.setThickness(8);
        graph.getLegendRenderer().setVisible(true);
        //graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.BOTTOM);
// custom paint to make a dotted line
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10);
        paint.setPathEffect(new DashPathEffect(new float[]{8, 5}, 0));
        //series2.setCustomPaint(paint);
        graph.addSeries(series);
    }

    //Async task to get values for line graph

    public class Getlinevalueasync extends AsyncTask<String, Void, String[]> {

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
                if (spinner_actual_values.get(spinner_pos) == "Hourly")
                    response = rc.getValues("/smarterwebservices.electricityusage/findDailyorHourlyUsageofAppliances/" + resid + "/2018-03-17/" + spinner_actual_values.get(spinner_pos));
                else
                    response = rc.getValues("/smarterwebservices.electricityusage/findlastthreeUsageofAppliances/" + resid + "/2018-03-17/" + spinner_actual_values.get(spinner_pos));
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

                List<Float> temps = new ArrayList<>();
                List<Float> usages = new ArrayList<>();
                List<Float> xvals = new ArrayList<>();
                JSONArray jarr = new JSONArray(result[0]);

                if (spinner_actual_values.get(spinner_pos) == "Hourly") {
                    //JSONArray jarr2= null;

                    for (int i = cur_hour - 3; i < cur_hour; i++) {
                        JSONObject jsonObject = jarr.getJSONObject(i);
                        temps.add((float) jsonObject.getDouble("Temperature"));
                        usages.add((float) jsonObject.getDouble("TotalUsage"));
                        xvals.add((float) jsonObject.getDouble("UsageHour"));

                    }

                } else {
                    //Generating data only for available data
                    for (Integer i = 0; i < jarr.length() - 1; i++) {
                        JSONObject jsonObject = jarr.getJSONObject(i);
                        temps.add((float) jsonObject.getDouble("AvgTemperature"));
                        usages.add((float) jsonObject.getDouble("TotalUsage"));
                        xvals.add((float) cur_day);
                        cur_day++;
                    }

                }
                //Generate line graph with data retrieved
                renderlinechart(temps, usages, xvals);


            } catch (JSONException e) {
                e.printStackTrace();

            }

        }


    }

}

