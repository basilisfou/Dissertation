package com.example.stonesoup.dissertation;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;

public class LogIn extends AppCompatActivity {
    private Button buttonLogIn, buttonRegister, buttonForgotPass;
    private EditText editTextEmail, editTextPass;
    private String email , password, token, messageResponce, username;
    private Context context;
    //private String url = "http://192.168.145.2:3000/users/login"; //local host
    private String url = "http://83.212.113.58:3000/users/login";
    private boolean res;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        context = this;
        //session
        pref = context.getSharedPreferences("sessionUser", 0);// 0 - for private mode
        editor = pref.edit();
        token = pref.getString("token",token);

        //Already the user is signed in
        if(token != null){
            Intent intent = new Intent(context, MainActivity.class);
            startActivity(intent);
            this.finish();
        }
        // initialise widgets
        buttonForgotPass = (Button)findViewById(R.id.forgot_pass);
        buttonLogIn = (Button)findViewById(R.id.buttonLogin);
        buttonRegister = (Button)findViewById(R.id.buttonRegister);
        editTextEmail = (EditText)findViewById(R.id.editTextEmail);
        editTextPass = (EditText)findViewById(R.id.editTextPassword);

        //Checking if the device is connected to the internet
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(context.CONNECTIVITY_SERVICE);
        final NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        buttonLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //assign value of edit text in String in order to send them to the server - validation server side
                email = editTextEmail.getText().toString();
                //Log.d("billy",email);
                password = editTextPass.getText().toString();

                //validation client side
                if(email != null && password != null ) {
                    //where the request lives
                    if(networkInfo != null && networkInfo.isConnected()){
                        //request body
                        final JSONObject jsonBody;

                        try {
                            jsonBody = new JSONObject("{\"email\":\""+email+"\", \"passWord\":\""+password+"\"}");

                            //Making a standard request with JSON
                            JsonObjectRequest request = new JsonObjectRequest
                                    (Request.Method.POST,url, jsonBody , new Response.Listener<JSONObject>(){

                                        @Override
                                        public void onResponse(JSONObject response){
                                            try {
                                                messageResponce = response.getString("response");
                                                res = response.getBoolean("res");
                                                token = response.getString("token");
                                                username = response.getString("username");

                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                                //Log.d("billy", e.toString());
                                            }
                                            //into the app
                                            if(res) {
                                                //Save the token for this user in order to maintain session
                                                editor.putString("token",token);
                                                editor.putString("username",username);
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
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }

                }
                else {
                    Toast toast = Toast.makeText(context,"Please give a User name and Password",Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });
        buttonRegister.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,Register.class);
                startActivity(intent);

            }
        });

        buttonForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,ResetPassword.class);
                startActivity(intent);
            }
        });

    }
}
