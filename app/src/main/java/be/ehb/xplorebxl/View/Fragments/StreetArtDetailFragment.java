package be.ehb.xplorebxl.View.Fragments;


import android.app.Fragment;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import be.ehb.xplorebxl.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class StreetArtDetailFragment extends Fragment {


    public StreetArtDetailFragment() {
        // Required empty public constructor
    }

    public static StreetArtDetailFragment newInstance() {
        return new StreetArtDetailFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_street_art_detail, container, false);
    }

}
