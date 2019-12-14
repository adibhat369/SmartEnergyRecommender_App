package com.monash.user.smarter;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Color;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DigitalClock;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;


//import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.net.ssl.HttpsURLConnection;

import static com.google.android.gms.internal.zzhl.runOnUiThread;

public class Homescreen_mainscreen extends Fragment {
    TextView tempview,welcome_msg;
    String fname="";
    String address = "";
    View vMain;
    Integer current_temperature =0;
    SharedPreferences sharedPreferences;
    String date_str="";
    Integer res_id;
    int usage_id = 51;
    String res_details="";
    Double total_current_hour_usage=0.0;
    TextView threshold_view;
    ImageView usg_image;
    DBManager dbmgr;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        vMain = inflater.inflate(R.layout.home_screen_main, container, false);
         welcome_msg= (TextView) vMain.findViewById(R.id.welcome_msg);
        getActivity().setTitle("SmartER");
        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(getActivity());
        res_details = (sharedPreferences.getString("res_details",""));
        try {
            JSONObject res_detail = new JSONObject(res_details);
            fname = res_detail.get("firstname").toString();
            address = res_detail.get("address").toString();
            res_id = res_detail.getInt("resid");
        }
        catch(JSONException e){
            e.printStackTrace();
        }
        //Welcome with first name from shared prefs
        welcome_msg.setText("Welcome "+fname);

        dbmgr = new DBManager(getActivity());


        //Set current date
        TextView txtview = (TextView) vMain.findViewById(R.id.view_date);
        Calendar cal = Calendar.getInstance();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        date_str = df.format(cal.getTime());
        txtview.setText(date_str);



        //Get location from shared prefs and call weather API to get temperature

        String lat1 = sharedPreferences.getString("latitude","");
        String long1 = sharedPreferences.getString("longitude","");
        Gettemptask t = new Gettemptask();
        t.execute(sharedPreferences.getString("latitude",""),sharedPreferences.getString("longitude",""));
        tempview= (TextView)vMain.findViewById(R.id.cur_temp);

        /*while(true) {
            if(t.getStatus() == Gettemptask.Status.FINISHED);
            break;
        }*/

        Button test_btn = (Button) vMain.findViewById(R.id.test_btn);
        test_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncTask<String, Void, Boolean>() {

                    @Override
                    protected Boolean doInBackground(String... strings) {

                        boolean res = delete_date_fromdb();
                        return res;

                    }
                }.execute();
            }});

        threshold_view = (TextView) vMain.findViewById(R.id.threshold_msg);
        usg_image = (ImageView) vMain.findViewById(R.id.image_usage);
        return vMain;
    }
    public void showtemp(String res) {
        final String DEGREE  = "\u00b0";
        current_temperature = Integer.parseInt(res);
        tempview.setText("Current Temp = " + res + DEGREE + "C");
        generate_curhour_data();
    }

    public class Gettemptask extends AsyncTask<String, Void, String[]> {

        @Override
        protected void onPreExecute() {
            System.out.println("Starting");
            super.onPreExecute();

        }

        @Override
        protected String[] doInBackground(String... params) {
            String response;
            try {
                //Call weather API in Async task
                response = getTempByURL("http://api.openweathermap.org/data/2.5/weather?lat="+params[0]+"&lon="+params[1]+"&units=metric&APPID=23aaba3d85eaaf8febb51700b1f13a69");
                Log.d("response",""+response);
                return new String[]{response};
            } catch (Exception e) {
                return new String[]{"error"};
            }
        }


        @Override
        protected void onPostExecute(String... result) {
            try {
                JSONObject mainObject = new JSONObject(result[0]).getJSONObject("main");

                Log.d("result",result[0].toString());
                Integer temp = mainObject.getInt("temp");
                showtemp(temp.toString());

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        public String getTempByURL(String requestURL) {
            URL url;
            String response = "";
            try {
                url = new URL(requestURL);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.setRequestProperty("Content-Type",
                        "application/x-www-form-urlencoded");
                conn.setDoOutput(true);
                int responseCode = conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    String line;
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    while ((line = br.readLine()) != null) {
                        response += line;
                    }
                } else {
                    response = "";
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }
    }

    public void generate_curhour_data () {
        //Thread to generate hourly data, assuming the app runs continuously
        Timer t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {
            public void run() {

                runhourlydatafunc();
            }
        }, new Date(),  3600 * 1000);
    }

    private void runhourlydatafunc() {
        Calendar cal = Calendar.getInstance();
        UsageValueGenerator ugv = new UsageValueGenerator(cal.get(Calendar.HOUR_OF_DAY),current_temperature);
        String[] appliance_usages = ugv.getcurrenthour_data();
        total_current_hour_usage = Double.parseDouble(appliance_usages[0]) + Double.parseDouble(appliance_usages [1]) + Double.parseDouble(appliance_usages[2]);
        total_current_hour_usage = Math.round(total_current_hour_usage * 100.0) / 100.0;

        //Set image based on usage
        setImage(total_current_hour_usage);


        try {
            dbmgr.open();
        }catch(SQLException e) {
            e.printStackTrace();
        }

        long val = dbmgr.insertUsagedate(usage_id,date_str,cal.get(Calendar.HOUR_OF_DAY),Float.parseFloat(appliance_usages[0]),Float.parseFloat(appliance_usages[1]),Float.parseFloat(appliance_usages[2]),current_temperature,res_id);
        System.out.println("Value is =----------------- " + val);
        usage_id++;

        if(cal.get(Calendar.HOUR_OF_DAY) == 23) {
            //At the end of the day, post all records to backend from sqllite
            Senddatatask sendtask = new Senddatatask();
            sendtask.execute();
        }


    }
    private void setImage(final Double value){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                threshold_view.setText("Current Usage - " + total_current_hour_usage.toString() + "KWH");
                if(value > 1.5) {
                    usg_image.setImageResource(R.drawable.alert_image);
                    threshold_view.setTextColor(Color.RED);
                }
                else {
                    usg_image.setImageResource(R.drawable.green_usage);
                    threshold_view.setTextColor(Color.GREEN);
                }
            }
        });
    }
    public boolean delete_date_fromdb() {
        //Get all users, post to backend and clear the sqllite db
        Cursor c = dbmgr.getAllUsers();

        Electricityusage usage[]= new Electricityusage[24];

        if (c.moveToFirst()) {
            do {
                try {

                    Resident obj = new Gson().fromJson(res_details, Resident.class);
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date d =dateFormat.parse(c.getString(1));

                    usage[0] = new Electricityusage(c.getInt(0),d,c.getInt(2),c.getFloat(3),c.getFloat(4),c.getFloat(5),c.getInt(6),obj);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } while (c.moveToNext());
        }
        RestClient rc = new RestClient();

        //
       boolean res = rc.post_usage_details(usage);
       if(res) {
           System.out.print(res);

           dbmgr.deleteUsagedate(res_id.toString());
       }
       return res;

    }
    public class Senddatatask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... strings) {
            Cursor c = dbmgr.getAllUsers();

            Electricityusage [] usages= null;
            Resident obj = new Gson().fromJson(res_details, Resident.class);
            int i =0;

            if (c.moveToFirst()) {
                do {
                    try {


                        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        Date d =dateFormat.parse(c.getString(1));

                        usages[i] = new Electricityusage(c.getInt(0),d,c.getInt(2),c.getFloat(3),c.getFloat(4),c.getFloat(5),c.getInt(6),obj);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } while (c.moveToNext());
            }
            RestClient rc = new RestClient();
            boolean res = rc.post_usage_details(usages);
            return  res;

        }


        @Override
        protected void onPostExecute(Boolean aBoolean) {
            //super.onPostExecute(aBoolean);
            delete_date_fromdb();
        }

    }

   /* public class Getnametask extends AsyncTask<String, Void, String[]> {

        @Override
        protected String[] doInBackground(String... strings) {
            String res = RestClient.GetFirstName();
            String p[] = new String[1];
            p[0] = res;
            return  p;
        }

        @Override
        protected void onPostExecute(String[] strings) {
            setwelcomemsg(strings[0]);
        }
    }*/


}
