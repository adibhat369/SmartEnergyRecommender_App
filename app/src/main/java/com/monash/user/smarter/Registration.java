package com.monash.user.smarter;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Registration extends Activity {
    EditText firstname_view;
    EditText surname_view;
    EditText address_view;
    EditText postcode_view;
    EditText mob_view;
    EditText email_view;
    EditText uname_view;
    EditText pwd_view;
    EditText confpwd_view;
    TextView restext;
    boolean username_already_exists_flag= true;


    private Calendar calendar;
    EditText dob_view;
    private int year, month, day;
    int norpos = 0;
    int enerpos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_registration);

        // get all the views
        firstname_view = (EditText) findViewById(R.id.firstname);

        surname_view = (EditText) findViewById(R.id.surname);

        address_view = (EditText) findViewById(R.id.Address);

        postcode_view = (EditText) findViewById(R.id.Postcode);

        mob_view = (EditText) findViewById(R.id.mobile);

        email_view = (EditText) findViewById(R.id.email);

        uname_view = (EditText) findViewById(R.id.uname);

        pwd_view = (EditText) findViewById(R.id.password);

        confpwd_view = (EditText) findViewById(R.id.conf_pwd);

        restext = (TextView) findViewById(R.id.restextview);

        //initialising values for date picker

        dob_view = (EditText) findViewById(R.id.dob);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        //Creating spinner for No of Residents
        Spinner norspinner = (Spinner) findViewById(R.id.nor);
        List<String> nores = new ArrayList<String>();
        nores.add("1");
        nores.add("2");
        nores.add("3");
        nores.add("4");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, nores);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        norspinner.setAdapter(dataAdapter);
        norspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                norpos = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
        final String selected_nor = nores.get(norpos);
        System.out.print(selected_nor);

        //Creating spinner for energy provider

        Spinner enerspinner = (Spinner) findViewById(R.id.enerprov);
        List<String> enerproviders = new ArrayList<String>();
        enerproviders.add("AGL");
        enerproviders.add("Origin Energy");
        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, enerproviders);
        // Drop down layout style - list view with radio button
        dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        enerspinner.setAdapter(dataAdapter1);
        enerspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                enerpos = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
        final String selected_enerprov = enerproviders.get(enerpos);
        System.out.print(selected_enerprov);


        Button registrbtn = (Button) findViewById(R.id.Register);
        registrbtn.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                //First perform validations
                boolean flag = validate_configurations();
                if(flag) {

                    new AsyncTask<String, Void, Boolean>() {
                        @Override
                        protected Boolean doInBackground(String... params) {

                            RestClient rc = new RestClient();

                            //Check if user exists
                            boolean already_exists = rc.check_if_user_exists(params[9]);
                            if(!already_exists) {


                                DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                                Date date;

                                Date regdate = new Date();
                                try {
                                    date = formatter.parse(params[2]);

                                } catch (ParseException e) {
                                    date = new Date();

                                }
                                //Register resident details first
                                System.out.println(date);
                                Resident resident = new Resident(8, params[0], params[1], date, params[3], params[4], params[6], params[5], Integer.valueOf(params[7]), params[8]);
                                boolean reg_res = rc.registerNewUser(resident);
                                if(reg_res) {
                                    Credentials cred = new Credentials(params[9], params[10], regdate, resident);
                                    // Post to register new credentials
                                    rc.registerNewCredentials(cred);
                                    return true;
                                }
                                else
                                    return false;
                            }
                            else {
                                return false;
                            }
                        }

                        @Override
                        protected void onPostExecute(Boolean response) {

                            //On successful registration, go to home page
                            if(response) {
                                restext.setText("Registered");
                                startActivity(new Intent(Registration.this, Homescreen.class));
                            }
                            else {
                                restext.setText("Username already present");
                            }
                        }
                    }.execute(firstname_view.getText().toString(), surname_view.getText().toString(), dob_view.getText().toString(), address_view.getText().toString(), postcode_view.getText().toString(), mob_view.getText().toString(), email_view.getText().toString(), selected_nor, selected_enerprov, uname_view.getText().toString(), hashpwd(pwd_view.getText().toString()), new Date().toString());

                }
            }
        });

    }

    public boolean validate_configurations() {
        String firstname = firstname_view.getText().toString();
        String surname = surname_view.getText().toString();
        String address = address_view.getText().toString();
        String postcode = postcode_view.getText().toString();
        String mob = mob_view.getText().toString();
        String email = email_view.getText().toString();
        String uname = uname_view.getText().toString();
        String pwd = pwd_view.getText().toString();
        String confpwd = confpwd_view.getText().toString();

        List<EditText> validations = new ArrayList<>();
        validations.add(firstname_view);
        validations.add(surname_view);
        validations.add(address_view);
        validations.add(postcode_view);
        validations.add(mob_view);
        validations.add(email_view);
        validations.add(uname_view);
        validations.add(pwd_view);
        validations.add(confpwd_view);

        //If any of the fields are empty
        for (EditText editText : validations) {
            if (editText.getText().toString().equals("")) {
                restext.setText("*Some fields are empty");
                editText.setHint("*" + editText.getHint());
                editText.setHintTextColor((Color.RED));
                return false;
            }
        }
        // Validate length of the fields
        if (firstname.length() > 25) {
            restext.setText("FirstName too long");
            firstname_view.setText("");
            firstname_view.setHintTextColor((Color.RED));
            return false;
        }
        if (surname.length() > 25) {
            restext.setText("SurName too long");
            surname_view.setText("");
            surname_view.setHintTextColor((Color.RED));
            return false;
        }
        if (address.length() > 255) {
            restext.setText("Address too long");
            address_view.setText("");
            address_view.setHintTextColor((Color.RED));
            return false;
        }

        if (postcode.length() > 10) {
            restext.setText("Postcode too long");
            postcode_view.setText("");
            postcode_view.setHintTextColor((Color.RED));
            return false;
        } else if (postcode.length() < 3) {
            restext.setText("Postcode too short");
            postcode_view.setText("");
            postcode_view.setHintTextColor((Color.RED));
            return false;

        }

        if (email.length() > 25) {
            restext.setText("Email too long");
            email_view.setText("");
            email_view.setHintTextColor((Color.RED));
            return false;
        } else if (!email.contains("@")) {
            restext.setText("Not a valid email");
            email_view.setText("");
            email_view.setHintTextColor((Color.RED));
            return false;
        }

        if (mob.length() > 12) {
            restext.setText("Mobile number too long");
            mob_view.setText("");
            mob_view.setHintTextColor((Color.RED));
            return false;
        } else if (mob.length() < 9) {
            restext.setText("Mobile number too short");
            mob_view.setText("");
            mob_view.setHintTextColor((Color.RED));
            return false;
        }

        if (uname.length() > 25) {
            restext.setText("Username too long");
            uname_view.setText("");
            uname_view.setHintTextColor((Color.RED));
            return false;
        }

        if (!pwd.equals(confpwd)) {
            restext.setText("Passwords do not match");
            pwd_view.setText("");
            confpwd_view.setText("");
            return false;
        }
        if(pwd.length() < 6 ) {
            restext.setText("Password should have minimim of 6 Characters");
            pwd_view.setText("");
            confpwd_view.setText("");
            return false;
        }

       /* new AsyncTask<String, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(String... strings) {
                RestClient rc = new RestClient();
                boolean already_exists = rc.check_if_user_exists(strings[0]);
                return already_exists;
            }
            @Override
            protected void onPostExecute(Boolean already_exists) {
                if(already_exists){
                    //username_already_exists_flag = false;
                    restext.setText("User Name already Exists");
                }
                else
                    username_already_exists_flag = false;
            }
        }.execute(uname);

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if(username_already_exists_flag)
            return false; */

        return true;




    }

    private void showDate(int year, int month, int day) {
        dob_view.setText(new StringBuilder().append(day).append("-0")
                .append(month).append("-").append(year));
    }

    public void setDate(View view) {
        showDialog(999);
        //Toast.makeText(getApplicationContext(), "ca",Toast.LENGTH_SHORT).show();

    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
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
                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day
                    showDate(arg1, arg2 + 1, arg3);
                }
            };

    private String hashpwd(String pwd) {
        return Hashing.sha1().hashString(pwd, Charsets.UTF_8).toString();
    }

}
