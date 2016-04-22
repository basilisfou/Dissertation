package com.example.stonesoup.dissertation;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stonesoup.dissertation.Fragments.AboutFragment;
import com.example.stonesoup.dissertation.Fragments.AccountFragment;
import com.example.stonesoup.dissertation.Fragments.CalendarFragment;
import com.example.stonesoup.dissertation.Fragments.MapFragment;
import com.example.stonesoup.dissertation.adapter.RecyclerViewerAdapter;
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
public class MainActivity extends AppCompatActivity {
    private DrawerLayout mDrawer; //The side bar
    private RecyclerView mRecyclerView; // widget that holds the Items of the side bar
    private RecyclerViewerAdapter mAdapter; //Adapter of the Navigation Drawer, helps d
    private LinearLayoutManager mLayoutManager; // The manager that is responsible about how the items will be displayed
    private ArrayList<DrawerItem> mList; // List that holds the items of the Group
    private ActionBarDrawerToggle mDrawerToggle; //The three lines that when pressed the Navigation Drawer is being opened
    private Toolbar mToolbar;
    private String[] navMenuItems;
    private TypedArray navMenuIcons;
    private static TextView mTitles;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private String token;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /**
         * Toolbar customization
         */
        mToolbar = (Toolbar) findViewById(R.id.toolbar); //replacing the old Action bar
        mTitles = (TextView) mToolbar.findViewById(R.id.toolbar_title);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);//disable toolbar title
        mTitles.setText("Map Calendar");
        //Managing Session
        pref = this.getSharedPreferences("sessionUser", 0);// 0 - for private mode
        editor = pref.edit();
        token = pref.getString("token",token);
        //initialise the navigation drawer
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        /**
         * initialise the Recycler viewer - layout manager - the adapter
         */
        mRecyclerView = (RecyclerView) findViewById(R.id.listItem); //Assigning  the recycler view with the XML view
        populateNavigationItems(); // populate the list
        mLayoutManager = new LinearLayoutManager(this); // initialise a new Linear Layout manager
        mRecyclerView.setLayoutManager(mLayoutManager); //setting the layout manager to the recycler view
        mAdapter = new RecyclerViewerAdapter(mList); //setting a new Adapter
        mRecyclerView.setAdapter(mAdapter); //setting the adapter to the Recycler view
        mAdapter.setOnItemCustomClickListener(new RecyclerViewerAdapter.OnItemCustomClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                onListViewItemClick(position);
            }
        });
        //initialise the up bar toggle
        mDrawerToggle = new ActionBarDrawerToggle(
                this, // the Main Activity
                mDrawer, //the Drawer
                mToolbar,// the action bar
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                setmTitles("Map Calendar");
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                setmTitles("Side Bar");
                invalidateOptionsMenu();
            }
        };
        mDrawerToggle.setHomeAsUpIndicator(R.drawable.ic_drawer);
        mDrawer.setDrawerListener(mDrawerToggle);

        // enabling action bar app icon and behaving it as toggle button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        //start the service to automatically check in
        Intent intentService = new Intent(this,AutoCheckIn.class);
        intentService.putExtra("token", token);
        startService(intentService);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // toggle nav drawer on selecting action bar app icon/title
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurSationChanged()...
     */
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    /**
     * populate the Navigation Items
     */
    public void populateNavigationItems() {
        mList = new ArrayList<>();
        navMenuItems = getResources().getStringArray(R.array.titles);
        navMenuIcons = getResources().obtainTypedArray(R.array.icons);

        for (int i = 0; i < navMenuItems.length; i++) {
            mList.add(new DrawerItem(navMenuItems[i], navMenuIcons.getResourceId(i, -1)));
        }

        // Recycle the typed array
        navMenuIcons.recycle();
    }

    /**
     * When clicking to an NAVIGATION ITEM from the navigation drawer
     */
    public void onListViewItemClick(final int position) {

        DrawerItem data = mList.get(position);
        Bundle lbundle = new Bundle();
        lbundle.putSerializable("data", data);
        if (data.getTitle().equals("Account")) {
            AccountFragment fragment = new AccountFragment();
            fragment.setArguments(lbundle);
            getFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
            mDrawer.closeDrawers();
        } else if (data.getTitle().equals("Map")) {
            MapFragment fragment = new MapFragment();
            fragment.setArguments(lbundle);
            getFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
            mDrawer.closeDrawers();
        } else if (data.getTitle().equals("Timeline")) {
            CalendarFragment fragment = new CalendarFragment();
            fragment.setArguments(lbundle);
            getFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
            mDrawer.closeDrawers();
        } else if (data.getTitle().equals("About")) {
            AboutFragment fragment = new AboutFragment();
            fragment.setArguments(lbundle);
            getFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
            mDrawer.closeDrawers();
        } else if(data.getTitle().equals("Sign Out")){
            // sign out - delete token from the device
            editor.clear();
            editor.commit();
            //
            Toast toast = Toast.makeText(this, "Goodbuy :( ",Toast.LENGTH_LONG);
            toast.show();
            //
            Intent intent = new Intent(this, LogIn.class);
            startActivity(intent);
        }
    }
    /**
     * Changing the title of the Action bar
     */
    public static void setmTitles(String mTitles) {
        MainActivity.mTitles.setText(mTitles);
    }
}
