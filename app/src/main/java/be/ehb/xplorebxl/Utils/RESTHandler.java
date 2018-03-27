package be.ehb.xplorebxl.Utils;

import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import be.ehb.xplorebxl.Database.LandMarksDatabase;
import be.ehb.xplorebxl.Model.Comic;
import be.ehb.xplorebxl.Model.Museum;
import be.ehb.xplorebxl.Model.StreetArt;
import be.ehb.xplorebxl.View.Activities.MainActivity;

/**
 * Created by huyghstijn on 19/03/2018.
 */

public class RESTHandler extends Handler {
    String TAG = "testtest";
    public static boolean isSucces = true;


    private final String KEY_PARAMETERS = "parameters",
                                KEY_DATASET = "dataset",
                                    NAME_DATASET_MUSEA = "musea-in-brussel",
                                    NAME_DATASET_STREETART = "streetart",
                                    NAME_DATASET_COMIC = "comic-book-route";

    private final String
            KEY_RECORDS = "records",
                KEY_RECORDID = "recordid",
                KEY_FIELDS = "fields",
                    KEY_ILLUSTRATOR = "auteur_s",
                    KEY_NAME = "naam_van_het_museum",
                    KEY_CITY = "gemeente",
                    KEY_ADRES = "adres",
                    KEY_URL = "site_web_website",
                    KEY_TEL = "telephone_telefoon",
                    KEY_EMAIL = "e_mail",
                    KEY_COORDX = "latitude_breedtegraad",
                    KEY_COORDY = "longitude_lengtegraad";

    private final String
            KEY_PHOTO_OBJECT = "photo",
                KEY_PHOTOID = "id",
            KEY_PERSONNAGE = "personnage_s",
            KEY_ADRESSE = "adresse",
            KEY_LOCATION = "location",
            KEY_ARTISTNAME_STREETART = "naam_van_de_kunstenaar",
            KEY_EXPLENTATION_STREETART = "verduidelijking",
            KEY_COORDINATES_STREETART = "geocoordinates",
            KEY_COORDINATES_COMIC = "coordonnees_geographiques";


    private MainActivity context;
    private Downloader downloader;

    RESTHandler(MainActivity context) {
        this.context = context;
        downloader = Downloader.getInstance();
    }

    @Override
    public void handleMessage(Message msg) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        String json_data = msg.getData().getString("json_data");

        try {
            JSONObject jsonObject = new JSONObject(json_data);

            String dataset = jsonObject.getJSONObject(KEY_PARAMETERS).getJSONArray(KEY_DATASET).getString(0);

            JSONArray records = jsonObject.getJSONArray(KEY_RECORDS);
            int json_length = records.length();

            switch (dataset){
                case NAME_DATASET_MUSEA:
                    parseMuseums(records, json_length);
                    break;
                case NAME_DATASET_STREETART:
                    parseStreetArt(records, json_length);
                    downloader.downloadImgs(LandMarksDatabase.getInstance(context).getStreetArtImgUrl(), "streetart", context);
                    break;
                case NAME_DATASET_COMIC:
                    parseComics(records, json_length);
                    downloader.downloadImgs(LandMarksDatabase.getInstance(context).getComicImgUrl(), "comic", context);
                    break;
            }


        } catch (JSONException e) {
            //if there was an error, the app will try to redownload data upon next onCreate of mainactivity
            sharedPreferences.edit().putBoolean("AppHasDownloadedDataBefore", false).apply();
            e.printStackTrace();
        }
        //redrawmarkers after download
        if(context.map != null) {
            context.drawMarkers();
        }
        sharedPreferences.edit().putBoolean("AppHasDownloadedDataBefore", true).apply();

    }


    private void parseMuseums(JSONArray records, int json_length) throws JSONException {

        for(int i = 0; i < json_length; i++){
            JSONObject JSON_Museum = records.getJSONObject(i);

            String recordId = JSON_Museum.getString(KEY_RECORDID);
            JSONObject fields = JSON_Museum.getJSONObject(KEY_FIELDS);
                String name = fields.getString(KEY_NAME);
                String city = fields.getString(KEY_CITY);
                String adres = fields.getString(KEY_ADRES);
                String url = (fields.has(KEY_URL)) ? fields.getString(KEY_URL) : "";
                String tel = (fields.has(KEY_TEL)) ? fields.getString(KEY_TEL) : "";
                String email = fields.getString(KEY_EMAIL);
                double coordX = fields.getDouble(KEY_COORDX);
                double coordY = fields.getDouble(KEY_COORDY);

            Museum museum = new Museum(recordId, name, city, adres, url, tel, email, coordX, coordY);

            ArrayList<String> recordIdList = (ArrayList<String>) LandMarksDatabase.getInstance(context).getMuseumRecordID();

            if(recordIdList.contains(recordId)){
                LandMarksDatabase.getInstance(context).updateMuseum(museum);
            }else{
                LandMarksDatabase.getInstance(context).insertMuseum(museum);
            }
        }

    }

    private void parseComics(JSONArray records, int json_lenght) throws JSONException{
        for (int i = 0; i < json_lenght; i++){

            JSONObject JSON_Comics = records.getJSONObject(i);

            String recordId = JSON_Comics.getString(KEY_RECORDID);
            JSONObject fields = JSON_Comics.getJSONObject(KEY_FIELDS);
                String nameOfIllustrator = fields.getString(KEY_ILLUSTRATOR);
                String personnage = fields.getString(KEY_PERSONNAGE);
            String imgId =
                    (fields.has(KEY_PHOTO_OBJECT))?
                            fields.getJSONObject(KEY_PHOTO_OBJECT).getString(KEY_PHOTOID):
                            "";
            double coordX = fields.getJSONArray(KEY_COORDINATES_COMIC).getDouble(0);
            double coordY = fields.getJSONArray(KEY_COORDINATES_COMIC).getDouble(1);

            Comic comic = new Comic(recordId, nameOfIllustrator, personnage, imgId, coordX, coordY);
            ArrayList<String> recordIdList = (ArrayList<String>) LandMarksDatabase.getInstance(context).getComicRecordID();

            if(recordIdList.contains(recordId)){
                LandMarksDatabase.getInstance(context).updateComic(comic);
            }else{
                LandMarksDatabase.getInstance(context).insertComic(comic);
            }


        }

    }


    private void parseStreetArt(JSONArray records, int json_length) throws JSONException {
        for(int i = 0; i < json_length; i++){
            JSONObject JSON_StreetArt = records.getJSONObject(i);

            String recordId = JSON_StreetArt.getString(KEY_RECORDID);
            JSONObject fields = JSON_StreetArt.getJSONObject(KEY_FIELDS);

            String nameOfArtist = fields.getString(KEY_ARTISTNAME_STREETART);

            String adres =
                    (fields.has(KEY_ADRES))? fields.getString(KEY_ADRES):
                        (fields.has(KEY_ADRESSE))? fields.getString(KEY_ADRESSE):
                            (fields.has(KEY_LOCATION))? fields.getString(KEY_LOCATION):
                                    "";

            String explenation = (fields.has(KEY_EXPLENTATION_STREETART)) ? fields.getString(KEY_EXPLENTATION_STREETART) : "";
            String imgId =
                    (fields.has(KEY_PHOTO_OBJECT))?
                            fields.getJSONObject(KEY_PHOTO_OBJECT).getString(KEY_PHOTOID):
                                 "";

            double coordX = fields.getJSONArray(KEY_COORDINATES_STREETART).getDouble(0);
            double coordY = fields.getJSONArray(KEY_COORDINATES_STREETART).getDouble(1);

            if(!TextUtils.isEmpty(imgId)) {
                StreetArt streetArt = new StreetArt(recordId, nameOfArtist, adres, explenation, imgId, coordX, coordY);

                ArrayList<String> recordIdList = (ArrayList<String>) LandMarksDatabase.getInstance(context).getStreetArtRecordID();

                if (recordIdList.contains(recordId)) {
                    LandMarksDatabase.getInstance(context).updateStreetArt(streetArt);
                } else {
                    LandMarksDatabase.getInstance(context).insertStreetArt(streetArt);
                }
            }
        }
    }
}
