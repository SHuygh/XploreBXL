package be.ehb.xplorebxl.View.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import be.ehb.xplorebxl.R;
import be.ehb.xplorebxl.Utils.Adapter.FabAdapter;
import be.ehb.xplorebxl.Utils.Listener.ListviewItemListener;


/**
 * Created by Millmaster on 26/03/2018.
 */

public class FabFragment extends Fragment {


    private RecyclerView recyclerView;
    private ListviewItemListener callback;
    private int filterId;


    public FabFragment() {
    }

    public static FabFragment newInstance (int filterId){
        FabFragment fragment = new FabFragment();
        fragment.filterId = filterId;
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

        recyclerView = rootView.findViewById(R.id.fab_recyclerview);

        FabAdapter fabAdapter = new FabAdapter(getActivity(), filterId);
        recyclerView.setAdapter(fabAdapter);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(llm);

        return rootView ;
    }

}

