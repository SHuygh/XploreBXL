package be.ehb.xplorebxl.Utils;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import be.ehb.xplorebxl.View.Activities.MainActivity;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Downloader {
    ArrayList<Target> targetComicList = new ArrayList<Target>();
    ArrayList<Target> targetStreetartList = new ArrayList<Target>();
    private Context context;

    private static Downloader INSTANCE = new Downloader();

    private Downloader() {
    }

    public static Downloader getInstance(){
        return  INSTANCE;
    }

    public static Downloader getInstance(Context context){
        INSTANCE.context = context;
        return  INSTANCE;
    }

    public void downloadData() {
        final RESTHandler handler = new RESTHandler((MainActivity) context);

        Thread backGroundThread = new Thread(new Runnable() {
            @Override
            public void run() {

                String url_museums = "https://opendata.brussel.be/api/records/1.0/search/?dataset=musea-in-brussel&rows=70";
                String url_streetArt = "https://opendata.brussel.be/api/records/1.0/search/?dataset=streetart&rows=70";
                String url_comic = "https://opendata.brussel.be/api/records/1.0/search/?dataset=comic-book-route&rows=70";

                ArrayList<String> urlList = new ArrayList<String>();

                urlList.add(url_museums);
                urlList.add(url_streetArt);
                urlList.add(url_comic);


                for (String url : urlList) {

                    try {

                        OkHttpClient client = new OkHttpClient();

                        Request request = new Request.Builder()
                                .url(url)
                                .get()
                                .build();


                        Response response = client.newCall(request).execute();

                        Message msg = new Message();

                        Bundle bndl = new Bundle();
                        bndl.putString("json_data", response.body().string());
                        msg.setData(bndl);

                        handler.sendMessage(msg);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        );
        backGroundThread.start();

    }

    //BASED ON http://www.codexpedia.com/android/android-download-and-save-image-through-picasso/
    public void downloadImgs(List<String> imgs, String type) {
        ArrayList<String> imgUrlList = (ArrayList<String>) imgs;

        int img_length = imgUrlList.size();
        for (int i = 0; i < img_length; i++) {

            String url = imgUrlList.get(i);
            if (!TextUtils.isEmpty(url)) {
                String imgId = url.split("files/")[1].split("[/]")[0];


                Uri uri = Uri.parse(url);

                Target target = getTarget(imgId);
                if (type == "streetart") {
                    targetStreetartList.add(target);
                    Picasso.with(context)
                            .load(uri)
                            .into(targetStreetartList.get(i));
                } else if (type == "comic") {
                    targetComicList.add(target);
                    Picasso.with(context)
                            .load(uri)
                            .into(targetComicList.get(i));
                }

            } else {
                Target target = getTarget("");
                if (type == "streetart") {
                    targetStreetartList.add(target);
                } else if (type == "comic") {
                    targetComicList.add(target);
                }
            }

        }

    }

    public Target getTarget(final String imgId) {
        Target target = new Target() {
            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        ContextWrapper cw = new ContextWrapper(context);
                        final File directory = cw.getDir("images", MainActivity.MODE_PRIVATE);

                        File file = new File(directory, imgId + ".jpeg");
                        try {
                            FileOutputStream ostream = new FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, ostream);
                            ostream.flush();
                            ostream.close();
                        } catch (IOException e) {
                            Log.e("IOException", e.getLocalizedMessage());
                        }
                    }
                }).start();
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };

        return target;
    }
}