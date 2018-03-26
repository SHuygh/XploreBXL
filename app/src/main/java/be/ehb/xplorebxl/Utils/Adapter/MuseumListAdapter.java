package be.ehb.xplorebxl.Utils.Adapter;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import be.ehb.xplorebxl.Database.LandMarksDatabase;
import be.ehb.xplorebxl.Model.Museum;
import be.ehb.xplorebxl.R;

/**
 * Created by TDS-Team on 16/03/2018.
 */

public class MuseumListAdapter extends BaseAdapter {

    private class ViewHolder {
        Button btnPhone, btnWebsite, btnEmail;
        TextView tvName, tvAdress, tvDistance;
    }

    private Activity context;
    private List<Museum> items;
    private Location location;

    public MuseumListAdapter(Activity context, LocationManager lm) {
        this.context = context;
        if (lm != null && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if(location == null){
                location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }

        }
        items = LandMarksDatabase.getInstance(context).getSortedMuseums(location);

    }


    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, final ViewGroup viewGroup) {

        ViewHolder mViewHolder;

        if (view == null) {
            view = context.getLayoutInflater().inflate(R.layout.fragment_museum_detail, viewGroup, false);
            mViewHolder = new ViewHolder();

            mViewHolder.btnEmail = view.findViewById(R.id.btn_detail_museum_mail);
            mViewHolder.btnPhone = view.findViewById(R.id.btn_detail_museum_phone);
            mViewHolder.btnWebsite = view.findViewById(R.id.btn_detail_museum_website);

            mViewHolder.tvAdress = view.findViewById(R.id.tv_detail_museum_address);
            mViewHolder.tvName = view.findViewById(R.id.tv_detail_museum_name);
            mViewHolder.tvDistance = view.findViewById(R.id.tv_detail_museum_distance);

            view.setTag(mViewHolder);

        } else {
            mViewHolder = (ViewHolder) view.getTag();
        }

        final Museum currentMuseum = items.get(i);

        mViewHolder.tvName.setText(currentMuseum.getName());
        mViewHolder.tvAdress.setText(currentMuseum.getAdres());

        if (location != null){
            Location loc_museum = new Location("location");
            loc_museum.setLatitude(currentMuseum.getCoordX());
            loc_museum.setLongitude(currentMuseum.getCoordY());
            float distance_museum = location.distanceTo(loc_museum);

            distance_museum = distance_museum/1000;
            mViewHolder.tvDistance.setText(String.format("%.2f km", distance_museum));

        }else {
            mViewHolder.tvDistance.setText("");
        }


        if(TextUtils.isEmpty(currentMuseum.getTel())){
            mViewHolder.btnPhone.setVisibility(View.GONE);
        }else{
            mViewHolder.btnPhone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String tel = currentMuseum.getTel();
                    Uri uri = Uri.parse("tel:" + tel);

                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    viewGroup.getContext().startActivity(intent);
                }
            });
        }


        if(TextUtils.isEmpty(currentMuseum.getEmail())){
            mViewHolder.btnEmail.setVisibility(View.GONE);
        }else {
            mViewHolder.btnEmail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                            "mailto", currentMuseum.getEmail(), null));
                    viewGroup.getContext().startActivity(Intent.createChooser(emailIntent, "Send email..."));
                }
            });
        }

        if(TextUtils.isEmpty(currentMuseum.getUrl())){
            mViewHolder.btnWebsite.setVisibility(View.GONE);
        }else{
            mViewHolder.btnWebsite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String url = currentMuseum.getUrl();

                    if (!url.startsWith("http://") && !url.startsWith("https://")) {
                        url = "http://" + url;
                    }

                    Uri uri = Uri.parse(url);

                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);

                    viewGroup.getContext().startActivity(intent);
                }
            });
        }




        return view;
    }
}
