package com.example.stonesoup.dissertation.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.stonesoup.dissertation.R;
import com.example.stonesoup.dissertation.model.Dates;

import java.util.ArrayList;

/**
 * Created by vfour_000 on 14/2/2016.
 */
public class DatesAdapter  extends RecyclerView.Adapter<DatesAdapter.viewHolder>  {

    private ArrayList<Dates> arrayList;

    public static class viewHolder extends RecyclerView.ViewHolder{
        public TextView date,longitude,latitude,place;

        public viewHolder(View v){
            super(v);
            date =(TextView) v.findViewById(R.id.date_tv);
            place = (TextView)v.findViewById(R.id.place_tv);
            longitude = (TextView)v.findViewById(R.id.longitude_tv);
            latitude = (TextView)v.findViewById(R.id.latitude_tv);
        }

    }
    public DatesAdapter(ArrayList<Dates> arrayList){
        this.arrayList = arrayList;
    }

    @Override
    public DatesAdapter.viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_date, parent, false);
        viewHolder vh = new viewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(DatesAdapter.viewHolder holder, int position) {
        Dates date = arrayList.get(position);
        holder.date.setText(date.getDate());
        holder.place.setText(date.getPlace());
        holder.latitude.setText(date.getLatitude().toString());
        holder.longitude.setText(date.getLongitude().toString());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
