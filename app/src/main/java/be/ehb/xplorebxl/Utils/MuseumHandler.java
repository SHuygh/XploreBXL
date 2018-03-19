package be.ehb.xplorebxl.Utils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import be.ehb.xplorebxl.Database.LandMarksDatabase;
import be.ehb.xplorebxl.Model.Museum;

/**
 * Created by huyghstijn on 19/03/2018.
 */

public class MuseumHandler extends Handler {

    private final String
            KEY_RECORDS = "records",
                KEY_RECORDID = "recordid",
                KEY_FIELDS = "fields",
                    KEY_NAME = "naam_van_het_museum",
                    KEY_CITY = "gemeente",
                    KEY_ADRES = "adres",
                    KEY_URL = "site_web_website",
                    KEY_TEL = "telephone_telefoon",
                    KEY_EMAIL = "e_mail",
                    KEY_COORDX = "latitude_breedtegraad",
                    KEY_COORDY = "longitude_lengtegraad";

    private Context context;

    public MuseumHandler(Context context) {
        this.context = context;
    }

    @Override
    public void handleMessage(Message msg) {
        String museumData = msg.getData().getString("json_data");

        try {
            JSONObject jsonObject = new JSONObject(museumData);
            JSONArray records = jsonObject.getJSONArray(KEY_RECORDS);
            int json_length = records.length();

            for(int i = 0; i < json_length; i++){
                JSONObject JSON_Museum = records.getJSONObject(i);

                String recordId = JSON_Museum.getString(KEY_RECORDID);
                JSONObject fields = JSON_Museum.getJSONObject(KEY_FIELDS);
                    String name = fields.getString(KEY_NAME);
                    String city = fields.getString(KEY_CITY);
                    String adres = fields.getString(KEY_ADRES);
                    String url = fields.getString(KEY_URL);
                    String tel = fields.getString(KEY_TEL);
                    String email = fields.getString(KEY_EMAIL);
                    double coordX = fields.getDouble(KEY_COORDX);
                    double coordY = fields.getDouble(KEY_COORDY);

                Museum museum = new Museum(recordId, name, city, adres, url, tel, email, coordX, coordY);

                ArrayList<String> recordIdList = (ArrayList<String>) LandMarksDatabase.getInstance(context).getMuseumDao().getMuseamRecordID();

                if(recordIdList.contains(recordId)){
                    LandMarksDatabase.getInstance(context).updateMuseum(museum);
                }else{
                    LandMarksDatabase.getInstance(context).insertMuseum(museum);
                }
            }



        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}
