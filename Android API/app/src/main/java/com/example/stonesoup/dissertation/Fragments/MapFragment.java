package com.example.stonesoup.dissertation.Fragments;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.stonesoup.dissertation.MainActivity;
import com.example.stonesoup.dissertation.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by vasilisfouroulis on 7/9/2015.
 */
public class MapFragment extends Fragment {

    private View v;
    com.google.android.gms.maps.MapFragment map;
    GoogleMap googleMap;
    private String token, url = "http://83.212.113.58:3000/gpc/getCoord";
    private SharedPreferences pref;
    private boolean res;
    private JSONArray jsonresponse;
    private SharedPreferences.Editor editor;


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        pref = getActivity().getSharedPreferences("sessionUser", 0);// 0 - for private mode
        editor = pref.edit();
        token = pref.getString("token", token);

        map = ((com.google.android.gms.maps.MapFragment) getChildFragmentManager().findFragmentById(R.id.map));
        googleMap = map.getMap();

        //Managing session
        pref = getActivity().getSharedPreferences("sessionUser", 0); // 0 - for private mode
        token = pref.getString("token", null);

        //zoom controls in the map
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        //control of the current location , pointer with the current location
        googleMap.setMyLocationEnabled(true);
        //compass enable
        googleMap.getUiSettings().isCompassEnabled();

        try {
            getCoordinates();
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        //initialize the view v
        v = inflater.inflate(R.layout.map_fragment, parent, false);
        MainActivity.setmTitles("Check in");

        return v;
    }

    public void getCoordinates() throws JSONException {
        final JSONObject jsonBody;
        //Log.d("billy", "longitude: " + String.valueOf(longitude) + " latitude: " + String.valueOf(latitude));

        try {
            jsonBody = new JSONObject("{\"token\":\"" + token + "\"}");
            Log.d("billy", jsonBody.toString());

            JsonObjectRequest request = new JsonObjectRequest
                    (Request.Method.POST, url, jsonBody, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                res = response.getBoolean("res");

                                if (res) {
                                    //Save the token for this user in order to maintain session
                                    //Log.d("billy",response.toString());
                                    jsonresponse = response.getJSONArray("response");
                                    Log.d("billy","Token found fetching locations of this token");
                                    for(int i =0;i<jsonresponse.length();i++ ){
                                        double latitude = jsonresponse.getJSONObject(i).getDouble("latitude");
                                        double longidude = jsonresponse.getJSONObject(i).getDouble("longidude");

                                        googleMap.addMarker(new MarkerOptions()
                                                .position(new LatLng(latitude, longidude)));
                                    }

                                } else {
                                    Log.d("billy","Token not found");
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                                //Log.d("billy", e.toString());
                            }
                            //into the app
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("billy", error.toString());
                        }
                    });

            Volley.newRequestQueue(getActivity()).add(request);

        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("billy",e.toString());
        }

    }

}
