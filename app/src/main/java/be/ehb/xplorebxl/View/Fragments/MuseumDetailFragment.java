package be.ehb.xplorebxl.View.Fragments;


import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import be.ehb.xplorebxl.Database.LandMarksDatabase;
import be.ehb.xplorebxl.Model.Museum;
import be.ehb.xplorebxl.R;
import be.ehb.xplorebxl.View.Activities.MainActivity;

/**
 * Created by TDS-Team on 16/03/2018.
 */

public class MuseumDetailFragment extends Fragment {

    private Button btnPhone, btnWebsite, btnEmail;
    private TextView tvName, tvAdress;
    private Museum selectedMuseum;

    public MuseumDetailFragment() {}

    public static MuseumDetailFragment newInstance(Museum m) {
        MuseumDetailFragment fragment = new MuseumDetailFragment();
        fragment.selectedMuseum = m;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_museum_detail,container,false);


        btnPhone = rootView.findViewById(R.id.btn_detail_museum_phone);

        if(TextUtils.isEmpty(selectedMuseum.getTel())){
            btnPhone.setVisibility(View.GONE);
        }else{
            btnPhone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String tel = selectedMuseum.getTel();
                    Uri uri = Uri.parse("tel:" + tel);

                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            });
        }


        btnEmail = rootView.findViewById(R.id.btn_detail_museum_mail);
        if(TextUtils.isEmpty(selectedMuseum.getEmail())){
            btnEmail.setVisibility(View.GONE);
        }else {
            btnEmail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                            "mailto", selectedMuseum.getEmail(), null));
                    startActivity(Intent.createChooser(emailIntent, "Send email..."));
                }
            });
        }

        btnWebsite = rootView.findViewById(R.id.btn_detail_museum_website);
        if(TextUtils.isEmpty(selectedMuseum.getUrl())){
            btnWebsite.setVisibility(View.GONE);
        }else{
            btnWebsite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String url = selectedMuseum.getUrl();

                    if (!url.startsWith("http://") && !url.startsWith("https://")) {
                        url = "http://" + url;
                    }

                    Uri uri = Uri.parse(url);

                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            });
        }




        tvName = rootView.findViewById(R.id.tv_detail_museum_name);
        tvAdress = rootView.findViewById(R.id.tv_detail_museum_address);

        tvName.setText(selectedMuseum.getName());
        tvAdress.setText(selectedMuseum.getAdres() + ", " + selectedMuseum.getCity());




        return rootView;
    }


}
