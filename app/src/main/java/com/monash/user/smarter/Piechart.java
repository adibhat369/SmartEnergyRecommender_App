package com.monash.user.smarter;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.EditText;

import com.github.mikephil.charting.charts.PieChart;

import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;


public class Piechart extends Activity {
    String resid;
    EditText datepicker_view;
    String selected_date="";
    private int year, month, day;
    PieChart pieChart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle("PIE CHART");
        setContentView(R.layout.pie_chart);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String res_details = sharedPreferences.getString("res_details", "");
        try {
            JSONObject res_json = new JSONObject(res_details);
            resid = res_json.getString("resid");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Set date picker for pie chart

        datepicker_view = (EditText) findViewById(R.id.date_picker_text);

        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        pieChart = (PieChart) findViewById(R.id.piechart);

    }
    private void invalidatepiechart() {

        pieChart.invalidate();
    }
    public void renderpiechart(float fval,float acval,float wmval,float fper,float acper,float wmper) {

        //Add values to pie chart and display it

        pieChart.setCenterText("Daily Usage Statistics");
        List<PieEntry> yvalues = new ArrayList<PieEntry>();
        yvalues.add(new PieEntry(fper, "Fridge",fval));
        yvalues.add(new PieEntry(acper, "AC",acval));
        yvalues.add(new PieEntry(wmper, "WashingMachine",wmval));


        PieDataSet dataSet = new PieDataSet(yvalues, "Daily Usage Statistics");
        dataSet.setDrawValues(true);
        dataSet.setValueFormatter(new PercentFormatter());
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);




        PieData data = new PieData(dataSet);

        pieChart.setData(data);
        pieChart.invalidate();

    }
    private void showDate(int year, int month, int day) {
        datepicker_view.setText(new StringBuilder().append(year).append("-0")
                .append(month).append("-").append(day));

    }

    public void setDate(View view) {
        showDialog(999);
        //Toast.makeText(getApplicationContext(), "ca",Toast.LENGTH_SHORT).show();

    }

    @Override
    protected Dialog onCreateDialog(int id) {

        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    showDate(arg1, arg2+1, arg3);
                    selected_date= datepicker_view.getText().toString();
                    GetPievalueasync pie = new GetPievalueasync();
                    pie.execute();
                }
            };

    //Get values to populate in PIE chart asynchronously
    public class GetPievalueasync extends AsyncTask<String, Void, String[]> {

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
                response = rc.getValues("/smarterwebservices.electricityusage/findDailyUsageofAppliances/" + resid + "/" + selected_date);
                Log.d("response", "" + response);
                return new String[]{response};
            } catch (Exception e) {
                return new String[]{"error"};
            }
        }


        @Override
        protected void onPostExecute(String... result) {
            try {
                JSONArray jarr = new JSONArray(result[0]);
                JSONObject jsonObject = jarr.getJSONObject(0);
                float fridge_val = (float) jsonObject.getDouble("TotalFridgeusage");
                float wm_val = (float) jsonObject.getDouble("TotalWashingmachineusage");
                float ac_val = (float) jsonObject.getDouble("TotalACusage");

                // Calculate percentages
                float fridge_per = fridge_val / (fridge_val + ac_val + wm_val) * 100;
                float ac_per = ac_val / (fridge_val + ac_val + wm_val) * 100;
                float wm_per = wm_val / (fridge_val + ac_val + wm_val) * 100;

                renderpiechart(fridge_val, ac_val, wm_val, fridge_per, ac_per, wm_per);


            } catch (JSONException e) {
                e.printStackTrace();
                invalidatepiechart();
            }

        }


    }}


