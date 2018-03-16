package be.ehb.xplorebxl.Utils;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * Created by TDS-Team on 16/03/2018.
 */

public class MuseumListAdapter extends BaseAdapter {

    private MuseumListAdapter() {
    }

    private MuseumListAdapter newInstance(){
        MuseumListAdapter mAdapter = new MuseumListAdapter();

        return mAdapter;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return null;
    }
}
