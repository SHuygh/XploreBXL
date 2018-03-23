package be.ehb.xplorebxl.View.Fragments;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import be.ehb.xplorebxl.Model.Comic;
import be.ehb.xplorebxl.R;
import be.ehb.xplorebxl.Utils.Adapter.ComicListAdapter;
import be.ehb.xplorebxl.Utils.ListviewItemListener;

/**
 * A simple {@link Fragment} subclass.

 * create an instance of this fragment.
 */
public class ComicListViewFragment extends Fragment {

    private ListView lvComics;

    private ListviewItemListener callback;


    public ComicListViewFragment() {
        // Required empty public constructor
    }


    public static ComicListViewFragment newInstance() {
        ComicListViewFragment fragment = new ComicListViewFragment();

        return fragment;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        callback = (ListviewItemListener) context;
    }

    //voor oudere android versies
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callback = (ListviewItemListener) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_comic_list_view, container, false);
        lvComics = rootView.findViewById(R.id.lv_comics);

        final ComicListAdapter comicListAdapter = new ComicListAdapter(getActivity());
        lvComics.setAdapter(comicListAdapter);

        lvComics.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Comic selectedComic = (Comic) comicListAdapter.getItem(i);
                callback.itemSelected(selectedComic);
                getActivity().onBackPressed();
            }
        });

        return rootView;
    }

}
