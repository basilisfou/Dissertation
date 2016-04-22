package com.example.stonesoup.dissertation;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class Register extends AppCompatActivity {
    private EditText name , surname , email, username , password;
    private Button signUp, reset;
    private Context context; // activity context
    private Toolbar mToolbar;
    private TextView mTitles;
    private String messageResponce;
    private Boolean res;
    private String nameString, surnameString, emailString, usernameString, passwordString, token;
    //private final static String URL = "http://192.168.145.2:3000/users/register";
    private final static String URL = "http://83.212.113.58:3000/users/register";
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_screen);

        context = getApplicationContext();
        /**
         * Toolbar customization
         */
        mToolbar = (Toolbar) findViewById(R.id.toolbar); //replacing the old Action bar
        setSupportActionBar(mToolbar);
        //
        pref = context.getSharedPreferences("sessionUser", 0);// 0 - for private mode
        editor = pref.edit();
        //
        //Checking if the device is connected to the internet
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(context.CONNECTIVITY_SERVICE);
        final NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        mTitles = (TextView) mToolbar.findViewById(R.id.toolbar_title);
        mTitles.setText("Register");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        name = (EditText)findViewById(R.id.edit_box_name);
        surname = (EditText)findViewById(R.id.edit_box_surname);
        email = (EditText)findViewById(R.id.edit_box_email);
        username = (EditText)findViewById(R.id.edit_box_username);
        password = (EditText)findViewById(R.id.edit_box_pass);

        signUp = (Button)findViewById(R.id.buttonSignUp);
        reset = (Button)findViewById(R.id.buttonReset);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                nameString = name.getText().toString();
                surnameString = surname.getText().toString();
                emailString = email.getText().toString();
                usernameString = username.getText().toString();
                passwordString = password.getText().toString();
                //first validation
                if(nameString !=null && surnameString !=null && emailString != null && usernameString != null && passwordString != null){

                    if(networkInfo != null && networkInfo.isConnected()){
                        //request body
                        final JSONObject jsonBody;

                        try {
                            jsonBody = new JSONObject("{\"name\":\""+nameString+"\", " +
                                    "\"surname\":\""+surnameString+"\",\"email\":\""+emailString+"\",\"userName\":\""+usernameString+"\"," +
                                    "\"passWord\":\""+passwordString+"\"}");

                            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,URL,
                                    jsonBody, new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            try{
                                                Log.d("Billy",response.toString());
                                                messageResponce = response.getString("response");
                                                res = response.getBoolean("res");
                                                token = response.getString("token"); // token for the new user
                                                //Log.d("billy",token);
                                            }catch (JSONException e){
                                                e.printStackTrace();
                                                //Log.d("billy",e.toString());
                                            }

                                            //into the app
                                            if(res) {
                                                //Save the token for this user in order to maintain session
                                                editor.putString("token",token);
                                                editor.putString("username",usernameString);
                                                editor.commit();

                                                Toast toast = Toast.makeText(context, messageResponce,Toast.LENGTH_LONG);
                                                toast.show();

                                                Intent intent = new Intent(context, MainActivity.class);
                                                startActivity(intent);
                                            } else {
                                                //error message from the server
                                                Toast toast = Toast.makeText(context, messageResponce,Toast.LENGTH_LONG);
                                                toast.show();
                                            }
                                        }
                                    }, new Response.ErrorListener(){

                                        @Override
                                        public void onErrorResponse(VolleyError error) {

                                        }
                                    });

                            Volley.newRequestQueue(context).add(request);
                        }catch (JSONException e){
                            e.printStackTrace();
                            Log.d("billy",e.toString());
                        }
                    }
                } else {
                    Toast toast = Toast.makeText(context,"Please give a User name and Password",Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });
        /**
         * reseting fields
         */
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name.setText(name.getHint().toString());
                surname.setText(surname.getHint().toString());
                email.setText(email.getHint().toString());
                username.setText(username.getHint().toString());
                password.setText(password.getHint().toString());
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //back button
        if(id == android.R.id.home){
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
