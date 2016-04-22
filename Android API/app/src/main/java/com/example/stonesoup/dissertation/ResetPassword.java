package com.example.stonesoup.dissertation;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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

public class ResetPassword extends AppCompatActivity {
    private Toolbar mToolbar;
    private TextView mTitles;
    private Context context;
    private Button resetButton;
    private String emailString, usernameString,  messageResponce;
    private EditText email, username;
    private final static String URL = "http://83.212.113.58:3000/users/resetpass";
    private Boolean res;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        context = getApplicationContext();
        /**
         * Toolbar customization
         */
        mToolbar = (Toolbar) findViewById(R.id.toolbar); //replacing the old Action bar
        setSupportActionBar(mToolbar);

        //Checking if the device is connected to the internet
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(context.CONNECTIVITY_SERVICE);
        final NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        mTitles = (TextView) mToolbar.findViewById(R.id.toolbar_title);
        mTitles.setText("Reset Password");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        resetButton = (Button)findViewById(R.id.resetbutton01);
        email = (EditText)findViewById(R.id.resetEmail);
        username = (EditText)findViewById(R.id.resetUserName);

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                emailString = email.getText().toString();
                usernameString = username.getText().toString();
                //validation from the mobile client
                if(emailString != null ) {
                    if(networkInfo != null && networkInfo.isConnected()) {
                        //request body
                        final JSONObject jsonBody;

                        try{
                            jsonBody = new JSONObject("{\"userName\":\""+usernameString+"\", \"email\":\""+emailString+"\"}");

                            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,URL,jsonBody,
                                    new Response.Listener<JSONObject>(){

                                        @Override
                                        public void onResponse(JSONObject response) {
                                            try{
                                                messageResponce = response.getString("response");
                                                res = response.getBoolean("res");

                                            }catch (JSONException e){
                                                e.printStackTrace();
                                            }
                                                if(res){
                                                    Toast toast = Toast.makeText(context,messageResponce,Toast.LENGTH_LONG);
                                                    toast.show();
                                                    //
                                                    Intent intent = new Intent(context, LogIn.class);
                                                    startActivity(intent);
                                                } else {
                                                    Toast toast = Toast.makeText(context,messageResponce,Toast.LENGTH_LONG);
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

                        }
                    }
                } else {
                    Toast toast = Toast.makeText(context,"Please give a Username or Password",Toast.LENGTH_LONG);
                    toast.show();
                }
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
