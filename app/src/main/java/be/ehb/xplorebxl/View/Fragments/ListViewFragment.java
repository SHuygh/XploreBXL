package be.ehb.xplorebxl.View.Fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import be.ehb.xplorebxl.R;

/**
 * Created by TDS-Team on 16/03/2018.
 */

public class ListViewFragment extends Fragment {

    public ListViewFragment() {
        // Required empty public constructor
    }


    public static ListViewFragment newInstance(String param1, String param2) {
        ListViewFragment fragment = new ListViewFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_view, container, false);
    }

}
