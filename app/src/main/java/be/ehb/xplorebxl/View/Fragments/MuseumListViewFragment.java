package be.ehb.xplorebxl.View.Fragments;


import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import be.ehb.xplorebxl.Model.Museum;
import be.ehb.xplorebxl.R;
import be.ehb.xplorebxl.Utils.MuseumListAdapter;

/**
 * Created by TDS-Team on 16/03/2018.
 */

public class MuseumListViewFragment extends Fragment implements AdapterView.OnItemClickListener{

    public interface MuseumListener {
        public void museumSelected(Museum m);
    }
    private MuseumListAdapter museumListAdapter;
    private Button btn_musea;
    private ListView lv_musea;
    private MuseumListener callback;
    private LocationManager locationManager;

    public MuseumListViewFragment() {
    }


    public static MuseumListViewFragment newInstance(LocationManager lm) {
        MuseumListViewFragment fragment = new MuseumListViewFragment();
        fragment.locationManager = lm;
        return fragment;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        callback = (MuseumListener) context;
    }

    //voor oudere android versies:
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callback = (MuseumListener) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_list_view, container, false);

        btn_musea = rootView.findViewById(R.id.btn_lv_musea);
        lv_musea = rootView.findViewById(R.id.lv_museums);

        museumListAdapter = new MuseumListAdapter(getActivity(), locationManager);
        lv_musea.setAdapter(museumListAdapter);

        lv_musea.setOnItemClickListener(this);

        return rootView;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        Museum selectedMuseum = (Museum) museumListAdapter.getItem(i);
        callback.museumSelected(selectedMuseum);
        getActivity().onBackPressed();

    }

}
