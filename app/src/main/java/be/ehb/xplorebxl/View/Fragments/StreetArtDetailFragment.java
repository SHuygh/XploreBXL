package be.ehb.xplorebxl.View.Fragments;


import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import be.ehb.xplorebxl.Model.StreetArt;
import be.ehb.xplorebxl.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class StreetArtDetailFragment extends Fragment {

    private StreetArt selectedStreetArt;
    private ImageView ivStreetart;
    private TextView tv_Artistname, tv_explenation;

    public StreetArtDetailFragment() {
        // Required empty public constructor
    }

    public static StreetArtDetailFragment newInstance(StreetArt streetArt) {
        StreetArtDetailFragment fragment = new StreetArtDetailFragment();
        fragment.selectedStreetArt = streetArt;
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_street_art_detail, container, false);

        ivStreetart = rootView.findViewById(R.id.iv_detail_streetart);
        tv_Artistname = rootView.findViewById(R.id.tv_detail_streetart_artistname);
        tv_explenation = rootView.findViewById(R.id.tv_detail_streetart_explanation);

        tv_Artistname.setText(selectedStreetArt.getNameOfArtist());

        String explenation = !TextUtils.isEmpty(selectedStreetArt.getExplanation())? selectedStreetArt.getAddress() + ", " + selectedStreetArt.getExplanation() : selectedStreetArt.getAddress();
        tv_explenation.setText(explenation);

        if(selectedStreetArt.isHasIMG()) {
            Log.d("testtest", "onCreateView: image should be loaded");
            Log.d("testtest", "onCreateView: " + selectedStreetArt.getImgUrl());
            String url = selectedStreetArt.getImgUrl();

            Uri uri = Uri.parse(url);
            Picasso.with(getActivity()).load(uri).into(ivStreetart);
        }else {
           ivStreetart.setVisibility(View.INVISIBLE);
        }



        return rootView;
    }

}
