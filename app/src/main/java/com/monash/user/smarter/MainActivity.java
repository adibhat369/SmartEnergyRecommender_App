package com.monash.user.smarter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends Activity {
    boolean login_flag = false;
    SharedPreferences sharedPref;
    Context con;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        TextView register_msg = findViewById(R.id.newuser);
        register_msg.setPaintFlags(register_msg.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        final Button login_btn = (Button) findViewById(R.id.login_btn);

        // On click of login button
        login_btn.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                boolean invalid_flag = false;
                final TextView invalid_view = (TextView) findViewById(R.id.Invalid_cred_textview);
                final EditText username_view = (EditText) findViewById(R.id.log_uname);
                final String uname = username_view.getText().toString();
                final EditText pwd_view = (EditText) findViewById(R.id.log_pwd);
                String pwd = pwd_view.getText().toString();

                // Perform Initial Validation
                if (uname.equals("")) {
                    invalid_view.setText("UserName Cannot be Empty");
                    invalid_flag = true;
                }

                else if (pwd.equals("")) {
                    invalid_view.setText("Password Cannot be Empty");
                    invalid_flag = true;
                }
                if (!invalid_flag) {
                    final String hashedpwd = hashpwd(pwd);

                    //create     an anonymous AsyncTask to validate credentials
                    new AsyncTask<Void, Void, String>() {
                        @Override
                        protected String doInBackground(Void... params) {
                            return RestClient.validatecredentials(uname, hashedpwd);
                        }

                        @Override
                        protected void onPostExecute(String res) {
                            if (res == "-1") {
                                invalid_view.setText("Invalid Credentials, Try Again");
                                pwd_view.setText("");
                                username_view.setText("");

                            } else {
                                //Correct login, Start Homescreen activity
                                setlogintrue(res);
                                startActivity(new Intent(MainActivity.this, Homescreen.class));
                            }

                        }
                    }.execute();


                }
            }


        });


        //If register link is clicked, start registration activity

       TextView registration_text = (TextView) findViewById(R.id.newuser);
       registration_text.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent Intent = new Intent(view.getContext(), Registration.class);
               view.getContext().startActivity(Intent);}
       });



    }
    private void setlogintrue(String res) {
        login_flag = true;

        int resid = Integer.parseInt(res);

        // Put the resid and details into shared preferences, so that it can be accessed everywhere
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("Resident", resid);
        if(RestClient.res_details != null) {
            editor.putString("res_details", RestClient.res_details.toString());
            editor.commit();

            //Get location async task through web service call to google API
            Getlocationasync g = new Getlocationasync();
            try {
                g.execute(RestClient.res_details.get("address").toString());
            }catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
    // Hashing method, to hash password  using guava API
    private String hashpwd(String pwd) {
        return Hashing.sha1().hashString( pwd, Charsets.UTF_8 ).toString();
    }
    public class Getlocationasync extends AsyncTask<String, Void, String[]> {

        @Override
        protected void onPreExecute() {
            System.out.println("Starting");
            super.onPreExecute();

        }

        @Override
        protected String[] doInBackground(String... params) {
            String response;
            try {
                response = getLatLongByURL("http://maps.google.com/maps/api/geocode/json?address=" + params[0] +"&sensor=false");
                Log.d("response",""+response);
                return new String[]{response};
            } catch (Exception e) {
                return new String[]{"error"};
            }
        }


        @Override
        protected void onPostExecute(String... result) {
            try {
                JSONObject jsonObject = new JSONObject(result[0]);

                Double lng = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
                        .getJSONObject("geometry").getJSONObject("location")
                        .getDouble("lng");

                Double lat = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
                        .getJSONObject("geometry").getJSONObject("location")
                        .getDouble("lat");

                Log.d("latitude", "" + lat);
                Log.d("longitude", "" + lng);

                // put latitude and longitude as well into shared preferences
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("latitude",lat.toString());
                editor.putString("longitude",lng.toString());
                editor.commit();


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }



        public String getLatLongByURL(String requestURL) {
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



}
