package be.ehb.xplorebxl.Utils.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContextWrapper;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

import be.ehb.xplorebxl.Database.LandMarksDatabase;
import be.ehb.xplorebxl.Model.Comic;
import be.ehb.xplorebxl.R;
import be.ehb.xplorebxl.Utils.Listener.FavouriteItemListener;
import be.ehb.xplorebxl.Utils.LocationUtil;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Q on 21-3-2018.
 */

public class ComicListAdapter extends BaseAdapter {

    private class Viewholder {
        TextView tvArtistName, tvPersonnage, tvDistance;
        ImageView ivComicMuralPhoto;
        Button btnFav;
    }

    private Activity context;
    private List<Comic> items;
    private Location location;
    private Viewholder mViewHolder;
    private FavouriteItemListener favouriteItemListener;

    public ComicListAdapter(Activity context) {
        this.context = context;
        favouriteItemListener = (FavouriteItemListener) context;
        location = LocationUtil.getInstance().getLocation();
        items = LandMarksDatabase.getInstance(context).getSortedCommic(location);
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
            mViewHolder = (Viewholder) view.getTag();
        }

        final Comic currentComic = items.get(i);

        mViewHolder.tvArtistName.setText(String.format("%s%s", context.getText(R.string.txt_detail_illustrator), currentComic.getNameOfIllustrator()));

        mViewHolder.tvPersonnage.setText(String.format("%s%s", context.getText(R.string.txt_detail_featuring), currentComic.getPersonnage()));

        setupDistance(mViewHolder, currentComic);

        setupImgView(mViewHolder, currentComic);

        final Button btnFav = mViewHolder.btnFav;

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            if (LandMarksDatabase.getInstance(context).checkFav(currentComic)) {
                btnFav.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.ic_favorite_black_36dp));
            } else {
                btnFav.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.ic_favorite_border_black_36dp));
            }

            btnFav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (LandMarksDatabase.getInstance(context).checkFav(currentComic)) {
                        btnFav.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.ic_favorite_border_black_36dp));
                    } else {
                        btnFav.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.ic_favorite_black_36dp));
                    }
                    favouriteItemListener.onFavButtonClick(currentComic);
                }
            });

        }else{
            if (LandMarksDatabase.getInstance(context).checkFav(currentComic)) {
                btnFav.setBackground(context.getResources().getDrawable(R.drawable.ic_favorite_black_36dp));
            } else {
                btnFav.setBackground(context.getResources().getDrawable(R.drawable.ic_favorite_border_black_36dp));
            }

            btnFav.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("NewApi")
                @Override
                public void onClick(View view) {
                    if (LandMarksDatabase.getInstance(context).checkFav(currentComic)) {
                        btnFav.setBackground(context.getResources().getDrawable(R.drawable.ic_favorite_border_black_36dp));
                    } else {
                        btnFav.setBackground(context.getResources().getDrawable(R.drawable.ic_favorite_black_36dp));
                    }
                    favouriteItemListener.onFavButtonClick(currentComic);
                }
            });
        }
        return view;
    }

    @NonNull
    private View setupView(ViewGroup viewGroup) {
        View view;
        view = context.getLayoutInflater().inflate(R.layout.fragment_street_art_detail,viewGroup,false);
        mViewHolder = new Viewholder();

        mViewHolder.tvArtistName = view.findViewById(R.id.tv_detail_museum_name);
        mViewHolder.tvPersonnage = view.findViewById(R.id.tv_detail_streetart_explanation);
        mViewHolder.ivComicMuralPhoto = view.findViewById(R.id.iv_detail_streetart);
        mViewHolder.tvDistance = view.findViewById(R.id.tv_detail_streetart_distance);
        mViewHolder.btnFav = view.findViewById(R.id.btn_favourite_streetart);

        view.setTag(mViewHolder);
        return view;
    }

    private void setupDistance(Viewholder mViewHolder, Comic currentComic) {
        //If location != null calculate distance and fill the textview with distance in km

        if (location != null){
            float distance = LocationUtil.getInstance().getDistance(currentComic.getCoordX(), currentComic.getCoordY(), location);
            mViewHolder.tvDistance.setText(String.format("%.2f km", distance));
        }else {
            mViewHolder.tvDistance.setVisibility(View.GONE);
        }
    }

    private void setupImgView(Viewholder mViewHolder, Comic currentComic) {
        if(currentComic.isHasIMG()) {
            mViewHolder.ivComicMuralPhoto.setVisibility(View.VISIBLE);


            String imgId = currentComic.getImgUrl()
                    .split("files/")[1]
                    .split("[/]")[0];

            ContextWrapper cw = new ContextWrapper(context);
            File directory = cw.getDir("images", MODE_PRIVATE);
            File file = new File(directory, imgId +".jpeg");
            Picasso.with(context)
                    .load(file)
                    .into(mViewHolder.ivComicMuralPhoto);
        }else {
            mViewHolder.ivComicMuralPhoto.setVisibility(View.INVISIBLE);
        }
    }
}
