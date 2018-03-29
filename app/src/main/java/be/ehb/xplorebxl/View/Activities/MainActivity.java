package be.ehb.xplorebxl.View.Activities;

import android.Manifest;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import be.ehb.xplorebxl.Database.LandMarksDatabase;
import be.ehb.xplorebxl.Model.Comic;
import be.ehb.xplorebxl.Model.Museum;
import be.ehb.xplorebxl.Model.StreetArt;
import be.ehb.xplorebxl.R;
import be.ehb.xplorebxl.Utils.DirectionsParser;
import be.ehb.xplorebxl.Utils.Downloader;
import be.ehb.xplorebxl.Utils.Listener.ListviewItemListener;
import be.ehb.xplorebxl.Utils.Listener.StartBtnListener;
import be.ehb.xplorebxl.Utils.LocationUtil;
import be.ehb.xplorebxl.View.Fragments.AboutFragment;
import be.ehb.xplorebxl.View.Fragments.Comic.ComicDetailFragment;
import be.ehb.xplorebxl.View.Fragments.Comic.ComicListViewFragment;
import be.ehb.xplorebxl.View.Fragments.FabFragment;
import be.ehb.xplorebxl.View.Fragments.LauncherFragment;
import be.ehb.xplorebxl.View.Fragments.Museum.MuseumDetailFragment;
import be.ehb.xplorebxl.View.Fragments.Museum.MuseumListViewFragment;
import be.ehb.xplorebxl.View.Fragments.StreetArt.StreetArtDetailFragment;
import be.ehb.xplorebxl.View.Fragments.StreetArt.StreetArtListViewFragment;

/**
 * Created by TDS-Team on 16/03/2018.
 */

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GoogleMap.OnMarkerClickListener, OnMapReadyCallback, ListviewItemListener, StartBtnListener {

    private Downloader downloader;
    private String TAG = "testtesttest";

    public GoogleMap map;
    private HashMap<Marker, Object> objectLinkedToMarker;
    private ImageButton btnCloseExtraFrag;
    private MapFragment mapFragment;
    private Marker selectedMarker;
    private Menu menu;
    private FloatingActionButton floatingActionButton;
    private FloatingActionButton fabDirections;
    private FrameLayout fab_container;
    private int filterId;
    private Toolbar toolbar;
    private Polyline route;
    private BitmapDescriptor
            IC_MUSEUM,
            IC_STREETART,
            IC_COMIC,
            IC_SELECTED,
            IC_FAV;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        getFragmentManager().beginTransaction().replace(R.id.frag_container, LauncherFragment.newInstance()).commit();

        downloader = Downloader.getInstance();

        filterId = R.id.pu_all;

        setupDrawer();
        toolbar.setVisibility(View.GONE);
        setupMapFragment();
        setupCloseDetailFrag();
        checkHasDownloadedBefore();
        LocationUtil.getInstance().setupLocationServices(this);

        setupFAB();



    }

    private void checkHasDownloadedBefore() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        if (!sharedPreferences.getBoolean("AppHasDownloadedDataBefore", false)) {
            downloader.downloadData(this);
        }
    }

    private void setupCloseDetailFrag() {

        btnCloseExtraFrag = findViewById(R.id.btn_main_close);

        btnCloseExtraFrag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeDetailFrag();
                fabDirections.setVisibility(View.GONE);
            }
        });
    }

    private void setupMapFragment() {
        mapFragment = new MapFragment();
        mapFragment.getMapAsync(this);

        //getFragmentManager().beginTransaction().replace(R.id.frag_container, mapFragment).commit();

        objectLinkedToMarker = new HashMap<>();
    }

    private void setupDrawer() {
        toolbar = findViewById(R.id.toolbar);
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
        LocationUtil.getInstance().removeUpdates();
    }


    /*NAVIGATION****NAVIGATION****NAVIGATION****NAVIGATION****NAVIGATION****NAVIGATION****NAVIGATION****NAVIGATION****NAVIGATION****NAVIGATION****NAVIGATION****NAVIGATION*****/

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_map) {
            getFragmentManager().beginTransaction().replace(R.id.frag_container, mapFragment).commit();
            menu.setGroupVisible(R.id.mg_filter, true);
            setupMap();
            closeDetailFrag();

        } else if (id == R.id.nav_list) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.frag_container, MuseumListViewFragment.newInstance())
                    .addToBackStack("back")
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
            menu.setGroupVisible(R.id.mg_filter, false);

            closeFABFrag();
            closeDetailFrag();
        } else if (id == R.id.nav_list_streetart) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.frag_container, StreetArtListViewFragment.newInstance())
                    .addToBackStack("back")
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
            menu.setGroupVisible(R.id.mg_filter, false);

            closeFABFrag();
            closeDetailFrag();
        } else if (id == R.id.nav_list_comics) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.frag_container, ComicListViewFragment.newInstance())
                    .addToBackStack("back")
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
            menu.setGroupVisible(R.id.mg_filter, false);

            closeFABFrag();
            closeDetailFrag();
        } else if (id == R.id.nav_about) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.frag_container, AboutFragment.newInstance())
                    .addToBackStack("back")
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
            menu.setGroupVisible(R.id.mg_filter, false);

            closeFABFrag();
            closeDetailFrag();
        } else if (id == R.id.nav_update) {
            downloader.downloadData(this);
            Toast.makeText(this, R.string.txt_toast_update_data, Toast.LENGTH_LONG).show();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void closeFABFrag() {
        floatingActionButton.setVisibility(View.GONE);
        fab_container.setVisibility(View.GONE);
    }

    public void closeDetailFrag() {
        fabDirections.setVisibility(View.GONE);
        findViewById(R.id.detail_frag_container).setVisibility(View.GONE);
        btnCloseExtraFrag.setVisibility(View.GONE);
        cancelSelectedMarker();
    }


    /*MAP METHODS****MAP METHODS****MAP METHODS****MAP METHODS****MAP METHODS****MAP METHODS****MAP METHODS****MAP METHODS****MAP METHODS****MAP METHODS****MAP METHODS****MAP METHODS*****/
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        IC_MUSEUM = BitmapDescriptorFactory.defaultMarker(180);
        IC_STREETART = BitmapDescriptorFactory.defaultMarker(90);
        IC_COMIC = BitmapDescriptorFactory.defaultMarker(10);
        IC_SELECTED = BitmapDescriptorFactory.defaultMarker(50);
        IC_FAV = BitmapDescriptorFactory.defaultMarker(270);

        drawMarkers();

        setupMap();

    }

    private void setupMap() {
        loadInMapStyle(map);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
            floatingActionButton.setVisibility(View.GONE);
        }else{
            floatingActionButton.setVisibility(View.VISIBLE);
        }



        map.setOnMarkerClickListener(this);
        updateCamera();
        if (!(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            map.setMyLocationEnabled(true);
            map.getUiSettings().setMyLocationButtonEnabled(true);
        }

    }

    public void loadInMapStyle(GoogleMap googleMap) {

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
    }

    public void drawMarkers() {
        if(objectLinkedToMarker.size()>0){
            clearMapBeforeDraw();
        }

        LandMarksDatabase landMarksDatabase = LandMarksDatabase.getInstance(this);
        List<Museum> museums = landMarksDatabase.getMuseums();

        for(Museum element: museums){

            Marker marker = map.addMarker(
                    new MarkerOptions()
                            .title(element.getName())
                            .position(element.getCoord())
                            .icon(IC_MUSEUM));

            objectLinkedToMarker.put(marker,element);
            if(landMarksDatabase.checkFav(element)){
                marker.setIcon(IC_FAV);
            }
            }

        List<StreetArt> streetArtList = landMarksDatabase.getAllStreetArt();

        for(StreetArt element: streetArtList){

            Marker marker = map.addMarker(
                    new MarkerOptions()
                            .title(element.getNameOfArtist())
                            .position(element.getCoord())
                            .icon(IC_STREETART));
            objectLinkedToMarker.put(marker, element);

            if(landMarksDatabase.checkFav(element)){
                marker.setIcon(IC_FAV);
            }
        }

        List<Comic> comicsList = landMarksDatabase.getAllComics();

        for (Comic element: comicsList){

            Marker marker = map.addMarker(
                    new MarkerOptions()
                            .title(element.getNameOfIllustrator())
                            .position(element.getCoord())
                            .icon(IC_COMIC));
            objectLinkedToMarker.put(marker, element);
            if(landMarksDatabase.checkFav(element)){
                marker.setIcon(IC_FAV);
            }
        }

        }

    private void clearMapBeforeDraw() {
        map.clear();
        objectLinkedToMarker.clear();
    }

    private void updateCamera() {

        List<Museum> museums = LandMarksDatabase.getInstance(this).getMuseums();

        if(museums.size() > 0){
            LatLng coord = museums.get(0).getCoord();
                if(coord != null){
                    CameraUpdate cameraUpdate;
                    if(map.getCameraPosition().zoom < 6) {
                        cameraUpdate = CameraUpdateFactory.newLatLngZoom(coord, 6);
                        map.moveCamera(cameraUpdate);
                    }
                    cameraUpdate = CameraUpdateFactory.newLatLngZoom(coord, 13);
                    map.animateCamera(cameraUpdate);
                }
        }
    }

    /*MARKER SELECT***MARKER SELECT***MARKER SELECT***MARKER SELECT***MARKER SELECT***MARKER SELECT***MARKER SELECT***MARKER SELECT***MARKER SELECT***MARKER SELECT***MARKER SELECT*/

    @Override
    public boolean onMarkerClick(Marker marker) {


        fab_container.setVisibility(View.GONE);
        switchFav(marker);
        updateSelectedMarker(marker);

        return true;
    }

    private void switchFav(Marker marker) {
        Object o = objectLinkedToMarker.get(marker);

        boolean fav = LandMarksDatabase.getInstance(this).switchFav(o);

        if(fav) {
            marker.setIcon(IC_FAV);
        }else{
            if(o instanceof Museum){marker.setIcon(IC_MUSEUM);
            }else if(o instanceof StreetArt){marker.setIcon(IC_STREETART);
            }else{marker.setIcon(IC_COMIC);}
        }
    }

    @Override
    public void itemSelected(Object o) {

        menu.setGroupVisible(R.id.mg_filter, true);


        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
            floatingActionButton.setVisibility(View.GONE);
        }else{
            floatingActionButton.setVisibility(View.VISIBLE);
            fabDirections.setVisibility(View.VISIBLE);
        }

        Marker marker = null;
        getFragmentManager().beginTransaction().replace(R.id.frag_container, mapFragment).commit();

        for (Marker element : objectLinkedToMarker.keySet()) {
            if (o.equals(objectLinkedToMarker.get(element))) {
                marker = element;
                break;
            }
        }

        if(marker != null) {
            fab_container.setVisibility(View.GONE);
            updateSelectedMarker(marker);
        }
    }

    public void updateSelectedMarker(Marker marker) {

        if(route != null){
            route.remove();
        }

        cancelSelectedMarker();

        fabDirections.setVisibility(View.VISIBLE);

        selectedMarker = marker;

        marker.setIcon(IC_SELECTED);
        CameraUpdate cu = CameraUpdateFactory.newLatLngZoom((marker.getPosition()),16);
        map.animateCamera(cu);

        openDetailFragment(marker);
    }

    public void cancelSelectedMarker() {

        if(selectedMarker != null){

            Object o = objectLinkedToMarker.get(selectedMarker);

            if(LandMarksDatabase.getInstance(this).checkFav(o)) {
                selectedMarker.setIcon(IC_FAV);
            }else {
                if (o instanceof Museum) {
                    selectedMarker.setIcon(IC_MUSEUM);
                } else if (o instanceof StreetArt) {
                    selectedMarker.setIcon(IC_STREETART);
                } else if (o instanceof Comic) {
                    selectedMarker.setIcon(IC_COMIC);
                }
            }

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

        this.menu = menu;
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

                    filterId = item.getItemId();

                    switch (filterId){
                        case R.id.pu_all:

                            getSupportActionBar().setTitle(getString(R.string.app_name));


                            for(Marker element: objectLinkedToMarker.keySet()){
                                    element.setVisible(true);
                            }
                            break;
                        case R.id.pu_musea:
                            getSupportActionBar().setTitle(getString(R.string.pu_musea));

                            filterMarker(Museum.class);
                            break;
                        case R.id.pu_comic:
                            getSupportActionBar().setTitle(getString(R.string.pu_comic));

                            filterMarker(Comic.class);
                            break;
                        case R.id.pu_streetart:
                            getSupportActionBar().setTitle(getString(R.string.pu_streetart));

                            filterMarker(StreetArt.class);
                            break;
                        case R.id.pu_fav:
                            getSupportActionBar().setTitle(getString(R.string.pu_favourites));
                            for(Marker element: objectLinkedToMarker.keySet()){
                                element.setVisible(false);
                            }
                            for(Object o: LandMarksDatabase.getInstance(getApplicationContext()).getAllFavObjects()){
                                for(Marker marker: objectLinkedToMarker.keySet()){
                                    if(objectLinkedToMarker.get(marker).equals(o)){
                                        marker.setVisible(true);
                                    }
                                }
                            }
                            break;
                    }

                    if(fab_container.getVisibility() == View.VISIBLE) {

                        getFragmentManager().beginTransaction()
                                .replace(R.id.fab_frag_container,FabFragment.newInstance(filterId))
                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                .commit();

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


    public void setupFAB(){

       floatingActionButton = findViewById(R.id.fab);
       fab_container = findViewById(R.id.fab_frag_container);

       floatingActionButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               if(fab_container.getVisibility() == View.GONE) {
                    closeDetailFrag();
                    fab_container.setVisibility(View.VISIBLE);
                    fabDirections.setVisibility(View.GONE);
                   getFragmentManager().beginTransaction()
                           .replace(R.id.fab_frag_container,FabFragment.newInstance(filterId))
                           .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                           .commit();
               }else{
                   fab_container.setVisibility(View.GONE);
               }

           }
       });

       fabDirections = findViewById(R.id.fab_route);

       fabDirections.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               Object object = objectLinkedToMarker.get(selectedMarker);
                fabDirections.setVisibility(View.GONE);
               if(object instanceof Museum){
                   getDirections(((Museum) object).getCoord());
               }else if(object instanceof StreetArt){
                   getDirections(((StreetArt) object).getCoord());

               }else if(object instanceof Comic){
                   getDirections(((Comic) object).getCoord());
               }

           }
       });

    }

    //Tutorial (class DirectionParser also from) https://www.youtube.com/watch?v=jg1urt3FGCY
    public void getDirections(LatLng destination){
        Location location_origin = LocationUtil.getInstance().getLocation();
        if(location_origin != null) {
            LatLng origin = new LatLng(location_origin.getLatitude(), location_origin.getLongitude());

            //String url = getRequestURL(origin, destination);
            String url = getRequestURLFav(origin, destination);
            TaskRequestDirections taskRequestDirections = new TaskRequestDirections();
            taskRequestDirections.execute(url);
        }
    }

    private String getRequestURL(LatLng origin, LatLng destination) {
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        String str_dest = "destination=" + destination.latitude + "," + destination.longitude;

        String sensor = "sensor=false";
        String mode = "mode=walking";

        String param = str_origin + "&" + str_dest + "&" + sensor + "&" + mode;

        String output = "json";

        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + param;

        return url;
    }

    private String getRequestURLFav(LatLng origin, LatLng destination) {
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        String str_dest = "destination=" + destination.latitude + "," + destination.longitude;

        String sensor = "sensor=false";
        String mode = "mode=walking";

        String waypoints = "waypoints=optimize:true";

        ArrayList<Object> favList = LandMarksDatabase.getInstance(this).getAllFavObjects();

        for(Object o: favList){
            if(!waypoints.endsWith("=")){
                waypoints += "|";
            }
            if(o instanceof Museum){
                Museum m = (Museum) o;
                String waypoint = m.getCoordX() + "," + m.getCoordY();
                waypoints += waypoint;
            }else if(o instanceof StreetArt){
                StreetArt s = (StreetArt) o;
                String waypoint = s.getCoordX() + "," + s.getCoordY();
                waypoints += waypoint;
            }else if(o instanceof Comic){
                Comic c = (Comic) o;
                String waypoint = c.getCoordX() + "," + c.getCoordY();
                waypoints += waypoint;
            }
        }



        String param = str_origin + "&" + str_dest + "&" + sensor + "&" + mode + "&" + waypoints;

        String output = "json";

        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + param;

        return url;
    }

    private String requestDirections(String reqUrl){
        String responseStr = "";
        InputStream inputStream = null;
        HttpURLConnection httpURLConnection = null;

        try {
            URL url = new URL(reqUrl);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.connect();

            inputStream =httpURLConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            StringBuffer stringBuffer = new StringBuffer();
            String line = "";

            while ((line = bufferedReader.readLine()) != null){
                stringBuffer.append(line);
            }

            responseStr = stringBuffer.toString();
            bufferedReader.close();
            inputStreamReader.close();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                httpURLConnection.disconnect();
            }

        }

        return  responseStr;

    }

    public class TaskRequestDirections extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String responseStr = "";

            responseStr = requestDirections(strings[0]);

            return responseStr;
        }

        @Override
        protected void onPostExecute(String s) {
            TaskParser taskParser = new TaskParser();
            taskParser.execute(s);
        }
    }

    public class TaskParser extends AsyncTask<String, Void, List<List<HashMap<String, String>>>>{

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... strings) {
            JSONObject jsonObject = null;
            List<List<HashMap<String, String>>> routes = null;
            Log.d(TAG, "doInBackground: " + strings[0]);
            try {
                jsonObject = new JSONObject(strings[0]);
                DirectionsParser directionsParser = new DirectionsParser();
                routes = directionsParser.parse(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
                fabDirections.setVisibility(View.VISIBLE);
            }

            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> lists) {
            if(lists != null) {
                ArrayList points = null;

                PolylineOptions polylineOptions = null;

                for (List<HashMap<String, String>> path : lists) {
                    points = new ArrayList();
                    polylineOptions = new PolylineOptions();

                    for (HashMap<String, String> point : path) {
                        double lat = Double.parseDouble(point.get("lat"));
                        double lon = Double.parseDouble(point.get("lon"));

                        points.add(new LatLng(lat, lon));
                    }

                    polylineOptions.addAll(points);
                    polylineOptions.width(10);
                    polylineOptions.color(Color.WHITE);
                    polylineOptions.geodesic(true);
                }

                if (polylineOptions != null) {
                    route = map.addPolyline(polylineOptions);
                }
            }else{
                Toast.makeText(getApplicationContext(), "Problem connecting to Google Maps, please try again", Toast.LENGTH_LONG).show();
                fabDirections.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onStartClick() {
        getFragmentManager().beginTransaction().replace(R.id.frag_container, mapFragment).commit();
        toolbar.setVisibility(View.VISIBLE);

        floatingActionButton.setVisibility(View.VISIBLE);
    }
}
