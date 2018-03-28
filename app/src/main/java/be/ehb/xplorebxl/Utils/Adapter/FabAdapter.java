package be.ehb.xplorebxl.Utils.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import be.ehb.xplorebxl.Model.StreetArt;
import be.ehb.xplorebxl.R;
import be.ehb.xplorebxl.Utils.OnItemClickListener;

/**
 * Created by Millmaster on 26/03/2018.
 */

public class FabAdapter extends RecyclerView.Adapter<FabAdapter.CustomViewHolder> {
    private List<StreetArt> streetArtList;
    private Context mContext;

    private OnItemClickListener onItemClickListener;

    public FabAdapter(List<StreetArt> streetArtList, Context mContext) {
        this.streetArtList = streetArtList;
        this.mContext = mContext;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_list_row, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, int i) {
        StreetArt streetArt = streetArtList.get(i);

        //Render image using Picasso library
        if (!TextUtils.isEmpty(streetArt.getImgUrl())) {
            Picasso.with(mContext).load(streetArt.getImgUrl())
                    .error(R.drawable.placeholder)
                    .placeholder(R.drawable.placeholder)
                    .into(customViewHolder.imageView);
        }

        //Setting text view title
        customViewHolder.textView.setText(Html.fromHtml(streetArt.getNameOfArtist()));
    }

    @Override
    public int getItemCount() {
        return (null != streetArtList ? streetArtList.size() : 0);
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        protected ImageView imageView;
        protected TextView textView;

        public CustomViewHolder(View view) {
            super(view);
            this.imageView = (ImageView) view.findViewById(R.id.thumbnail);
            this.textView = (TextView) view.findViewById(R.id.title);
        }
    }

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}



