package be.ehb.xplorebxl.View.Fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_about, container, false);
    }

}
