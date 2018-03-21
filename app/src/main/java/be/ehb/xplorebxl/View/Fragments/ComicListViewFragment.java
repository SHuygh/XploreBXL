package be.ehb.xplorebxl.View.Fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import be.ehb.xplorebxl.R;
import be.ehb.xplorebxl.Utils.ComicListAdapter;

/**
 * A simple {@link Fragment} subclass.

 * create an instance of this fragment.
 */
public class ComicListViewFragment extends Fragment {

    private ListView lvComics;


    public ComicListViewFragment() {
        // Required empty public constructor
    }


    public static ComicListViewFragment newInstance() {
        ComicListViewFragment fragment = new ComicListViewFragment();

        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_comic_list_view, container, false);
        lvComics = rootView.findViewById(R.id.lv_comics);

        ComicListAdapter comicListAdapter = new ComicListAdapter(getActivity());
        lvComics.setAdapter(comicListAdapter);

        return rootView;
    }

}
