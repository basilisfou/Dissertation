package com.example.stonesoup.dissertation.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.stonesoup.dissertation.MainActivity;
import com.example.stonesoup.dissertation.R;
import com.example.stonesoup.dissertation.adapter.DatesAdapter;
import com.example.stonesoup.dissertation.model.Dates;

import java.util.ArrayList;

/**
 * Created by vfour_000 on 14/2/2016.
 */
public class RecyclerFragment extends Fragment {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    DatesAdapter datesAdapter;
    private ArrayList<Dates> data;
    private String toolbarTitle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        savedInstanceState = getArguments();
        if(savedInstanceState !=null){
            data = (ArrayList<Dates>) savedInstanceState.getSerializable("data");
            toolbarTitle = savedInstanceState.getString("title");
            MainActivity.setmTitles(toolbarTitle);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent , Bundle savedInstanceState){

        //initialize the view v
        View v = inflater.inflate(R.layout.fragment_recycler,parent, false);

        recyclerView = (RecyclerView) v.findViewById(R.id.my_recycler_view);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        datesAdapter = new DatesAdapter(data);
        recyclerView.setAdapter(datesAdapter);

        return v;
    }

}
