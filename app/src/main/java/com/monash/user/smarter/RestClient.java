package com.monash.user.smarter;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;


import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

public class RestClient {
    private static final String BASE_URI =
            "http://118.138.86.197:8080/SmartERApplication/webresources";
    static JSONObject res_details = null;

    public static String validatecredentials(String uname, String hashedpwd){
        //Validates credentials by getting hitting backend, also fetches details of the resident
        // to store in shared preferences

        final String methodPath = "/smarterwebservices.credentials/findbyUsernameandPwd/" + uname + "/" + hashedpwd;
        final String methodPath2 = "/smarterwebservices.resident/";
//initialise
        Integer resid=-1;
        URL url,url2 = null;
        HttpURLConnection conn = null;
        String textResult = "";
        String resident_details="";
//Making HTTP request
        try {
            url = new URL(BASE_URI + methodPath);

//open the connection
            conn = (HttpURLConnection) url.openConnection();
//set the timeout
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
//set the connection method to GET
            conn.setRequestMethod("GET");

//add http headers to set your response type to json
            conn.setRequestProperty("Content-Type", "text/plain");
            conn.setRequestProperty("Accept", "text/plain");
//Read the response
            Scanner inStream = new Scanner(conn.getInputStream());
//read the input steream and store it as string
            while (inStream.hasNextLine()) {
                textResult += inStream.nextLine();
            }
            resid = Integer.parseInt(textResult);
            url2 = new URL(BASE_URI + methodPath2 + resid.toString());
            conn = (HttpURLConnection) url2.openConnection();
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");

            //Read the response
            Scanner inStream1 = new Scanner(conn.getInputStream());
//read the input steream and store it as string
            while (inStream1.hasNextLine()) {
                resident_details += inStream1.nextLine();
            }
            try {
                res_details = new JSONObject(resident_details);
                Log.d("Resident details found",res_details.toString());

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.disconnect();
        }

        if (resid == -1) {
            return "-1";
        }
        else
            return resid.toString();
    }

   public static boolean registerNewUser(Resident resident) {

        // Register new resident by post method
   URL url = null;
    HttpURLConnection conn = null;
       String resident_str="";
    final String methodPath="/smarterwebservices.resident/";
        try {
        Gson gson =new GsonBuilder().setDateFormat("YYYY-MM-dd'T'HH:mm:ss.SSSXXX").create();
        resident_str=gson.toJson(resident);
        url = new URL(BASE_URI + methodPath);
//open the connection
        conn = (HttpURLConnection) url.openConnection();
//set the timeout
        conn.setReadTimeout(10000);
        conn.setConnectTimeout(15000);
//set the connection method to POST
        conn.setRequestMethod("POST");
        //set the output to true
        conn.setDoOutput(true);
//set length of the data you want to send
        conn.setFixedLengthStreamingMode(resident_str.getBytes().length);
//add HTTP headers
        conn.setRequestProperty("Content-Type", "application/json");
//Send the POST out
        PrintWriter out= new PrintWriter(conn.getOutputStream());
        out.print(resident_str);
        out.close();
        Log.i("error",new Integer(conn.getResponseCode()).toString());
        //res_details = new JSONObject(resident_str);
    } catch (Exception e) {
        e.printStackTrace();
        return false;
    } finally {
     conn.disconnect();


     return true;
 }}


    public static boolean registerNewCredentials(Credentials cred) {
        URL url = null;
        HttpURLConnection conn = null;
        final String methodPath="/smarterwebservices.credentials/";
        try {
            Gson gson =new GsonBuilder().setDateFormat("YYYY-MM-dd'T'HH:mm:ss.SSSXXX").create();
            String cred_str=gson.toJson(cred);
            url = new URL(BASE_URI + methodPath);
//open the connection
            conn = (HttpURLConnection) url.openConnection();
//set the timeout
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
//set the connection method to POST
            conn.setRequestMethod("POST");
            //set the output to true
            conn.setDoOutput(true);
//set length of the data you want to send
            conn.setFixedLengthStreamingMode(cred_str.getBytes().length);
//add HTTP headers
            conn.setRequestProperty("Content-Type", "application/json");
//Send the POST out
            PrintWriter out= new PrintWriter(conn.getOutputStream());
            out.print(cred_str);
            out.close();
            Log.i("error",new Integer(conn.getResponseCode()).toString());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            conn.disconnect();


        }
        return true;
    }


    public String getValues(String requestURL) {
        URL url;
        String response = "";
        try {
            url = new URL(BASE_URI + requestURL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            //conn.setDoInput(true);
            conn.setRequestProperty("Content-Type",
                    "application/json");
            conn.setRequestProperty("Accept", "application/json");
            //conn.setDoOutput(true);
            int responseCode = conn.getResponseCode();

           // if (responseCode == HttpsURLConnection.HTTP_OK) {
            Scanner inStream1 = new Scanner(conn.getInputStream());
//read the input steream and store it as string
            while (inStream1.hasNextLine()) {
                response += inStream1.nextLine();
            }

           // } else {
                //response = "";


        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }


public Boolean  post_usage_details(Electricityusage [] usages) {
    // Method to Post usage details from sqllite db
    URL url = null;
    HttpURLConnection conn = null;
    String usage_str="";
    final String methodPath = "/smarterwebservices.electricityusage/";
    try {
        Gson gson =new GsonBuilder().setDateFormat("YYYY-MM-dd'T'HH:mm:ss.SSSXXX").create();
        for(int i = 0; i<usages.length; i++) {
            String val = gson.toJson(usages[i]);
            if(!val.equals("null"))
             usage_str += gson.toJson(usages[i]);
        }
        url = new URL(BASE_URI + methodPath);
//open the connection
        conn = (HttpURLConnection) url.openConnection();
//set the timeout
        conn.setReadTimeout(10000);
        conn.setConnectTimeout(15000);
//set the connection method to POST
        conn.setRequestMethod("POST");
        //set the output to true
        conn.setDoOutput(true);
//set length of the data you want to send
        conn.setFixedLengthStreamingMode(usage_str.getBytes().length);
//add HTTP headers
        conn.setRequestProperty("Content-Type", "application/json");
//Send the POST out
        PrintWriter out = new PrintWriter(conn.getOutputStream());
        out.print(usage_str);
        out.close();
        Log.i("error", new Integer(conn.getResponseCode()).toString());
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        conn.disconnect();

        return true;
    }

}
public boolean check_if_user_exists (String uname){

    URL url;
    String response = "";
    String methodpath = "/smarterwebservices.credentials/" + uname;
    try {
        url = new URL(BASE_URI + methodpath);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(15000);
        conn.setConnectTimeout(15000);
        conn.setRequestMethod("GET");
        //conn.setDoInput(true);
        conn.setRequestProperty("Content-Type",
                "application/json");
        conn.setRequestProperty("Accept", "application/json");
        //conn.setDoOutput(true);
        int responseCode = conn.getResponseCode();

         if (responseCode == HttpsURLConnection.HTTP_OK) {
        Scanner inStream1 = new Scanner(conn.getInputStream());
//read the input steream and store it as string
        while (inStream1.hasNextLine()) {
            response += inStream1.nextLine();
        }
        if(response.length() > 0)
            return true;

         } else {
             return false;
         }


    } catch (Exception e) {
        e.printStackTrace();
    }
    return false;

    }
}

