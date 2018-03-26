package be.ehb.xplorebxl.View.Fragments.Comic;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.content.ContextWrapper;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;

import be.ehb.xplorebxl.Model.Comic;
import be.ehb.xplorebxl.R;
import be.ehb.xplorebxl.Utils.LocationUtil;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class ComicDetailFragment extends Fragment {

    private Comic selectedComic;
    private ImageView ivComic;
    private TextView tvIllustratorName, tvPersonnage, tvDistance;

    private Location location;


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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = setupView(inflater, container);


        tvIllustratorName.setText(String.format("Illustrator: %s", selectedComic.getNameOfIllustrator()));
        tvPersonnage.setText(String.format("Feat. %s", selectedComic.getPersonnage()));

        setupDistance();

        setupImgView();

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
        tvIllustratorName = rootView.findViewById(R.id.tv_detail_streetart_artistname);
        tvPersonnage = rootView.findViewById(R.id.tv_detail_streetart_explanation);
        tvDistance = rootView.findViewById(R.id.tv_detail_streetart_distance);
        return rootView;
    }

}
