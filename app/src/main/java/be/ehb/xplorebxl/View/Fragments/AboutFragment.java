package be.ehb.xplorebxl.View.Fragments;


import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import be.ehb.xplorebxl.R;

/**
 * Created by TDS-Team on 16/03/2018.
 */

public class AboutFragment extends android.app.Fragment {

    public AboutFragment() {
        // Required empty public constructor
    }


    public static AboutFragment newInstance() {
        AboutFragment fragment = new AboutFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_about, container, false);

        ImageView iv_test = rootView.findViewById(R.id.iv_test);

        Uri uri;

        uri = Uri.parse("https://opendata.brussel.be/explore/dataset/streetart/files/e25418821200a0f7c8f9f81b22d21691/300/");
        Picasso.with(getActivity()).load(uri).into(iv_test);

        return rootView;
    }

}
