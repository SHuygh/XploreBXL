package be.ehb.xplorebxl.Database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import java.util.List;

import be.ehb.xplorebxl.Model.Museum;
import be.ehb.xplorebxl.Model.MuseumDAO;
import be.ehb.xplorebxl.Model.StreetArt;
import be.ehb.xplorebxl.Model.StreetArtDAO;

/**
 * Created by TDS-Team on 16/03/2018.
 */

@Database(version = 1, entities = {Museum.class, StreetArt.class})
public abstract class LandMarksDatabase extends RoomDatabase {

    //singleton
    private static LandMarksDatabase instance;

    public static LandMarksDatabase getInstance (Context context){
        if (instance == null){
            instance = Room.databaseBuilder(context, LandMarksDatabase.class, "landMarks.db").allowMainThreadQueries().build();

        }
        return instance;
    }

    public abstract MuseumDAO getMuseumDao();

    public abstract StreetArtDAO getStreetArtDao();

//eigen functies

    public List<Museum> getMuseums(){
        return getMuseumDao().getAllMuseums();
    }




   /* ArrayList<Museum> museums;

    private static final LandMarksDatabase INSTANCE = new LandMarksDatabase();

    private LandMarksDatabase() {
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

    public static LandMarksDatabase getInstance(){
        return INSTANCE;
    }

    */

}
