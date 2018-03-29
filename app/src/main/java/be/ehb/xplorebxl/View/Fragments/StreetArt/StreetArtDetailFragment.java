package be.ehb.xplorebxl.View.Fragments.StreetArt;


import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.ContextWrapper;
import android.location.Location;
import android.os.Bundle;

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
import be.ehb.xplorebxl.Utils.Listener.FavouriteItemListener;
import be.ehb.xplorebxl.Utils.LocationUtil;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class StreetArtDetailFragment extends Fragment {

    private StreetArt selectedStreetArt;
    private ImageView ivStreetart;
    private TextView tv_Artistname, tv_explenation, tvDistance;

    private Location location;

    private FavouriteItemListener faveCallback;


    public StreetArtDetailFragment() {
        // Required empty public constructor
    }

    public static StreetArtDetailFragment newInstance(StreetArt streetArt) {
        StreetArtDetailFragment fragment = new StreetArtDetailFragment();
        fragment.selectedStreetArt = streetArt;
        fragment.location = LocationUtil.getInstance().getLocation();

        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        faveCallback = (FavouriteItemListener) context;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        faveCallback = (FavouriteItemListener) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_street_art_detail, container, false);

        ivStreetart = rootView.findViewById(R.id.iv_detail_streetart);
        tv_Artistname = rootView.findViewById(R.id.tv_detail_museum_name);
        tv_explenation = rootView.findViewById(R.id.tv_detail_streetart_explanation);
        tvDistance = rootView.findViewById(R.id.tv_detail_streetart_distance);


        tv_Artistname.setText(selectedStreetArt.getNameOfArtist());

        String explenation = !TextUtils.isEmpty(selectedStreetArt.getExplanation())? selectedStreetArt.getAddress() + ", " + selectedStreetArt.getExplanation() : selectedStreetArt.getAddress();
        tv_explenation.setText(explenation);

        if (location != null){
            float distance = LocationUtil.getInstance().getDistance(selectedStreetArt.getCoordX(), selectedStreetArt.getCoordY(), location);
            tvDistance.setText(String.format("%.2f km", distance));

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
