package be.ehb.xplorebxl.View.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import be.ehb.xplorebxl.Database.LandMarksDatabase;
import be.ehb.xplorebxl.R;
import be.ehb.xplorebxl.Utils.Adapter.FabAdapter;
import be.ehb.xplorebxl.Utils.ListviewItemListener;


/**
 * Created by Millmaster on 26/03/2018.
 */

public class FabFragment extends Fragment {


    private RecyclerView rvStreetart;
    private ListviewItemListener callback;
    private LocationManager locationManager;

    public FabFragment() {
    }

    public static FabFragment newInstance (LocationManager lm){
        FabFragment fragment = new FabFragment();
        fragment.locationManager = lm;

        return fragment;
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

        View rootView = inflater.inflate(R.layout.fragment_fab, container, false);

        rvStreetart = rootView.findViewById(R.id.fab_recyclerview);

        //final FabAdapter fabAdapter = new FabAdapter(getActivity(), rvStreetart, locationManager);

        FabAdapter fabAdapter = new FabAdapter(LandMarksDatabase.getInstance(getActivity()).getStreetArtDao().getAllStreetArt(), getActivity());
        rvStreetart.setAdapter(fabAdapter);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvStreetart.setLayoutManager(llm);

        return rootView ;
    }

}

/*         mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        mRecyclerView.setLayoutManager(layoutManager);

        progressBar = (ProgressBar) findViewById(R.id.progress_bar); */
