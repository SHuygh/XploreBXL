package be.ehb.xplorebxl.View.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import be.ehb.xplorebxl.Database.LandMarksDatabase;
import be.ehb.xplorebxl.Model.Museum;
import be.ehb.xplorebxl.R;
import be.ehb.xplorebxl.View.Fragments.AboutFragment;
import be.ehb.xplorebxl.View.Fragments.ListViewFragment;

/**
 * Created by TDS-Team on 16/03/2018.
 */

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GoogleMap.OnInfoWindowClickListener, OnMapReadyCallback {

    private GoogleMap map;
    private MapFragment mapFragment;
    private HashMap<Marker, Object > objectLinkedToMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        //MAPS
        mapFragment = new MapFragment();
        mapFragment.getMapAsync(this);

        getFragmentManager().beginTransaction().replace(R.id.frag_container, mapFragment).commit();

        objectLinkedToMarker = new HashMap<>();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_map) {
            getFragmentManager().beginTransaction().replace(R.id.frag_container, mapFragment).commit();

            setupMap();
        } else if (id == R.id.nav_list) {
            getFragmentManager().beginTransaction().replace(R.id.frag_container, ListViewFragment.newInstance()).commit();

        } else if (id == R.id.nav_about) {
            getFragmentManager().beginTransaction().replace(R.id.frag_container, AboutFragment.newInstance()).commit();

        } else if (id == R.id.nav_settings) {
            Toast.makeText(getApplicationContext(), "SETTING SCHERM AANMAKEN!!!", Toast.LENGTH_LONG).show();
            //SETTINGSFRAGMENT
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        setupMap();
    }

    private void setupMap() {
        Log.d("testtest", "setupMap: ");
        drawMarkers();
        map.setOnInfoWindowClickListener(this);
    }

    private void drawMarkers() {
        List<Museum> museums = LandMarksDatabase.getInstance(this).getMuseums();

        for(Museum element: museums){
           objectLinkedToMarker.put(map.addMarker(
                   new MarkerOptions()
                    .title(element.getName())
                    .position(element.getCoord())
                    .snippet("Click for more information")
                    .icon(BitmapDescriptorFactory.defaultMarker(180))),
                   element
                   );
            }
        }


    @Override
    public void onInfoWindowClick(Marker marker) {
        Log.d("testtest", "onInfoWindowClick: ");

        Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
        Object selectedObject = (objectLinkedToMarker.get(marker).getClass()).cast(objectLinkedToMarker.get(marker));
        intent.putExtra("selected object", (Serializable) objectLinkedToMarker.get(marker).getClass().cast(objectLinkedToMarker.get(marker)));
        startActivity(intent);
    }
}
