package be.ehb.xplorebxl.Utils.Adapter;

import android.Manifest;
import android.app.Activity;
import android.content.ContextWrapper;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

import be.ehb.xplorebxl.Database.LandMarksDatabase;
import be.ehb.xplorebxl.Model.StreetArt;
import be.ehb.xplorebxl.R;
import be.ehb.xplorebxl.Utils.LocationUtil;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Q on 19-3-2018.
 */

public class StreetArtListAdapter extends BaseAdapter {

    private class ViewHolder{
        TextView tvStreetartArtistName;
        TextView tvStreetartAddress;
        TextView tvDistance;
        ImageView ivStreetartPhoto;
    }
    private Activity context;
    private List<StreetArt> items;
    private Location location;
    private ViewHolder mViewHolder;



    public StreetArtListAdapter(Activity context) {
        this.context = context;
        location = LocationUtil.getInstance().getLocation();
        items = LandMarksDatabase.getInstance(context).getSortedStreetArt(location);
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
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (view == null){
            view = setupView(viewGroup);
        }else {
            mViewHolder = (ViewHolder) view.getTag();
        }

        StreetArt currentStreetArt = items.get(i);

        mViewHolder.tvStreetartArtistName.setText(currentStreetArt.getNameOfArtist());
        mViewHolder.tvStreetartAddress.setText(currentStreetArt.getAddress());

        setupDistance(currentStreetArt);

        setupImgView(currentStreetArt);

        return view;
    }

    private void setupImgView(StreetArt currentStreetArt) {
        if(currentStreetArt.isHasIMG()) {

            String imgId = currentStreetArt.getImgUrl()
                    .split("files/")[1]
                    .split("[/]")[0];

            ContextWrapper cw = new ContextWrapper(context);
            File directory = cw.getDir("images", MODE_PRIVATE);
            File file = new File(directory, imgId +".jpeg");
            Picasso.with(context)
                    .load(file)
                    .into(mViewHolder.ivStreetartPhoto);
        }
    }

    private void setupDistance(StreetArt currentStreetArt) {
        if (location != null){
            float distance = LocationUtil.getInstance().getDistance(currentStreetArt.getCoordX(), currentStreetArt.getCoordY(), location);
            mViewHolder.tvDistance.setText(String.format("%.2f km", distance));

        }else {
            mViewHolder.tvDistance.setVisibility(View.GONE);
        }
    }

    @NonNull
    private View setupView(ViewGroup viewGroup) {
        View view;
        view = context.getLayoutInflater().inflate(R.layout.fragment_street_art_detail,viewGroup,false);
        mViewHolder = new ViewHolder();

        mViewHolder.tvStreetartArtistName = view.findViewById(R.id.tv_detail_streetart_artistname);
        mViewHolder.tvStreetartAddress = view.findViewById(R.id.tv_detail_streetart_explanation);
        mViewHolder.ivStreetartPhoto = view.findViewById(R.id.iv_detail_streetart);
        mViewHolder.tvDistance = view.findViewById(R.id.tv_detail_streetart_distance);


        view.setTag(mViewHolder);
        return view;
    }


}

