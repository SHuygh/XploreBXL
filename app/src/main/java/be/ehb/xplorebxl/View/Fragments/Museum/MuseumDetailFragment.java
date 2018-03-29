package be.ehb.xplorebxl.View.Fragments.Museum;


import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import be.ehb.xplorebxl.Model.Museum;
import be.ehb.xplorebxl.R;
import be.ehb.xplorebxl.Utils.Listener.FavouriteItemListener;
import be.ehb.xplorebxl.Utils.LocationUtil;

/**
 * Created by TDS-Team on 16/03/2018.
 */

public class MuseumDetailFragment extends Fragment {

    private Button btnPhone, btnWebsite, btnEmail;
    private TextView tvName, tvAdress, tvDistance;
    private Museum selectedMuseum;

    private Location location;

    private Button btnInfo;

    private FavouriteItemListener faveCallback;




    public MuseumDetailFragment() {}

    public static MuseumDetailFragment newInstance(Museum m) {
        MuseumDetailFragment fragment = new MuseumDetailFragment();
        fragment.selectedMuseum = m;
        fragment.location = LocationUtil.getInstance().getLocation();
        return fragment;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        faveCallback = (FavouriteItemListener) context;

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        faveCallback = (FavouriteItemListener) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = setupView(inflater, container);


        tvName.setText(selectedMuseum.getName());
        tvAdress.setText(String.format("%s, %s", selectedMuseum.getAdres(), selectedMuseum.getCity()));

        setupBtnPhone(rootView);
        setupBtnEmail(rootView);
        setupBtnWebsite(rootView);




        setupDistance();

        return rootView;
    }

    @NonNull
    public View setupView(LayoutInflater inflater, ViewGroup container) {
        final View rootView = inflater.inflate(R.layout.fragment_museum_detail,container,false);

        btnInfo = rootView.findViewById(R.id.btn_info);
        btnInfo.setVisibility(View.VISIBLE);

        if (rootView.findViewById(R.id.contact_frag_container).getVisibility() == View.VISIBLE){
            rootView.findViewById(R.id.contact_frag_container).setVisibility(View.GONE);
        }

        btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rootView.findViewById(R.id.contact_frag_container).getVisibility() == View.GONE){
                    rootView.findViewById(R.id.contact_frag_container).setVisibility(View.VISIBLE);
                } else {
                    rootView.findViewById(R.id.contact_frag_container).setVisibility(View.GONE);
                }

            }
        });




        tvName = rootView.findViewById(R.id.tv_detail_museum_name);
        tvAdress = rootView.findViewById(R.id.tv_detail_museum_address);
        tvDistance = rootView.findViewById(R.id.tv_detail_museum_distance);
        return rootView;
    }

    public void setupDistance() {
        if (location != null){
            float distance = LocationUtil.getInstance().getDistance(selectedMuseum.getCoordX(), selectedMuseum.getCoordY(), location);
            tvDistance.setText(String.format("%.2f km", distance));

        }else {
            tvDistance.setVisibility(View.GONE);
        }
    }

    public void setupBtnWebsite(View rootView) {
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
    }

    public void setupBtnEmail(View rootView) {
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
    }

    public void setupBtnPhone(View rootView) {
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
    }


}
