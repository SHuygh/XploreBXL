package be.ehb.xplorebxl.View.Fragments.Comic;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.ContextWrapper;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;

import be.ehb.xplorebxl.Database.LandMarksDatabase;
import be.ehb.xplorebxl.Model.Comic;
import be.ehb.xplorebxl.R;
import be.ehb.xplorebxl.Utils.Listener.FavouriteItemListener;
import be.ehb.xplorebxl.Utils.LocationUtil;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class ComicDetailFragment extends Fragment {

    private Comic selectedComic;
    private ImageView ivComic;
    private TextView tvIllustratorName, tvPersonnage, tvDistance;
    private Button btnFav;
    private Location location;
    private FavouriteItemListener favouriteItemListener;

    public ComicDetailFragment() {
        // Required empty public constructor
    }

    public static ComicDetailFragment newInstance (Comic comic){
        ComicDetailFragment fragment = new ComicDetailFragment();
        fragment.selectedComic = comic;
        fragment.location = LocationUtil.getInstance().getLocation();

        return fragment;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        favouriteItemListener = (FavouriteItemListener) context;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        favouriteItemListener = (FavouriteItemListener) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = setupView(inflater, container);

        tvIllustratorName.setText(String.format("%s%s", getString(R.string.txt_detail_illustrator), selectedComic.getNameOfIllustrator()));

        tvPersonnage.setText(String.format("%s%s", getString(R.string.txt_detail_featuring), selectedComic.getPersonnage()));

        setupDistance();

        setupImgView();

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            if (LandMarksDatabase.getInstance(getActivity()).checkFav(selectedComic)) {
                btnFav.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_favorite_black_36dp));
            } else {
                btnFav.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_favorite_border_black_36dp));
            }

            btnFav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (LandMarksDatabase.getInstance(getActivity()).checkFav(selectedComic)) {
                        btnFav.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_favorite_border_black_36dp));
                    } else {
                        btnFav.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_favorite_black_36dp));
                    }
                    favouriteItemListener.onFavButtonClick(selectedComic);
                }
            });

        }else{
            if (LandMarksDatabase.getInstance(getActivity()).checkFav(selectedComic)) {
                btnFav.setBackground(getResources().getDrawable(R.drawable.ic_favorite_black_36dp));
            } else {
                btnFav.setBackground(getResources().getDrawable(R.drawable.ic_favorite_border_black_36dp));
            }

            btnFav.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("NewApi")
                @Override
                public void onClick(View view) {
                    if (LandMarksDatabase.getInstance(getActivity()).checkFav(selectedComic)) {
                        btnFav.setBackground(getResources().getDrawable(R.drawable.ic_favorite_border_black_36dp));
                    } else {
                        btnFav.setBackground(getResources().getDrawable(R.drawable.ic_favorite_black_36dp));
                    }
                    favouriteItemListener.onFavButtonClick(selectedComic);
                }
            });
        }


        return rootView;
    }

    public void setupImgView() {
        if(selectedComic.isHasIMG()) {
            String imgId = selectedComic.getImgUrl()
                    .split("files/")[1]
                    .split("[/]")[0];

            ContextWrapper cw = new ContextWrapper(getActivity());
            File directory = cw.getDir("images", MODE_PRIVATE);
            File file = new File(directory, imgId +".jpeg");
            Picasso.with(getActivity())
                    .load(file)
                    .into(ivComic);
        }else {
            ivComic.setVisibility(View.INVISIBLE);
        }
    }

    public void setupDistance() {
        if (location != null){
            tvDistance.setVisibility(View.VISIBLE);
            float distance = LocationUtil.getInstance().getDistance(selectedComic.getCoordX(), selectedComic.getCoordY(), location);
            tvDistance.setText(String.format("%.2f km", distance));
        }else {
            tvDistance.setVisibility(View.GONE);
        }
    }

    @NonNull
    public View setupView(LayoutInflater inflater, ViewGroup container) {
        View rootView = inflater.inflate(R.layout.fragment_street_art_detail,container,false);

        ivComic = rootView.findViewById(R.id.iv_detail_streetart);
        tvIllustratorName = rootView.findViewById(R.id.tv_detail_museum_name);
        tvPersonnage = rootView.findViewById(R.id.tv_detail_streetart_explanation);
        tvDistance = rootView.findViewById(R.id.tv_detail_streetart_distance);
        btnFav = rootView.findViewById(R.id.btn_favourite_streetart);

        return rootView;
    }

}
