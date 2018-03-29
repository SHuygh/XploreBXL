package be.ehb.xplorebxl.Utils.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import be.ehb.xplorebxl.Database.LandMarksDatabase;
import be.ehb.xplorebxl.Model.Museum;
import be.ehb.xplorebxl.R;
import be.ehb.xplorebxl.Utils.Listener.FavouriteItemListener;
import be.ehb.xplorebxl.Utils.LocationUtil;

/**
 * Created by TDS-Team on 16/03/2018.
 */

public class MuseumListAdapter extends BaseAdapter {

    private class ViewHolder {
        Button btnPhone, btnWebsite, btnEmail;
        TextView tvName, tvAdress, tvDistance;
        ImageView ivMuseum;
        Button btnFav;
    }

    private Activity context;
    private List<Museum> items;
    private Location location;
    private ViewHolder mViewHolder;
    private FavouriteItemListener favouriteItemListener;

    public MuseumListAdapter(Activity context) {
        this.context = context;
        favouriteItemListener = (FavouriteItemListener) context;
        location = LocationUtil.getInstance().getLocation();
        items = LandMarksDatabase.getInstance(context).getSortedMuseums(location);

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
    public View getView(int i, View view, final ViewGroup viewGroup) {

        if (view == null) {
            view = setupView(viewGroup);
        } else {
            mViewHolder = (ViewHolder) view.getTag();
        }

        final Museum currentMuseum = items.get(i);

        mViewHolder.tvName.setText(currentMuseum.getName());
        mViewHolder.tvAdress.setText(currentMuseum.getAdres());

        setupDistance(currentMuseum);

        setupButtons(viewGroup, currentMuseum);

        randomMuseumImage(mViewHolder);
        final Button btnFav = mViewHolder.btnFav;

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            if (LandMarksDatabase.getInstance(context).checkFav(currentMuseum)) {
                btnFav.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.ic_favorite_black_36dp));
            } else {
                btnFav.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.ic_favorite_border_black_36dp));
            }

            btnFav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (LandMarksDatabase.getInstance(context).checkFav(currentMuseum)) {
                        btnFav.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.ic_favorite_border_black_36dp));
                    } else {
                        btnFav.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.ic_favorite_black_36dp));
                    }
                    favouriteItemListener.onFavButtonClick(currentMuseum);
                }
            });

        }else{
            if (LandMarksDatabase.getInstance(context).checkFav(currentMuseum)) {
                btnFav.setBackground(context.getResources().getDrawable(R.drawable.ic_favorite_black_36dp));
            } else {
                btnFav.setBackground(context.getResources().getDrawable(R.drawable.ic_favorite_border_black_36dp));
            }

            btnFav.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("NewApi")
                @Override
                public void onClick(View view) {
                    if (LandMarksDatabase.getInstance(context).checkFav(currentMuseum)) {
                        btnFav.setBackground(context.getResources().getDrawable(R.drawable.ic_favorite_border_black_36dp));
                    } else {
                        btnFav.setBackground(context.getResources().getDrawable(R.drawable.ic_favorite_black_36dp));
                    }
                    favouriteItemListener.onFavButtonClick(currentMuseum);
                }
            });
        }
        return view;
    }

    private void setupButtons(final ViewGroup viewGroup, final Museum currentMuseum) {
        setupBtnPhone(viewGroup, currentMuseum);
        setupBtnEmail(viewGroup, currentMuseum);
        setupBtnWebsite(viewGroup, currentMuseum);
    }

    private void setupBtnWebsite(final ViewGroup viewGroup, final Museum currentMuseum) {
        if(TextUtils.isEmpty(currentMuseum.getUrl())){
            mViewHolder.btnWebsite.setVisibility(View.GONE);
        }else{
            mViewHolder.btnWebsite.setVisibility(View.VISIBLE);
            mViewHolder.btnWebsite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String url = currentMuseum.getUrl();

                    if (!url.startsWith("http://") && !url.startsWith("https://")) {
                        url = "http://" + url;
                    }

                    Uri uri = Uri.parse(url);

                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);

                    viewGroup.getContext().startActivity(intent);
                }
            });
        }
    }

    private void setupBtnEmail(final ViewGroup viewGroup, final Museum currentMuseum) {
        if(TextUtils.isEmpty(currentMuseum.getEmail())){
            mViewHolder.btnEmail.setVisibility(View.GONE);
        }else {
            mViewHolder.btnEmail.setVisibility(View.VISIBLE);
            mViewHolder.btnEmail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                            "mailto", currentMuseum.getEmail(), null));
                    viewGroup.getContext().startActivity(Intent.createChooser(emailIntent, "Send email..."));
                }
            });
        }
    }

    private void setupBtnPhone(final ViewGroup viewGroup, final Museum currentMuseum) {
        if(TextUtils.isEmpty(currentMuseum.getTel())){
            mViewHolder.btnPhone.setVisibility(View.GONE);
        }else{
            mViewHolder.btnPhone.setVisibility(View.VISIBLE);
            mViewHolder.btnPhone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String tel = currentMuseum.getTel();
                    Uri uri = Uri.parse("tel:" + tel);

                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    viewGroup.getContext().startActivity(intent);
                }
            });
        }
    }

    private void setupDistance(Museum currentMuseum) {
        if (location != null){
            float distance = LocationUtil.getInstance().getDistance(currentMuseum.getCoordX(), currentMuseum.getCoordY(), location);
            mViewHolder.tvDistance.setText(String.format("%.2f km", distance));

        }else {
            mViewHolder.tvDistance.setVisibility(View.GONE);
        }
    }

    @NonNull
    private View setupView(ViewGroup viewGroup) {
        View view;
        view = context.getLayoutInflater().inflate(R.layout.fragment_museum_detail, viewGroup, false);
        mViewHolder = new ViewHolder();

        mViewHolder.ivMuseum = view.findViewById(R.id.iv_detail_museum);

        mViewHolder.btnEmail = view.findViewById(R.id.btn_detail_museum_mail);
        mViewHolder.btnPhone = view.findViewById(R.id.btn_detail_museum_phone);
        mViewHolder.btnWebsite = view.findViewById(R.id.btn_detail_museum_website);

        mViewHolder.tvAdress = view.findViewById(R.id.tv_detail_museum_address);
        mViewHolder.tvName = view.findViewById(R.id.tv_detail_museum_name);
        mViewHolder.tvDistance = view.findViewById(R.id.tv_detail_museum_distance);
        mViewHolder.btnFav = view.findViewById(R.id.btn_favourite_museum);


        view.setTag(mViewHolder);
        return view;
    }



    private void randomMuseumImage(ViewHolder viewHolder) {

        Random random = new Random();
        List<Integer> generated = new ArrayList<>();
        generated.add(R.drawable.museum1);
        generated.add(R.drawable.museum2);
        generated.add(R.drawable.museum3);
        generated.add(R.drawable.museum4);
        generated.add(R.drawable.museum5);
        generated.add(R.drawable.museum6);

        Integer next = random.nextInt(5);
        viewHolder.ivMuseum.setImageResource(generated.get(next));
    }

}
