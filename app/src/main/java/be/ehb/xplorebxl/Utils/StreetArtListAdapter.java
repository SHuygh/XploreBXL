package be.ehb.xplorebxl.Utils;

import android.app.Activity;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import be.ehb.xplorebxl.Database.LandMarksDatabase;
import be.ehb.xplorebxl.Model.StreetArt;
import be.ehb.xplorebxl.R;

/**
 * Created by Q on 19-3-2018.
 */

public class StreetArtListAdapter extends BaseAdapter {

    private class ViewHolder{
        TextView tvStreetartArtistName;
        TextView tvStreetartAddress;
        ImageView ivStreetartPhoto;
    }
    private Activity context;
    private List<StreetArt> items;

    public StreetArtListAdapter(Activity context) {
        this.context = context;
        items = LandMarksDatabase.getInstance(context).getStreetArtDao().getAllStreetArt();

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

        ViewHolder mViewHolder;

        if (view == null){
            view = context.getLayoutInflater().inflate(R.layout.fragment_street_art_detail,viewGroup,false);
            mViewHolder = new ViewHolder();

            mViewHolder.tvStreetartArtistName = view.findViewById(R.id.tv_detail_streetart_artistname);
            mViewHolder.tvStreetartAddress = view.findViewById(R.id.tv_detail_streetart_explanation);
            mViewHolder.ivStreetartPhoto = view.findViewById(R.id.iv_detail_streetart);

        }else {
            mViewHolder = (ViewHolder) view.getTag();
        }

        StreetArt currentStreetArt = items.get(i);

        mViewHolder.tvStreetartArtistName.setText(currentStreetArt.getNameOfArtist());
        mViewHolder.tvStreetartAddress.setText(currentStreetArt.getAddress());


        if(currentStreetArt.isHasIMG()) {
            String url = currentStreetArt.getImgUrl();

            Uri uri = Uri.parse(url);
            Picasso.with(context).load(uri).into(mViewHolder.ivStreetartPhoto);
        }else {
            mViewHolder.ivStreetartPhoto.setVisibility(View.INVISIBLE);
        }


        return view;
    }


}

