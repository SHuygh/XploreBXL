package be.ehb.xplorebxl.Database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.location.Location;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import be.ehb.xplorebxl.Model.Comic;
import be.ehb.xplorebxl.Model.ComicDAO;
import be.ehb.xplorebxl.Model.Museum;
import be.ehb.xplorebxl.Model.MuseumDAO;
import be.ehb.xplorebxl.Model.StreetArt;
import be.ehb.xplorebxl.Model.StreetArtDAO;

/**
 * Created by TDS-Team on 16/03/2018.
 */

@Database(entities = {Museum.class, StreetArt.class, Comic.class}, version = 1 ,exportSchema = false)
public abstract class LandMarksDatabase extends RoomDatabase {

    //singleton
    private static LandMarksDatabase instance;

    public static LandMarksDatabase getInstance (Context context){
        if (instance == null){
            instance = Room.databaseBuilder(context, LandMarksDatabase.class, "landMarks.db").allowMainThreadQueries().build();

        }
        return instance;
    }


/**MUSEUM**/

    public abstract MuseumDAO getMuseumDao();

    public List<Museum> getMuseums(){
        return getMuseumDao().getAllMuseums();
    }

    public List<Museum> getSortedMuseums(final Location location){
        List<Museum> museumList = getMuseums();

        Collections.sort(museumList, new Comparator<Museum>() {
            @Override
            public int compare(Museum museum, Museum t1) {
                Location loc_museum = new Location("location");
                    loc_museum.setLatitude(museum.getCoordX());
                    loc_museum.setLongitude(museum.getCoordY());
                Location loc_t1 = new Location("location_t1");
                    loc_t1.setLatitude(t1.getCoordX());
                    loc_t1.setLongitude(t1.getCoordY());
                float distance_museum = location.distanceTo(loc_museum);
                float distance_t1 = location.distanceTo(loc_t1);
                float difference = distance_museum - distance_t1;
                int result;
                if(difference>0){
                    result = 1;
                }else if(difference<0){
                    result = -1;
                }else{
                    result = 0;
                }
                return result;
            }
        });


        return museumList;
    }

    public List<String> getMuseumRecordID(){
        return  getMuseumDao().getMuseumRecordID();
    }

    public void insertMuseum(Museum m){
        getMuseumDao().insertMuseum(m);
    }

    public void updateMuseum(Museum m){
        getMuseumDao().updateMuseum(m);
    }

/**StreetArt**/

    public abstract StreetArtDAO getStreetArtDao();

    public List<StreetArt> getAllStreetArt(){return getStreetArtDao().getAllStreetArt();    }

    public List<String> getStreetArtRecordID(){return getStreetArtDao().getStreetArtRecordID();}

    public List<String> getStreetArtImgUrl(){return getStreetArtDao().getImgUrl();};

    public void insertStreetArt(StreetArt s){getStreetArtDao().insertStreetArt(s);}

    public void updateStreetArt(StreetArt s){getStreetArtDao().updateStreetArt(s);}

/**Comic**/


    public abstract ComicDAO getComicDao();

    public List<Comic> getAllComics(){return getComicDao().getAllComics();  }

    public List<String> getComicRecordID(){return getComicDao().getComicRecordID();}

    public List<String> getComicImgUrl(){return getComicDao().getImgUrl();};

    public void insertComic(Comic comic){getComicDao().insertComic(comic);}

    public void updateComic(Comic comic){getComicDao().updateComic(comic);}




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
