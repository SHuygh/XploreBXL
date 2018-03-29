package be.ehb.xplorebxl.View.Fragments.Comic;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import be.ehb.xplorebxl.Model.Comic;
import be.ehb.xplorebxl.R;
import be.ehb.xplorebxl.Utils.Adapter.ComicListAdapter;
import be.ehb.xplorebxl.Utils.Listener.FavouriteItemListener;
import be.ehb.xplorebxl.Utils.Listener.ListviewItemListener;

/**
 * A simple {@link Fragment} subclass.

 * create an instance of this fragment.
 */
public class ComicListViewFragment extends Fragment {

    private ListView lvComics;

    private ListviewItemListener callback;

    private FavouriteItemListener faveCallback;


    public ComicListViewFragment() {
        // Required empty public constructor
    }


    public static ComicListViewFragment newInstance() {
        return new ComicListViewFragment();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        callback = (ListviewItemListener) context;

        faveCallback = (FavouriteItemListener) context;
    }

    //voor oudere android versies
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callback = (ListviewItemListener) activity;

        faveCallback = (FavouriteItemListener) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getString(R.string.pu_comic));

        View rootView = inflater.inflate(R.layout.fragment_comic_list_view, container, false);
        lvComics = rootView.findViewById(R.id.lv_comics);

        final ComicListAdapter comicListAdapter = new ComicListAdapter(getActivity());
        lvComics.setAdapter(comicListAdapter);

        lvComics.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Comic selectedComic = (Comic) comicListAdapter.getItem(i);
                callback.itemSelected(selectedComic);
            }
        });

        

        return rootView;
    }

}
