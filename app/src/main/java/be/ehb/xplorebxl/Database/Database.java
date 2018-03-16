package be.ehb.xplorebxl.Database;

import java.util.ArrayList;

import be.ehb.xplorebxl.Model.Museum;

/**
 * Created by TDS-Team on 16/03/2018.
 */


public class Database {

    ArrayList<Museum> museums;

    private static final Database INSTANCE = new Database();

    private Database() {
        museums = new ArrayList<>();

        museums.add(new Museum("9e20aaa554aabb6b8da0f37ff1b45e125849c349",
                "Archief en Museum voor het Vlaams Leven te Brussel",
                "Brussel", "Arduinkaai 28", "http://www.amvb.be",
                "telephone_telefoon", "info@amvb.be",
                50.8552703, 4.34897509));

        museums.add(new Museum("fbf48a0216f8e9814629179e06b3518bf4db6152",
                "Art & marges museum",
                "Brussel", "Arduinkaai 28", "http://www.amvb.be",
                "telephone_telefoon", "info@amvb.be",
                50.8347929,4.34647749));

        museums.add(new Museum("efc644b8cb0c1b4d003f62d12afb1e602146baf8",
                "Belgisch Museum van de Vrijmetselarij",
                "Brussel", "Arduinkaai 28", "http://www.amvb.be",
                "telephone_telefoon", "info@amvb.be",
                50.8529947,4.3515889));

        museums.add(new Museum("c1b3d92b8b7a92214ecd32834c41579461707e51",
                "BELvue museum",
                "Brussel", "Arduinkaai 28", "http://www.amvb.be",
                "telephone_telefoon", "info@amvb.be",
                50.84261069,4.3606882));

    }

    public ArrayList<Museum> getMuseums(){
        return museums;
    }

    public static Database getInstance(){
        return INSTANCE;
    }

}
