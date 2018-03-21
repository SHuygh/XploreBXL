package be.ehb.xplorebxl.View.Fragments;

import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import be.ehb.xplorebxl.Model.Comic;
import be.ehb.xplorebxl.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ComicDetailFragment extends Fragment {

    private Comic selectedComic;
    private ImageView ivComic;
    private TextView tvIllustratorName, tvPersonnage;


    public ComicDetailFragment() {
        // Required empty public constructor
    }

    public static ComicDetailFragment newInstance (Comic comic){
        ComicDetailFragment fragment = new ComicDetailFragment();
        fragment.selectedComic = comic;
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_street_art_detail,container,false);

        ivComic = rootView.findViewById(R.id.iv_detail_streetart);
        tvIllustratorName = rootView.findViewById(R.id.tv_detail_streetart_artistname);
        tvPersonnage = rootView.findViewById(R.id.tv_detail_streetart_explanation);

        tvIllustratorName.setText("Illustrator: " + selectedComic.getNameOfIllustrator());
        tvPersonnage.setText("Feat. " + selectedComic.getPersonnage());

        if (selectedComic.isHasIMG()){

            String url = selectedComic.getImgUrl();
            Uri uri = Uri.parse(url);
            Picasso.with(getActivity()).load(uri).into(ivComic);
        }else{
            ivComic.setVisibility(View.INVISIBLE);
        }



        return rootView;
    }

}
