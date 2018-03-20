package be.ehb.xplorebxl.Utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import be.ehb.xplorebxl.Database.LandMarksDatabase;
import be.ehb.xplorebxl.Model.Museum;
import be.ehb.xplorebxl.R;

/**
 * Created by TDS-Team on 16/03/2018.
 */

public class MuseumListAdapter extends BaseAdapter {

    private class ViewHolder {
        Button btnPhone, btnWebsite, btnEmail;
        TextView tvName, tvAdress;
    }

    private Activity context;
    private List<Museum> items;

    public MuseumListAdapter(Activity context) {
        this.context = context;
        items = LandMarksDatabase.getInstance(context).getMuseumDao().getAllMuseums();
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

        ViewHolder mViewHolder;

        if (view == null){
            view = context.getLayoutInflater().inflate(R.layout.fragment_museum_detail, viewGroup, false);
            mViewHolder = new ViewHolder();

            mViewHolder.btnEmail = view.findViewById(R.id.btn_detail_museum_mail);
            mViewHolder.btnPhone = view.findViewById(R.id.btn_detail_museum_phone);
            mViewHolder.btnWebsite = view.findViewById(R.id.btn_detail_museum_website);

            mViewHolder.tvAdress = view.findViewById(R.id.tv_detail_museum_address);
            mViewHolder.tvName = view.findViewById(R.id.tv_detail_museum_name);

            view.setTag(mViewHolder);

        }else {
            mViewHolder = (ViewHolder) view.getTag();
        }

        final Museum currentMuseum = items.get(i);

        mViewHolder.tvName.setText(currentMuseum.getName());
        mViewHolder.tvAdress.setText(currentMuseum.getAdres());

        if(TextUtils.isEmpty(currentMuseum.getTel())){
            mViewHolder.btnPhone.setVisibility(View.GONE);
        }else{
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


        if(TextUtils.isEmpty(currentMuseum.getEmail())){
            mViewHolder.btnEmail.setVisibility(View.GONE);
        }else {
            mViewHolder.btnEmail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                            "mailto", currentMuseum.getEmail(), null));
                    viewGroup.getContext().startActivity(Intent.createChooser(emailIntent, "Send email..."));
                }
            });
        }

        if(TextUtils.isEmpty(currentMuseum.getUrl())){
            mViewHolder.btnWebsite.setVisibility(View.GONE);
        }else{
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




        return view;
    }
}
