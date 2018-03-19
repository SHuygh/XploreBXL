package be.ehb.xplorebxl.Utils;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
        TextView tvMuseumName, tvMuseumAddress;
    }

    private Activity context;
    private List<Museum> items;

    public MuseumListAdapter(Activity context) {
        this.context = context;
        items = LandMarksDatabase.getInstance(context).getMuseumDao().getAllMuseums();

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
            view = context.getLayoutInflater().inflate(R.layout.row_museum,viewGroup, false);
            mViewHolder = new ViewHolder();

            mViewHolder.tvMuseumName = view.findViewById(R.id.tv_museum_name);
            mViewHolder.tvMuseumAddress = view.findViewById(R.id.tv_museum_address);

            view.setTag(mViewHolder);

        }else {
            mViewHolder = (ViewHolder) view.getTag();
        }

        Museum currentMuseum = items.get(i);

        mViewHolder.tvMuseumName.setText(currentMuseum.getName());
        mViewHolder.tvMuseumAddress.setText(currentMuseum.getAdres());




        return view;
    }
}
