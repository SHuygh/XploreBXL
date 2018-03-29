package be.ehb.xplorebxl.Utils.Adapter;

import android.content.Context;
import android.content.ContextWrapper;
import android.location.Location;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import be.ehb.xplorebxl.Database.LandMarksDatabase;
import be.ehb.xplorebxl.Model.Comic;
import be.ehb.xplorebxl.Model.Museum;
import be.ehb.xplorebxl.Model.StreetArt;
import be.ehb.xplorebxl.R;
import be.ehb.xplorebxl.Utils.Listener.ListviewItemListener;
import be.ehb.xplorebxl.Utils.LocationUtil;
import be.ehb.xplorebxl.View.Activities.MainActivity;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Millmaster on 26/03/2018.
 */

public class FabAdapter extends RecyclerView.Adapter<FabAdapter.CustomViewHolder> {

    class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected ImageView imageView;
        protected TextView tvTitle;
        protected TextView tvDistance;

        public CustomViewHolder(View view) {
            super(view);
            this.imageView = view.findViewById(R.id.thumbnail);
            this.tvTitle = view.findViewById(R.id.title);
            this.tvDistance = view.findViewById(R.id.tv_fab_distance);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            callback.itemSelected(items.get(getAdapterPosition()));
        }
    }

    private ArrayList<Object> items;
    private Context mContext;
    private Location location;

    private ListviewItemListener callback;

    public FabAdapter(Context mContext, int filterId) {
        this.location = LocationUtil.getInstance().getLocation();
        this.items = LandMarksDatabase.getInstance(mContext).getSortedList(this.location, filterId);
        this.mContext = mContext;
        this.callback = (MainActivity) mContext;
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
            customViewHolder.tvTitle.setText("ERROR");
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

        customViewHolder.tvTitle.setText(streetArt.getNameOfArtist());

        setupDistance(streetArt.getCoordX(), streetArt.getCoordY(), customViewHolder);
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

        customViewHolder.tvTitle.setText(comic.getPersonnage());

        setupDistance(comic.getCoordX(), comic.getCoordY(), customViewHolder);

    }

    public void setupView(CustomViewHolder customViewHolder, Museum museum) {
        customViewHolder.tvTitle.setText(museum.getName());
        randomMuseumImage(customViewHolder);
        setupDistance(museum.getCoordX(), museum.getCoordY(), customViewHolder);

    }

    public void setupDistance(double coordx, double coordy, CustomViewHolder customViewHolder) {

        if (location != null){
            customViewHolder.tvDistance.setVisibility(View.VISIBLE);
            float distance = LocationUtil.getInstance().getDistance(coordx, coordy, location);
            customViewHolder.tvDistance.setText(String.format("%.1f km", distance));
        }else {
            customViewHolder.tvDistance.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return (null != items ? items.size() : 0);
    }

    private void randomMuseumImage(CustomViewHolder viewHolder) {

        Random random = new Random();
        List<Integer> generated = new ArrayList<Integer>();
        generated.add(R.drawable.museum1);
        generated.add(R.drawable.museum2);
        generated.add(R.drawable.museum3);
        generated.add(R.drawable.museum4);
        generated.add(R.drawable.museum5);
        generated.add(R.drawable.museum6);

        Integer next = random.nextInt(5);
        viewHolder.imageView.setImageResource(generated.get(next));
    }

}



