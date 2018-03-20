package be.ehb.xplorebxl.Utils;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import be.ehb.xplorebxl.Database.LandMarksDatabase;
import be.ehb.xplorebxl.Model.StreetArt;
import be.ehb.xplorebxl.R;

/**
 * Created by Q on 19-3-2018.
 */

public class StreetArtListAdapter extends BaseAdapter {

    private class ViewHolder{
        TextView tvRowStreetartName;
        TextView tvRowStreetartAddress;
        ImageView ivRowStreetartPhoto;
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
            view = context.getLayoutInflater().inflate(R.layout.row_streetart,viewGroup,false);
            mViewHolder = new ViewHolder();

            mViewHolder.tvRowStreetartName = view.findViewById(R.id.tv_row_streetart_artistname);
            mViewHolder.tvRowStreetartAddress = view.findViewById(R.id.tv_row_streetart_address);
            mViewHolder.ivRowStreetartPhoto = view.findViewById(R.id.iv_row_streetart);

        }else {
            mViewHolder = (ViewHolder) view.getTag();
        }

        StreetArt currentStreetArt = items.get(i);

        mViewHolder.tvRowStreetartName.setText(currentStreetArt.getNameOfArt());
        mViewHolder.tvRowStreetartAddress.setText(currentStreetArt.getAddress());



        return view;
    }


}

