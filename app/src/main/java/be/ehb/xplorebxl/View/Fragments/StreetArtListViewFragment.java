package be.ehb.xplorebxl.View.Fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import be.ehb.xplorebxl.R;
import be.ehb.xplorebxl.Utils.StreetArtListAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class StreetArtListViewFragment extends Fragment {

    private ListView lvStreetart;


    public StreetArtListViewFragment() {
        // Required empty public constructor
    }

    public static StreetArtListViewFragment newInstance (){
        StreetArtListViewFragment fragment = new StreetArtListViewFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_street_art_list_view, container, false);

        lvStreetart = rootView.findViewById(R.id.lv_streetart);

        StreetArtListAdapter streetArtListAdapter = new StreetArtListAdapter(getActivity());

        lvStreetart.setAdapter(streetArtListAdapter);


        return rootView ;
    }

}
