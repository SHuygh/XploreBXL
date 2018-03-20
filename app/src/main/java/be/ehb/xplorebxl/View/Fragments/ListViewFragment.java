package be.ehb.xplorebxl.View.Fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import be.ehb.xplorebxl.R;

/**
 * Created by TDS-Team on 16/03/2018.
 */

public class ListViewFragment extends Fragment {

    private Button btn_musea, btn_streetart;
    private ListView lv_musea, lv_streetart;
    private boolean showMusea, showStreetart;

    public ListViewFragment() {
    }


    public static ListViewFragment newInstance() {
        ListViewFragment fragment = new ListViewFragment();
        fragment.showMusea = true;
        fragment.showStreetart = true;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_list_view, container, false);

        btn_musea = rootView.findViewById(R.id.btn_lv_musea);
        btn_streetart = rootView.findViewById(R.id.btn_lv_streetart);
        lv_musea = rootView.findViewById(R.id.lv_museums);
        lv_streetart = rootView.findViewById(R.id.lv_streetart);

        btn_musea.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View view) {
                                             showMusea = !showMusea;

                                             int visibility = showMusea? View.VISIBLE: View.GONE;
                                             lv_musea.setVisibility(visibility);
                                         }
                                     }
        );


        btn_streetart.setOnClickListener(new View.OnClickListener() {
                                             @Override
                                             public void onClick(View view) {
                                                 showStreetart = !showStreetart;

                                                 int visibility = showStreetart? View.VISIBLE: View.GONE;
                                                 lv_streetart.setVisibility(visibility);
                                             }
                                         }
        );

        return rootView;
    }

}
