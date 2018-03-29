package be.ehb.xplorebxl.View.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import be.ehb.xplorebxl.R;
import be.ehb.xplorebxl.Utils.StartBtnListener;


public class LauncherFragment extends Fragment {

    private Button startbtn;
    private StartBtnListener startBtnListener;


    public LauncherFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        startBtnListener = (StartBtnListener) context;
    }


    //voor oudere android versies
    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        startBtnListener = (StartBtnListener) context;
    }

    public static LauncherFragment newInstance() {

        LauncherFragment fragment = new LauncherFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_launcher, container, false);


        startbtn = rootView.findViewById(R.id.btn_start);

        startbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startBtnListener.onStartClick();
            }
        });


        return rootView;
    }
}
