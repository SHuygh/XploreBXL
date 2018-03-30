package be.ehb.xplorebxl.View.Fragments;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import be.ehb.xplorebxl.R;

/**
 * Created by TDS-Team on 16/03/2018.
 */

public class AboutFragment extends android.app.Fragment {

    private ImageView ivAbout;

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

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getString(R.string.about));


        View rootView = inflater.inflate(R.layout.fragment_about, container, false);

        /*ImageView ivLogo = rootView.findViewById(R.id.iv_logo);
        Uri uri;

        uri = Uri.parse("https://opendata.brussel.be/explore/dataset/streetart/files/e25418821200a0f7c8f9f81b22d21691/300/");
        Picasso.with(getActivity()).load(uri).into(ivLogo);*/

        return rootView;
    }

}
