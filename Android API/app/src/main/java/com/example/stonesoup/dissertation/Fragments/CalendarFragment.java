package com.example.stonesoup.dissertation.Fragments;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.stonesoup.dissertation.MainActivity;
import com.example.stonesoup.dissertation.R;
import com.example.stonesoup.dissertation.model.Dates;

import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by vasilis fouroulis  7/9/2015.
 */
public class CalendarFragment extends Fragment {

    private View v;
    private CalendarView calendar;
    private TextView textView;
    private JSONArray jsonresponse;
    private boolean res;
    private SharedPreferences.Editor editor;
    private String token;
    private static final String URL = "http://83.212.113.58:3000/gpc/getCoordDate";
    private SharedPreferences pref;
    private ArrayList<Dates> listOfDates = new ArrayList<>();
    private ProgressDialog progressDialog;
    private Boolean clicked = true;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent , Bundle savedInstanceState){

        //initialize the view v
        v = inflater.inflate(R.layout.calendar_fragment,parent, false);
        MainActivity.setmTitles("Calendar");
        progressDialog = new ProgressDialog(getActivity());
        textView = (TextView)v.findViewById(R.id.tv_date);
        pref = getActivity().getSharedPreferences("sessionUser", 0);// 0 - for private mode
        editor = pref.edit();
        token = pref.getString("token", token);
        //initializes the calendarView
        initializeCalendar(v);
        return v;
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

    /**
     * Method that initialize the calendar view
     */
    public void initializeCalendar(View view){
        calendar = (CalendarView)view.findViewById(R.id.calendar);

        // sets whether to show the week number.
        calendar.setShowWeekNumber(false);

        // sets the first day of the of the week according the Calendar , here we set the MONDAY
        calendar.setFirstDayOfWeek(2);

        //setting the Seperator between weeks
        calendar.setWeekSeparatorLineColor(Color.WHITE);

        //setting the selected week color
        calendar.setSelectedWeekBackgroundColor(getResources().getColor(R.color.SelectedWeekColor));

        calendar.setFocusedMonthDateColor(getResources().getColor(R.color.SelectMonthColor));
        //sets the listener to be notified upon selected date change.
        calendar.setOnDateChangeListener(new OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month , int dayOfMonth) {

                try {
                    if(clicked) {
                        clicked = false;
                        month = month+1;
                        getCoordinates(dayOfMonth + "/" + month + "/" + year);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                textView.setText(String.valueOf(dayOfMonth) +" "+String.valueOf(month)+" "+String.valueOf(year),TextView.BufferType.NORMAL);
            }
        });
    }

    public void getCoordinates(final String date) throws JSONException {
        final JSONObject jsonBody;
        //Log.d("billy", "longitude: " + String.valueOf(longitude) + " latitude: " + String.valueOf(latitude));
        Log.d("billy",date);
        try {
            jsonBody = new JSONObject("{\"token\":\"" + token + "\",\"date\":\""+ date +"\"}");
            //Log.d("billy", jsonBody.toString());

            JsonObjectRequest request = new JsonObjectRequest
                    (Request.Method.POST, URL, jsonBody, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                res = response.getBoolean("res");

                                if (res) {
                                    //Save the token for this user in order to maintain session
                                    //Log.d("billy",response.toString());
                                    jsonresponse = response.getJSONArray("response");
                                    Log.d("billy","Token found fetching locations of this token");
                                    if(jsonresponse.length() > 0) {
                                        progressDialog.show();
                                        for (int i = 0; i < jsonresponse.length(); i++) {
                                            double latitude = jsonresponse.getJSONObject(i).getDouble("latitude");
                                            double longidude = jsonresponse.getJSONObject(i).getDouble("longidude");
                                            //Log.d("billy",String.valueOf(latitude)+" "+String.valueOf(longidude));
                                            Dates d = new Dates();
                                            d.setDate(date);
                                            d.setLatitude(latitude);
                                            d.setLongitude(longidude);
                                            listOfDates.add(d);

                                        }
                                        Bundle lbundle = new Bundle();
                                        lbundle.putSerializable("data", listOfDates);
                                        lbundle.putString("title",date);
                                        //to the Fragment Recycler View
                                        RecyclerFragment fragment = new RecyclerFragment();
                                        fragment.setArguments(lbundle);
                                        getFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
                                        progressDialog.dismiss();
                                    } else {
                                        clicked = true;
                                        textView.setText("No location found for this day");
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
