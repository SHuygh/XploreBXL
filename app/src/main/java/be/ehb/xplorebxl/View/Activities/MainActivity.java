package be.ehb.xplorebxl.View.Activities;

import android.app.FragmentTransaction;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
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
        implements NavigationView.OnNavigationItemSelectedListener, GoogleMap.OnMarkerClickListener, OnMapReadyCallback, MuseumListViewFragment.MuseumListener {

    public GoogleMap map;
    private HashMap<Marker, Object> objectLinkedToMarker;
    private  Button btnCloseExtraFrag;
    private ArrayList<Target> targetComicList = new ArrayList<>();
    private ArrayList<Target> targetStreetartList = new ArrayList<>();
    private MapFragment mapFragment;


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
        mapFragment = new MapFragment();
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

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        if(sharedPreferences.getBoolean("AppHasDownloadedDataBefore", false)){
        }else{
            downloadData();
        }




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

            findViewById(R.id.detail_frag_container).setVisibility(View.GONE);
            btnCloseExtraFrag.setVisibility(View.GONE);
        } else if (id == R.id.nav_list) {
            getFragmentManager().beginTransaction().replace(R.id.frag_container, MuseumListViewFragment.newInstance()).addToBackStack("back").setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
            findViewById(R.id.detail_frag_container).setVisibility(View.GONE);
            btnCloseExtraFrag.setVisibility(View.GONE);

        } else if (id == R.id.nav_list_streetart) {
            getFragmentManager().beginTransaction().replace(R.id.frag_container, StreetArtListViewFragment.newInstance()).addToBackStack("back").setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
            findViewById(R.id.detail_frag_container).setVisibility(View.GONE);
            btnCloseExtraFrag.setVisibility(View.GONE);
        } else if (id == R.id.nav_list_comics) {
            getFragmentManager().beginTransaction().replace(R.id.frag_container, ComicListViewFragment.newInstance()).addToBackStack("back").setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
            findViewById(R.id.detail_frag_container).setVisibility(View.GONE);
            btnCloseExtraFrag.setVisibility(View.GONE);
        } else if (id == R.id.nav_about) {
            getFragmentManager().beginTransaction().replace(R.id.frag_container, AboutFragment.newInstance()).addToBackStack("back").setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
            findViewById(R.id.detail_frag_container).setVisibility(View.GONE);
            btnCloseExtraFrag.setVisibility(View.GONE);

        } else if(id == R.id.nav_update){
            this.downloadData();
            Toast.makeText(this, "Data updated", Toast.LENGTH_LONG).show();
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
        drawMarkers();
        //map.setOnInfoWindowClickListener(this);
        map.setOnMarkerClickListener(this);
        updateCamera();
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

    @Override
    public boolean onMarkerClick(Marker marker) {

        findViewById(R.id.detail_frag_container).setVisibility(View.VISIBLE);
        btnCloseExtraFrag.setVisibility(View.VISIBLE);

        Object objectClicked = objectLinkedToMarker.get(marker);

        if(objectClicked instanceof Museum){
            getFragmentManager().beginTransaction().replace(R.id.detail_frag_container, MuseumDetailFragment.newInstance((Museum) objectClicked)).commit();
        }else if(objectClicked instanceof StreetArt){
            getFragmentManager().beginTransaction().replace(R.id.detail_frag_container, StreetArtDetailFragment.newInstance((StreetArt) objectClicked)).commit();
        }else if (objectClicked instanceof Comic){
            getFragmentManager().beginTransaction().replace(R.id.detail_frag_container, ComicDetailFragment.newInstance((Comic) objectClicked)).commit();
        }

        return true;
    }

    @Override
    public void museumSelected(Museum m) {



        CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(m.getCoord(),17);
        map.animateCamera(cu);
    }
}
