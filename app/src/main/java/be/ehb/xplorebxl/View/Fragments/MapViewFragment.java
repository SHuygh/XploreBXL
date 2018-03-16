package be.ehb.xplorebxl.View.Fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import be.ehb.xplorebxl.R;

public class MapViewFragment extends Fragment {

    /**
     * Created by TDS-Team on 16/03/2018.
     */

    public MapViewFragment() {
        // Required empty public constructor
    }

    public static MapViewFragment newInstance() {
        MapViewFragment fragment = new MapViewFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map_view, container, false);
    }

}
