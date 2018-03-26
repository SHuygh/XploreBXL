package be.ehb.xplorebxl.View.Fragments.StreetArt;


import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.content.ContextWrapper;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;

import be.ehb.xplorebxl.Model.StreetArt;
import be.ehb.xplorebxl.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class StreetArtDetailFragment extends Fragment {

    private StreetArt selectedStreetArt;
    private ImageView ivStreetart;
    private TextView tv_Artistname, tv_explenation, tvDistance;

    private Location location;


    public StreetArtDetailFragment() {
        // Required empty public constructor
    }

    public static StreetArtDetailFragment newInstance(StreetArt streetArt, LocationManager lm, Activity context) {
        StreetArtDetailFragment fragment = new StreetArtDetailFragment();
        fragment.selectedStreetArt = streetArt;

        if (lm != null && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fragment.location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if(fragment.location == null){
                fragment.location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }

        }

        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_street_art_detail, container, false);

        ivStreetart = rootView.findViewById(R.id.iv_detail_streetart);
        tv_Artistname = rootView.findViewById(R.id.tv_detail_streetart_artistname);
        tv_explenation = rootView.findViewById(R.id.tv_detail_streetart_explanation);
        tvDistance = rootView.findViewById(R.id.tv_detail_streetart_distance);


        tv_Artistname.setText(selectedStreetArt.getNameOfArtist());

        String explenation = !TextUtils.isEmpty(selectedStreetArt.getExplanation())? selectedStreetArt.getAddress() + ", " + selectedStreetArt.getExplanation() : selectedStreetArt.getAddress();
        tv_explenation.setText(explenation);

        if (location != null){
            tvDistance.setVisibility(View.VISIBLE);
            Location loc_streetart = new Location("location");
            loc_streetart.setLatitude(selectedStreetArt.getCoordX());
            loc_streetart.setLongitude(selectedStreetArt.getCoordY());
            float distance_streetart = location.distanceTo(loc_streetart);

            distance_streetart = distance_streetart/1000;
           tvDistance.setText(String.format("%.2f km", distance_streetart));

        }else {
            tvDistance.setVisibility(View.GONE);
        }

        if(selectedStreetArt.isHasIMG()) {

            String imgId = selectedStreetArt.getImgUrl()
                                .split("files/")[1]
                                    .split("[/]")[0];

            ContextWrapper cw = new ContextWrapper(getActivity());
            File directory = cw.getDir("images", MODE_PRIVATE);
            File file = new File(directory, imgId +".jpeg");
            Picasso.with(getActivity())
                    .load(file)
                    .into(ivStreetart);
        }else {
           ivStreetart.setVisibility(View.INVISIBLE);
        }


        return rootView;
    }

}
