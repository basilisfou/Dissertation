package com.example.stonesoup.dissertation;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by vasilis fouroulis on 19/12/2015.
 */
    public class AutoCheckIn extends IntentService {
    private String token;
    private static final int TIMEOUT = 1800000;
//    private static final int TIMEOUT = (int) TimeUnit.MINUTES.toMillis(1);

    private boolean res;
    private double latitude, longitude;
    private static String url = "http://83.212.113.58:3000/gpc/saveCoord";
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private LocationListener locationListener;
    private GoogleApiClient.ConnectionCallbacks mCallbacks;
    private GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener;
    private ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private Context context;
    private static final String TAG = "IntentServiceTAG";
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            start();
        }
    };
    private MediaPlayer mediaPlayer;

    public AutoCheckIn() {
        super("AutoCheckIn");
    }
    /** start the scheduler **/
    public void start(){
        getLocation();
    }

    // Invoked on the worker thread
    // Do some work in background without affecting the UI thread
    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG,"onHandleIntent");
        context = getApplicationContext();
        intent.getExtras();
        token = intent.getStringExtra("token");
        scheduler.scheduleWithFixedDelay(runnable, 0, TIMEOUT, TimeUnit.MILLISECONDS);
    }

    /*************************************************************************************************************************
     * ************************************************ Make a Call to Okeanos ***********************************************
     **************************************************************************************************************************/
    public void saveLocation(){
        final JSONObject jsonBody;

        try {
            Log.d(TAG,"longitude: "+longitude+",latitude: "+latitude);
            jsonBody = new JSONObject("{\"token\":\""+token+"\", \"longitude\":\""+String.valueOf(longitude)+"\",\"latitude\":\""+String.valueOf(latitude)+"\"}");
            //Making a standard request with JSON
            JsonObjectRequest request = new JsonObjectRequest
                    (Request.Method.POST,url, jsonBody , new Response.Listener<JSONObject>(){
                        @Override
                        public void onResponse(JSONObject response){
                            try {
                                res = response.getBoolean("res");

                                if(res) {
                                    //Save the token for this user in order to maintain session
                                    Log.d(TAG,response.getString("response"));
                                    Toast toast = Toast.makeText(getApplicationContext(),"Location Saved",Toast.LENGTH_LONG);
                                    toast.show();
                                    mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.saved);
                                    mediaPlayer.start();  /** Play sound when the location is saved **/
                                } else {

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.d(TAG, "JSONException: "+e.toString());
                            }
                        }
                    }, new Response.ErrorListener(){

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d(TAG,error.toString());
                        }
                    });

            Volley.newRequestQueue(this).add(request);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /*************************************************************************************************************************
     * ******************************************************** Get Location *************************************************
     **************************************************************************************************************************/
    public void getLocation(){
        mCallbacks = new GoogleApiClient.ConnectionCallbacks() {
            @Override
            public void onConnected(Bundle bundle) {
                mLocationRequest = LocationRequest.create();
                mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                mLocationRequest.setNumUpdates(1);
                mLocationRequest.setInterval(0);
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, locationListener);
            }

            @Override
            public void onConnectionSuspended(int i) {

            }
        };
        onConnectionFailedListener = new GoogleApiClient.OnConnectionFailedListener() {
            @Override
            public void onConnectionFailed(ConnectionResult connectionResult) {

            }
        };

        mGoogleApiClient = new GoogleApiClient.Builder(context).
                addApi(LocationServices.API)
                .addConnectionCallbacks(mCallbacks)
                .addOnConnectionFailedListener(onConnectionFailedListener)
                .build();


        mGoogleApiClient.connect();
        locationListener = new com.google.android.gms.location.LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.d(TAG,"onLocationChanged ");
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                Log.d(TAG, String.valueOf(latitude) + " " + String.valueOf(longitude));
                saveLocation();
            }
        };
    }
}

