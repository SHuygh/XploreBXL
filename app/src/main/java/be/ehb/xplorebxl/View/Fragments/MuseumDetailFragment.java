package be.ehb.xplorebxl.View.Fragments;


import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import be.ehb.xplorebxl.Database.LandMarksDatabase;
import be.ehb.xplorebxl.R;

/**
 * Created by TDS-Team on 16/03/2018.
 */

public class MuseumDetailFragment extends Fragment {

    private Button btnPhone, btnWebsite, btnEmail;

    public MuseumDetailFragment() {
        // Required empty public constructor
    }

    public static MuseumDetailFragment newInstance() {
        MuseumDetailFragment fragment = new MuseumDetailFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_museum_detail,container,false);
        btnPhone = rootView.findViewById(R.id.btn_detail_museum_phone);
        btnEmail = rootView.findViewById(R.id.btn_detail_museum_mail);
        btnWebsite = rootView.findViewById(R.id.btn_detail_museum_website);

        btnPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



            }
        });







        return rootView;
    }


}
