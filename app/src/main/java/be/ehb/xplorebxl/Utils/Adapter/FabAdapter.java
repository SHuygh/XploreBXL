package be.ehb.xplorebxl.Utils.Adapter;

import android.content.Context;
import android.content.ContextWrapper;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import be.ehb.xplorebxl.Database.LandMarksDatabase;
import be.ehb.xplorebxl.Model.Comic;
import be.ehb.xplorebxl.Model.Museum;
import be.ehb.xplorebxl.Model.StreetArt;
import be.ehb.xplorebxl.R;
import be.ehb.xplorebxl.Utils.LocationUtil;
import be.ehb.xplorebxl.Utils.OnItemClickListener;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Millmaster on 26/03/2018.
 */

public class FabAdapter extends RecyclerView.Adapter<FabAdapter.CustomViewHolder> {
    private ArrayList<Object> items;
    private Context mContext;

    private OnItemClickListener onItemClickListener;

    public FabAdapter(Context mContext) {
        this.items = LandMarksDatabase.getInstance(mContext).getSortedList(LocationUtil.getInstance().getLocation());
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
        Object currentObject = items.get(i);

        if(currentObject instanceof Museum){
            setupView(customViewHolder, (Museum) currentObject);
        }else if(currentObject instanceof StreetArt){
            setupView(customViewHolder, (StreetArt) currentObject);
        }else if(currentObject instanceof Comic){
            setupView(customViewHolder, (Comic) currentObject);
        }else{
            customViewHolder.textView.setText("ERROR");
        }


    }

    public void setupView(CustomViewHolder customViewHolder, StreetArt streetArt) {
        //Render image using Picasso library
        if(streetArt.isHasIMG()) {

            String imgId = streetArt.getImgUrl()
                    .split("files/")[1]
                    .split("[/]")[0];

            ContextWrapper cw = new ContextWrapper(mContext);
            File directory = cw.getDir("images", MODE_PRIVATE);
            File file = new File(directory, imgId +".jpeg");

            Picasso.with(mContext)
                    .load(file)
                    .into(customViewHolder.imageView);
        }

        customViewHolder.textView.setText(streetArt.getNameOfArtist());
    }

    public void setupView(CustomViewHolder customViewHolder, Comic comic) {
        //Render image using Picasso library
        if(comic.isHasIMG()) {

            String imgId = comic.getImgUrl()
                    .split("files/")[1]
                    .split("[/]")[0];

            ContextWrapper cw = new ContextWrapper(mContext);
            File directory = cw.getDir("images", MODE_PRIVATE);
            File file = new File(directory, imgId +".jpeg");
            Picasso.with(mContext)
                    .load(file)
                    .into(customViewHolder.imageView);
        }

        customViewHolder.textView.setText(comic.getPersonnage());
    }

    public void setupView(CustomViewHolder customViewHolder, Museum museum) {
        customViewHolder.textView.setText(museum.getName());
    }

    @Override
    public int getItemCount() {
        return (null != items ? items.size() : 0);
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        protected ImageView imageView;
        protected TextView textView;

        public CustomViewHolder(View view) {
            super(view);
            this.imageView = view.findViewById(R.id.thumbnail);
            this.textView = view.findViewById(R.id.title);
        }
    }

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


}



