package be.ehb.xplorebxl.View.Activities;

import android.Manifest;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import be.ehb.xplorebxl.Database.LandMarksDatabase;
import be.ehb.xplorebxl.Model.Comic;
import be.ehb.xplorebxl.Model.Museum;
import be.ehb.xplorebxl.Model.StreetArt;
import be.ehb.xplorebxl.R;
import be.ehb.xplorebxl.Utils.Downloader;
import be.ehb.xplorebxl.Utils.ListviewItemListener;
import be.ehb.xplorebxl.View.Fragments.AboutFragment;
import be.ehb.xplorebxl.View.Fragments.ComicDetailFragment;
import be.ehb.xplorebxl.View.Fragments.ComicListViewFragment;
import be.ehb.xplorebxl.View.Fragments.MuseumDetailFragment;
import be.ehb.xplorebxl.View.Fragments.MuseumListViewFragment;
import be.ehb.xplorebxl.View.Fragments.StreetArtDetailFragment;
import be.ehb.xplorebxl.View.Fragments.StreetArtListViewFragment;

/**
 * Created by TDS-Team on 16/03/2018.
 */

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GoogleMap.OnMarkerClickListener, OnMapReadyCallback, ListviewItemListener {

    private Downloader downloader;
    private String TAG = "testtest";

    public GoogleMap map;
    private HashMap<Marker, Object> objectLinkedToMarker;
    private ImageButton btnCloseExtraFrag;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private MapFragment mapFragment;
    private Marker selectedMarker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        downloader = Downloader.getInstance(this);

        setupDrawer();
        setupMaps();
        setupCloseDetailFrag();
        checkHasDownloadedBefore();
        setupLocationServices();

    }

    private void setupLocationServices() {
        locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {
            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, 1000, 5, locationListener);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "No GPS LOCATION", Toast.LENGTH_LONG).show();
                        return;
                    } else {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER, 5000, 10, locationListener);
                    }
                }
        }
    }

    private void checkHasDownloadedBefore() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        if (sharedPreferences.getBoolean("AppHasDownloadedDataBefore", false)) {
        } else {
            downloader.downloadData();
        }
    }

    private void setupCloseDetailFrag() {

        btnCloseExtraFrag = findViewById(R.id.btn_main_close);

        btnCloseExtraFrag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeDetailFrag();
            }
        });
    }

    private void setupMaps() {
        mapFragment = new MapFragment();
        mapFragment.getMapAsync(this);

        getFragmentManager().beginTransaction().replace(R.id.frag_container, mapFragment).commit();

        objectLinkedToMarker = new HashMap<>();
    }

    private void setupDrawer() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        locationManager.removeUpdates(locationListener);
    }


    /*NAVIGATION****NAVIGATION****NAVIGATION****NAVIGATION****NAVIGATION****NAVIGATION****NAVIGATION****NAVIGATION****NAVIGATION****NAVIGATION****NAVIGATION****NAVIGATION*****/

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_map) {
            getFragmentManager().beginTransaction().replace(R.id.frag_container, mapFragment).commit();

            setupMap();

            closeDetailFrag();
        } else if (id == R.id.nav_list) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.frag_container, MuseumListViewFragment.newInstance(locationManager))
                    .addToBackStack("back")
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
            closeDetailFrag();
        } else if (id == R.id.nav_list_streetart) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.frag_container, StreetArtListViewFragment.newInstance(locationManager))
                    .addToBackStack("back")
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
            closeDetailFrag();
        } else if (id == R.id.nav_list_comics) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.frag_container, ComicListViewFragment.newInstance(locationManager))
                    .addToBackStack("back")
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
            closeDetailFrag();
        } else if (id == R.id.nav_about) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.frag_container, AboutFragment.newInstance())
                    .addToBackStack("back")
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
            closeDetailFrag();

        } else if (id == R.id.nav_update) {
            downloader.downloadData();
            Toast.makeText(this, "Updating Data...", Toast.LENGTH_LONG).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void closeDetailFrag() {
        findViewById(R.id.detail_frag_container).setVisibility(View.GONE);
        btnCloseExtraFrag.setVisibility(View.GONE);
        cancelSelectedMarker();
    }


    /*MAP METHODS****MAP METHODS****MAP METHODS****MAP METHODS****MAP METHODS****MAP METHODS****MAP METHODS****MAP METHODS****MAP METHODS****MAP METHODS****MAP METHODS****MAP METHODS*****/
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        /** Map Styling  **/

        try {
            // Customise the styling of the base map using a JSON object defined
         // in a raw resource file.
         boolean success = googleMap.setMapStyle(
         MapStyleOptions.loadRawResourceStyle(
         this, R.raw.style_json));

         if (!success) {
         Log.e(TAG, "Style parsing failed.");
         }
         } catch (Resources.NotFoundException e) {
         Log.e(TAG, "Can't find style. Error: ", e);
         }



        drawMarkers();

        setupMap();

    }

    private void setupMap() {
        //map.setOnInfoWindowClickListener(this);
        map.setOnMarkerClickListener(this);
        updateCamera();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        }else {
            map.setMyLocationEnabled(true);
            map.getUiSettings().setMyLocationButtonEnabled(true);
        }

    }

    public void drawMarkers() {
        List<Museum> museums = LandMarksDatabase.getInstance(this).getMuseums();

        for(Museum element: museums){

            Marker marker = map.addMarker(
                    new MarkerOptions()
                            .title(element.getName())
                            .position(element.getCoord())
                            .snippet("Click for more information")
                            .icon(BitmapDescriptorFactory.defaultMarker(180)));

            objectLinkedToMarker.put(marker,element);
            }

         List<StreetArt> streetArtList = LandMarksDatabase.getInstance(this).getAllStreetArt();

        for(StreetArt element: streetArtList){
            objectLinkedToMarker.put(map.addMarker(
                    new MarkerOptions()
                            .title(element.getNameOfArtist())
                            .position(element.getCoord())
                            .snippet("Click for more information")
                            .icon(BitmapDescriptorFactory.defaultMarker(90))),
                    element
            );
        }

        List<Comic> comicsList = LandMarksDatabase.getInstance(this).getAllComics();

        for (Comic element: comicsList){

            objectLinkedToMarker.put(map.addMarker(
                    new MarkerOptions()
                            .title(element.getNameOfIllustrator())
                            .position(element.getCoord())
                            .snippet("Click for more information")
                            .icon(BitmapDescriptorFactory.defaultMarker(10))),
                            element);
        }



        }

    private void updateCamera() {

        List<Museum> museums = LandMarksDatabase.getInstance(this).getMuseums();

        if(museums.size() > 0){
            LatLng coord = museums.get(0).getCoord();
                if(coord != null){
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(coord, 13);

                    map.animateCamera(cameraUpdate);
                }
        }
    }

    /*MARKER SELECT***MARKER SELECT***MARKER SELECT***MARKER SELECT***MARKER SELECT***MARKER SELECT***MARKER SELECT***MARKER SELECT***MARKER SELECT***MARKER SELECT***MARKER SELECT*/

    @Override
    public boolean onMarkerClick(Marker marker) {
        updateSelectedMarker(marker);


        return true;
    }


    @Override
    public void itemSelected(Object o) {
        Marker marker = null;
        getFragmentManager().beginTransaction().replace(R.id.frag_container, mapFragment).commit();

        LOOP: for(Marker element: objectLinkedToMarker.keySet()){
            if(o.equals(objectLinkedToMarker.get(element))){
                marker = element;
                break LOOP;
            }
        }

        if(marker != null) {
            updateSelectedMarker(marker);
        }
    }

    public void updateSelectedMarker(Marker marker) {

        cancelSelectedMarker();

        selectedMarker = marker;

        marker.setIcon(BitmapDescriptorFactory.defaultMarker(50));
        CameraUpdate cu = CameraUpdateFactory.newLatLngZoom((marker.getPosition()),18);
        map.animateCamera(cu);

        openDetailFragment(marker);
    }

    public void cancelSelectedMarker() {

        if(selectedMarker != null){

            Object o = objectLinkedToMarker.get(selectedMarker);

            float hue = 0;
            if(o instanceof Museum){
                hue = 180;
            }else if(o instanceof StreetArt){
                hue = 90;
            }else if(o instanceof Comic){
                hue = 10;
            }
            selectedMarker.setIcon(BitmapDescriptorFactory.defaultMarker(hue));

            selectedMarker = null;

        }
    }

    public void openDetailFragment(Marker marker) {
        findViewById(R.id.detail_frag_container).setVisibility(View.VISIBLE);
        btnCloseExtraFrag.setVisibility(View.VISIBLE);

        Object objectClicked = objectLinkedToMarker.get(marker);


        if(objectClicked instanceof Museum){
            getFragmentManager().beginTransaction().replace(R.id.detail_frag_container, MuseumDetailFragment.newInstance((Museum) objectClicked)).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
        }else if(objectClicked instanceof StreetArt){
            getFragmentManager().beginTransaction().replace(R.id.detail_frag_container, StreetArtDetailFragment.newInstance((StreetArt) objectClicked)).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
        }else if (objectClicked instanceof Comic){
            getFragmentManager().beginTransaction().replace(R.id.detail_frag_container, ComicDetailFragment.newInstance((Comic) objectClicked)).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
        }
    }

    /*FILTER MARKERS***FILTER MARKERS***FILTER MARKERS***FILTER MARKERS***FILTER MARKERS***FILTER MARKERS***FILTER MARKERS***FILTER MARKERS***FILTER MARKERS***FILTER MARKERS***FILTER MARKERS***/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        final int id = item.getItemId();
        if (id == R.id.mi_marker_filter){

            PopupMenu popup = new PopupMenu(this, findViewById(R.id.mi_marker_filter));
            popup.getMenuInflater().inflate(R.menu.marker_list, popup.getMenu());

            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                @Override
                public boolean onMenuItemClick(MenuItem item) {

                    switch (item.getItemId()){
                        case R.id.pu_all:
                            for(Marker element: objectLinkedToMarker.keySet()){
                                    element.setVisible(true);
                            }
                            break;
                        case R.id.pu_musea:
                            filterMarker(Museum.class);
                            break;
                        case R.id.pu_comic:
                            filterMarker(Comic.class);
                            break;
                        case R.id.pu_streetart:
                            filterMarker(StreetArt.class);
                            break;
                    }

                    return true;
                }
            });
            popup.show();
        }

        return super.onOptionsItemSelected(item);
    }

    public void filterMarker(Class cls) {

        for(Marker element: objectLinkedToMarker.keySet()){
            if(objectLinkedToMarker.get(element).getClass().equals(cls)){
                element.setVisible(true);
            }else{
                element.setVisible(false);
            }
        }

    }

}
