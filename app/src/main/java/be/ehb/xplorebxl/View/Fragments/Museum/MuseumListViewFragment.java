package be.ehb.xplorebxl.View.Fragments.Museum;


import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import be.ehb.xplorebxl.Model.Museum;
import be.ehb.xplorebxl.R;
import be.ehb.xplorebxl.Utils.Listener.FavouriteItemListener;
import be.ehb.xplorebxl.Utils.Listener.ListviewItemListener;
import be.ehb.xplorebxl.Utils.Adapter.MuseumListAdapter;
import be.ehb.xplorebxl.Utils.Listener.ListviewItemListener;

/**
 * Created by TDS-Team on 16/03/2018.
 */

public class MuseumListViewFragment extends Fragment implements AdapterView.OnItemClickListener{


    private MuseumListAdapter museumListAdapter;
    private ListView lv_musea;


    private ListviewItemListener callback;


    public MuseumListViewFragment() {
    }


    public static MuseumListViewFragment newInstance() {
        return new MuseumListViewFragment();
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        callback = (ListviewItemListener) context;

    }

    //voor oudere android versies:
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callback = (ListviewItemListener) activity;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getString(R.string.pu_musea));


        View rootView = inflater.inflate(R.layout.fragment_list_view, container, false);

        lv_musea = rootView.findViewById(R.id.lv_museums);

        museumListAdapter = new MuseumListAdapter(getActivity());
        lv_musea.setAdapter(museumListAdapter);

        lv_musea.setOnItemClickListener(this);

        return rootView;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        Museum selectedMuseum = (Museum) museumListAdapter.getItem(i);
        callback.itemSelected(selectedMuseum);

    }



}
