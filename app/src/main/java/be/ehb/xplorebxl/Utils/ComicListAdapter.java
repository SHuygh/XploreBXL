package be.ehb.xplorebxl.Utils;

import android.app.Activity;
import android.content.ContextWrapper;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

import be.ehb.xplorebxl.Database.LandMarksDatabase;
import be.ehb.xplorebxl.Model.Comic;
import be.ehb.xplorebxl.Model.StreetArt;
import be.ehb.xplorebxl.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Q on 21-3-2018.
 */

public class ComicListAdapter extends BaseAdapter {

    private class Viewholder {
        TextView tvArtistName, tvPersonnage;
        ImageView ivComicMuralPhoto;
    }

    private Activity context;
    private List<Comic> items;


    public ComicListAdapter(Activity context) {
        this.context = context;
        items = LandMarksDatabase.getInstance(context).getComicDao().getAllComics();
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

        Viewholder mViewHolder;

        if (view == null){
            view = context.getLayoutInflater().inflate(R.layout.fragment_street_art_detail,viewGroup,false);
            mViewHolder = new Viewholder();

            mViewHolder.tvArtistName = view.findViewById(R.id.tv_detail_streetart_artistname);
            mViewHolder.tvPersonnage = view.findViewById(R.id.tv_detail_streetart_explanation);
            mViewHolder.ivComicMuralPhoto = view.findViewById(R.id.iv_detail_streetart);

            view.setTag(mViewHolder);

        }else {
            mViewHolder = (Viewholder) view.getTag();
        }

        Comic currentComic = items.get(i);

        mViewHolder.tvArtistName.setText("Illustrator: " + currentComic.getNameOfIllustrator());
        mViewHolder.tvPersonnage.setText("Feat. " + currentComic.getPersonnage());


/*        if(currentComic.isHasIMG()) {
            String url = currentComic.getImgUrl();

            Uri uri = Uri.parse(url);
            Picasso.with(context).load(uri).into(mViewHolder.ivComicMuralPhoto);
        }else {
            mViewHolder.ivComicMuralPhoto.setVisibility(View.INVISIBLE);
        }*/

        if(currentComic.isHasIMG()) {

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

        return view;
    }
}
