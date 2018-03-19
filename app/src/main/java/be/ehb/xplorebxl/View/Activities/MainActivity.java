package be.ehb.xplorebxl.View.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import be.ehb.xplorebxl.Database.LandMarksDatabase;
import be.ehb.xplorebxl.Model.Museum;
import be.ehb.xplorebxl.Model.StreetArt;
import be.ehb.xplorebxl.R;
import be.ehb.xplorebxl.Utils.MuseumHandler;
import be.ehb.xplorebxl.View.Fragments.AboutFragment;
import be.ehb.xplorebxl.View.Fragments.DetailFragment;
import be.ehb.xplorebxl.View.Fragments.ListViewFragment;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by TDS-Team on 16/03/2018.
 */

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GoogleMap.OnInfoWindowClickListener, OnMapReadyCallback {

    private GoogleMap map;
    private MapFragment mapFragment;
    //private MapFragment mapFragment;
    private HashMap<Marker, Object> objectLinkedToMarker;
    private  Button btnCloseExtraFrag;
    private MuseumHandler handler;

    private Object objectClicked;

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

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        //MAPS
        MapFragment mapFragment = new MapFragment();
        mapFragment.getMapAsync(this);

        getFragmentManager().beginTransaction().replace(R.id.frag_container, mapFragment).commit();

        objectLinkedToMarker = new HashMap<>();

        btnCloseExtraFrag = findViewById(R.id.btn_main_close);

        btnCloseExtraFrag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.detail_frag_container).setVisibility(View.GONE);
                btnCloseExtraFrag.setVisibility(View.GONE);
            }
        });

        handler = new MuseumHandler(getApplicationContext());

        downloadData();

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
           // getFragmentManager().beginTransaction().replace(R.id.frag_container, mapFragment).commit();
            //setupMap();

            MapFragment mapFragment = new MapFragment();
            mapFragment.getMapAsync(this);

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
        updateCamera();
    }

    private void setupMap() {
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
        //List<StreetArt> streetArtList = LandMarksDatabase.getInstance(this)


        }

    private void updateCamera() {

        List<Museum> museums = LandMarksDatabase.getInstance(this).getMuseums();

        if(museums.size() > 0){
            LatLng coord = museums.get(0).getCoord();
                if(coord != null){
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(coord, 16);

                    map.animateCamera(cameraUpdate);
                }
        }
    }


    @Override
    public void onInfoWindowClick(Marker marker) {

        findViewById(R.id.detail_frag_container).setVisibility(View.VISIBLE);
        btnCloseExtraFrag.setVisibility(View.VISIBLE);

        objectClicked = objectLinkedToMarker.get(marker);

        if(objectClicked instanceof Museum){
            getFragmentManager().beginTransaction().replace(R.id.detail_frag_container, DetailFragment.newInstance()).commit();
        }else if(objectClicked instanceof StreetArt){
            getFragmentManager().beginTransaction().replace(R.id.detail_frag_container, DetailFragment.newInstance()).commit();
        }



  /*      Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
        Object selectedObject = (objectLinkedToMarker.get(marker).getClass()).cast(objectLinkedToMarker.get(marker));
        intent.putExtra("selected object", (Serializable) objectLinkedToMarker.get(marker).getClass().cast(objectLinkedToMarker.get(marker)));
        startActivity(intent);*/
    }


    private void downloadData() {
        Thread backGroundThread = new Thread(new Runnable() {
            @Override
            public void run() {

                try {

                    OkHttpClient client = new OkHttpClient();

                    Request request = new Request.Builder()
                            .url("https://opendata.brussel.be/api/records/1.0/search/?dataset=musea-in-brussel&rows=70")
                            .get()
                            .build();


                    Response response = client.newCall(request).execute();

                    Message msg = new Message();

                    Bundle bndl = new Bundle();
                    bndl.putString("json_data", response.body().string());
                    msg.setData(bndl);


                    handler.sendMessage(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
        backGroundThread.start();
    }
}
