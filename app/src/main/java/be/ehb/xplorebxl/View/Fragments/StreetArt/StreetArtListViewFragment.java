package be.ehb.xplorebxl.View.Fragments.StreetArt;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import be.ehb.xplorebxl.Model.StreetArt;
import be.ehb.xplorebxl.R;
import be.ehb.xplorebxl.Utils.Listener.FavouriteItemListener;
import be.ehb.xplorebxl.Utils.Listener.ListviewItemListener;
import be.ehb.xplorebxl.Utils.Adapter.StreetArtListAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class StreetArtListViewFragment extends Fragment {

    private ListView lvStreetart;

    private ListviewItemListener callback;

    public StreetArtListViewFragment() {
        // Required empty public constructor
    }

    public static StreetArtListViewFragment newInstance (){
       return new StreetArtListViewFragment();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        callback = (ListviewItemListener) context;
    }

    //voor oudere android versies
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callback = (ListviewItemListener) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_street_art_list_view, container, false);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getString(R.string.pu_streetart));

        lvStreetart = rootView.findViewById(R.id.lv_streetart);

        final StreetArtListAdapter streetArtListAdapter = new StreetArtListAdapter(getActivity());

        lvStreetart.setAdapter(streetArtListAdapter);

        lvStreetart.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                StreetArt selectedStreetart = (StreetArt) streetArtListAdapter.getItem(i);
                callback.itemSelected(selectedStreetart);


            }
        });


        return rootView ;
    }

}
