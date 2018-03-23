package be.ehb.xplorebxl.View.Activities;

import android.Manifest;
import android.content.Context;
import android.app.FragmentTransaction;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
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
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Provider;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import be.ehb.xplorebxl.Database.LandMarksDatabase;
import be.ehb.xplorebxl.Model.Comic;
import be.ehb.xplorebxl.Model.Museum;
import be.ehb.xplorebxl.Model.StreetArt;
import be.ehb.xplorebxl.R;
import be.ehb.xplorebxl.Utils.RESTHandler;
import be.ehb.xplorebxl.View.Fragments.AboutFragment;
import be.ehb.xplorebxl.View.Fragments.ComicDetailFragment;
import be.ehb.xplorebxl.View.Fragments.ComicListViewFragment;
import be.ehb.xplorebxl.View.Fragments.ListviewItemListener;
import be.ehb.xplorebxl.View.Fragments.MuseumDetailFragment;
import be.ehb.xplorebxl.View.Fragments.MuseumListViewFragment;
import be.ehb.xplorebxl.View.Fragments.StreetArtDetailFragment;
import be.ehb.xplorebxl.View.Fragments.StreetArtListViewFragment;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by TDS-Team on 16/03/2018.
 */

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GoogleMap.OnMarkerClickListener, OnMapReadyCallback, ListviewItemListener {

    private String TAG = "testtest";

    public GoogleMap map;
    private HashMap<Marker, Object> objectLinkedToMarker;
    private Button btnCloseExtraFrag;
    private ArrayList<Target> targetComicList = new ArrayList<>();
    private ArrayList<Target> targetStreetartList = new ArrayList<>();
    private LocationManager locationManager;
    private LocationListener locationListener;
    private MapFragment mapFragment;
    private Marker selectedMarker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
              /*  Museum museum = LandMarksDatabase.getInstance(getApplicationContext()).getMuseums().get(0);
                if(museum != null) {
                   Location locationMuseum = new Location("locationMuseum");

                    locationMuseum.setLatitude(museum.getCoordX());
                    locationMuseum.setLongitude(museum.getCoordY());
                    Toast.makeText(getApplicationContext(),
                            "Distance to " + museum.getName() + " is equals to " + location.distanceTo(locationMuseum) + " m", Toast.LENGTH_SHORT).show();
                }*/
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
                    LocationManager.GPS_PROVIDER, 5000, 10, locationListener);
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
            downloadData();
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
            getFragmentManager().beginTransaction().replace(R.id.frag_container, MuseumListViewFragment.newInstance(locationManager)).addToBackStack("back").setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
            closeDetailFrag();
        } else if (id == R.id.nav_list_streetart) {
            getFragmentManager().beginTransaction().replace(R.id.frag_container, StreetArtListViewFragment.newInstance()).addToBackStack("back").setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
            closeDetailFrag();
        } else if (id == R.id.nav_list_comics) {
            getFragmentManager().beginTransaction().replace(R.id.frag_container, ComicListViewFragment.newInstance()).addToBackStack("back").setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
            closeDetailFrag();
        } else if (id == R.id.nav_about) {
            getFragmentManager().beginTransaction().replace(R.id.frag_container, AboutFragment.newInstance()).addToBackStack("back").setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
            closeDetailFrag();

        } else if (id == R.id.nav_update) {
            this.downloadData();
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

        setupMap();

    }

    private void setupMap() {
        drawMarkers();
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
           objectLinkedToMarker.put(map.addMarker(
                   new MarkerOptions()
                    .title(element.getName())
                    .position(element.getCoord())
                    .snippet("Click for more information")
                    .icon(BitmapDescriptorFactory.defaultMarker(180))),
                   element
                   );
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

/*GET ALL REST DATA***ET ALL REST DATA***ET ALL REST DATA***ET ALL REST DATA***ET ALL REST DATA***ET ALL REST DATA***ET ALL REST DATA***ET ALL REST DATA***ET ALL REST DATA***ET ALL REST DATA****/
    private void downloadData() {
        final RESTHandler handler = new RESTHandler(this);

        Thread backGroundThread = new Thread(new Runnable() {
            @Override
            public void run() {

                String url_museums = "https://opendata.brussel.be/api/records/1.0/search/?dataset=musea-in-brussel&rows=70";
                String url_streetArt = "https://opendata.brussel.be/api/records/1.0/search/?dataset=streetart&rows=70";
                String url_comic = "https://opendata.brussel.be/api/records/1.0/search/?dataset=comic-book-route&rows=70";

                ArrayList<String> urlList = new ArrayList<>();

                urlList.add(url_museums);
                urlList.add(url_streetArt);
                urlList.add(url_comic);


                for(String url: urlList) {

                    try {

                        OkHttpClient client = new OkHttpClient();

                        Request request = new Request.Builder()
                                .url(url)
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
            }
        }
        );
        backGroundThread.start();

    }
//BASED ON http://www.codexpedia.com/android/android-download-and-save-image-through-picasso/
    public void downloadImgs(List<String> imgs, String type) {
        ArrayList<String> imgUrlList = (ArrayList<String>) imgs;

        int img_length = imgUrlList.size();
        for(int i = 0; i < img_length; i++){

            String url = imgUrlList.get(i);
            if(!TextUtils.isEmpty(url)) {
                String imgId = url.split("files/")[1].split("[/]")[0];


                Uri uri = Uri.parse(url);

                Target target = getTarget(imgId);
                if(type == "streetart") {
                    targetStreetartList.add(target);
                    Picasso.with(getApplicationContext())
                            .load(uri)
                            .into(targetStreetartList.get(i));
                }else if( type == "comic"){
                    targetComicList.add(target);
                    Picasso.with(getApplicationContext())
                            .load(uri)
                            .into(targetComicList.get(i));
                }

            }else{
                Target target = getTarget("");
                if(type == "streetart") {
                    targetStreetartList.add(target);
                }else if( type == "comic"){
                    targetComicList.add(target);
                }
            }

        }

    }

    private Target getTarget(final String imgId) {
        Target target = new Target() {
            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        ContextWrapper cw = new ContextWrapper(getApplicationContext());
                        final File directory = cw.getDir("images", MODE_PRIVATE);

                        File file = new File(directory, imgId + ".jpeg");
                        try {
                            FileOutputStream ostream = new FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, ostream);
                            ostream.flush();
                            ostream.close();
                        } catch (IOException e) {
                            Log.e("IOException", e.getLocalizedMessage());
                        }
                    }
                }).start();
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };

        return target;
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


}
