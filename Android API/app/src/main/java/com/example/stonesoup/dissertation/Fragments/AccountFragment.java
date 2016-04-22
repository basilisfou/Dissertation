package com.example.stonesoup.dissertation.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.stonesoup.dissertation.MainActivity;
import com.example.stonesoup.dissertation.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by vasilis fouroulis on 7/9/2015.
 */
public class AccountFragment extends Fragment {

    private View v;
    private EditText username, email, oldpassword,newPassword;
    private Button changePasword;
    private String usermaneString, emailString, oldPasswordString, passwordNewString, messageResponse;
    private Context context;
    private Boolean res;
    private static final String URL = "http://83.212.113.58:3000/users/changepass";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity.setmTitles("Account");
        context = getActivity();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent , Bundle savedInstanceState){

        //initialize the view v
        v = inflater.inflate(R.layout.account_fragment,parent, false);
        username = (EditText)v.findViewById(R.id.cp_username);
        email = (EditText)v.findViewById(R.id.cp_email);
        oldpassword = (EditText)v.findViewById(R.id.cp_oldPassword);
        newPassword = (EditText)v.findViewById(R.id.cp_newPassword);
        changePasword = (Button)v.findViewById(R.id.cp_changePassword);

        changePasword.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                usermaneString = username.getText().toString();
                emailString = email.getText().toString();
                oldPasswordString = oldpassword.getText().toString();
                passwordNewString = newPassword.getText().toString();

                if(usermaneString != null && emailString != null && oldPasswordString != null && passwordNewString != null){
                    //request body
                    final JSONObject jsonBody;

                    try {
                        jsonBody = new JSONObject("{\"userName\":\""+usermaneString+"\", \"email\":\""+emailString+"\"," +
                                "\"oldPassword\":\""+oldPasswordString+"\", \"newPassword\":\""+passwordNewString+"\" }");
                        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,URL,jsonBody, new Response.Listener<JSONObject>(){

                            @Override
                            public void onResponse(JSONObject response){
                                try {
                                    messageResponse = response.getString("response");
                                    res = response.getBoolean("res");


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    //Log.d("billy", e.toString());
                                }
                                //into the app
                                if(res) {
                                    Toast toast = Toast.makeText(context, messageResponse,Toast.LENGTH_LONG);
                                    toast.show();

                                    Intent intent = new Intent(context, MainActivity.class);
                                    startActivity(intent);
                                } else {
                                    //error message from the server
                                    Toast toast = Toast.makeText(context, messageResponse,Toast.LENGTH_LONG);
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
                } else {
                    Toast toast = Toast.makeText(context,"please fill all the fields",Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });

        return  v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        //the button search is setting to visible

        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar actions click
        switch (item.getItemId()) {
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
