package com.example.stonesoup.dissertation.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.stonesoup.dissertation.R;
import com.example.stonesoup.dissertation.model.DrawerItem;
import java.util.ArrayList;

/*
******************************
*  8         8    888888888  *
*   8       8     8          *
*    8     8      888888888  *
*     8   8       8          *
*      8 8        8          *
*       8     .   8         .*
******************************
* vassilis Fouroulis
 */
public class RecyclerViewerAdapter extends RecyclerView.Adapter<RecyclerViewerAdapter.CommentViewHolder> {
    private ArrayList<DrawerItem> mList;
    public static OnItemCustomClickListener listener;// Define listener member variable ( Composition )

    /** Defines the listener interface - inner interface */
    public interface OnItemCustomClickListener {
        void onItemClick(View itemView, int position);
    }
    /**Sets the custom Listener - Define the method that allows the parent activity or fragment to define the listener*/
    public void setOnItemCustomClickListener(OnItemCustomClickListener listener){
        this.listener = listener;
    }
    //Constructor initialise the list
    public RecyclerViewerAdapter(ArrayList<DrawerItem> pList){
        this.mList = pList;
    }
    /** View holder - holds the views of the navigation items*/
    public class CommentViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageview;
        public TextView textView;
        //constructor of the inner class
        public CommentViewHolder(final View itemView){
            super(itemView);
            // Find the TextView in the LinearLayout
            imageview = (ImageView)itemView.findViewById(R.id.nv_image);
            textView = (TextView)itemView.findViewById(R.id.nv_title);
            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    // Triggers click upwards to the adapter on click
                    if(listener != null)
                        listener.onItemClick(itemView,getAdapterPosition());
                }

            });
        }
    }
    /** Replace the contents of a view (invoked by the layout manager */
    @Override
    public RecyclerViewerAdapter.CommentViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view;
        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.navigation_item, viewGroup, false);
        CommentViewHolder commentViewHolder = new CommentViewHolder(view);
        return commentViewHolder;
    }
    @Override
    public void onBindViewHolder(CommentViewHolder Holder, int position) {
        final  CommentViewHolder commentViewHolder2 = Holder;
        DrawerItem item2 = mList.get(position);
        commentViewHolder2.textView.setText(item2.getTitle());
        commentViewHolder2.imageview.setImageResource(item2.getIcom());
    }
    @Override
    public int getItemCount() {
        return mList.size();
    }
}
