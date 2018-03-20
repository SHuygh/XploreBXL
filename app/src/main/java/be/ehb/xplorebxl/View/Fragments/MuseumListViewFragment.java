package be.ehb.xplorebxl.View.Fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import be.ehb.xplorebxl.R;
import be.ehb.xplorebxl.Utils.MuseumListAdapter;

/**
 * Created by TDS-Team on 16/03/2018.
 */

public class MuseumListViewFragment extends Fragment {

    private Button btn_musea;
    private ListView lv_musea;


    public MuseumListViewFragment() {
    }


    public static MuseumListViewFragment newInstance() {
        MuseumListViewFragment fragment = new MuseumListViewFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_list_view, container, false);

        btn_musea = rootView.findViewById(R.id.btn_lv_musea);
        lv_musea = rootView.findViewById(R.id.lv_museums);

        MuseumListAdapter museumListAdapter = new MuseumListAdapter(getActivity());
        lv_musea.setAdapter(museumListAdapter);


        return rootView;
    }

}
