package be.ehb.xplorebxl.Database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.location.Location;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import be.ehb.xplorebxl.Database.DAO.ComicDAO;
import be.ehb.xplorebxl.Database.DAO.MuseumDAO;
import be.ehb.xplorebxl.Database.DAO.StreetArtDAO;
import be.ehb.xplorebxl.Model.Comic;
import be.ehb.xplorebxl.Model.Museum;
import be.ehb.xplorebxl.Model.StreetArt;

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

    /**If Location is not null, returns the list of items from Room sorted by distance to user,
     * else returns the non sorted list*/
    public List<Museum> getSortedMuseums(final Location location){
        if(location != null) {
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
                    if (difference > 0) { result = 1;
                    } else if (difference < 0) { result = -1;
                    } else {result = 0; }

                    return result;
                }
            });


            return museumList;
        }else{
            return getMuseums();
        }
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

    /**If Location is not null, returns the list of items from Room sorted by distance to user,
     * else returns the non sorted list*/
    public List<StreetArt> getSortedStreetArt(final Location location){
        if(location != null) {
            List<StreetArt> StreetartList = getAllStreetArt();

            Collections.sort(StreetartList, new Comparator<StreetArt>() {
                @Override
                public int compare(StreetArt streetArt, StreetArt t1) {

                    Location loc_streetart = new Location("location");
                    loc_streetart.setLatitude(streetArt.getCoordX());
                    loc_streetart.setLongitude(streetArt.getCoordY());

                    Location loc_t1 = new Location("location_t1");
                    loc_t1.setLatitude(t1.getCoordX());
                    loc_t1.setLongitude(t1.getCoordY());

                    float distance_streetArt = location.distanceTo(loc_streetart);
                    float distance_t1 = location.distanceTo(loc_t1);

                    float difference = distance_streetArt - distance_t1;

                    int result;
                    if (difference > 0) { result = 1;
                    } else if (difference < 0) { result = -1;
                    } else { result = 0;}

                    return result;
                }
            });

            return StreetartList;
        }else{
            return getAllStreetArt();
        }
    }

    public List<StreetArt> getAllStreetArt(){return getStreetArtDao().getAllStreetArt();    }

    public List<String> getStreetArtRecordID(){return getStreetArtDao().getStreetArtRecordID();}

    public List<String> getStreetArtImgUrl(){return getStreetArtDao().getImgUrl();}

    public void insertStreetArt(StreetArt s){getStreetArtDao().insertStreetArt(s);}

    public void updateStreetArt(StreetArt s){getStreetArtDao().updateStreetArt(s);}

/**Comic**/


    public abstract ComicDAO getComicDao();

    public List<Comic> getAllComics(){return getComicDao().getAllComics();  }

    /**If Location is not null, returns the list of items from Room sorted by distance to user,
     * else returns the non sorted list*/
    public List<Comic> getSortedCommic(final Location location){
        if(location != null) {
            List<Comic> ComicsList = getAllComics();

            Collections.sort(ComicsList, new Comparator<Comic>() {
                @Override
                public int compare(Comic comic, Comic t1) {
                    Location loc_comic = new Location("location");
                    loc_comic.setLatitude(comic.getCoordX());
                    loc_comic.setLongitude(comic.getCoordY());

                    Location loc_t1 = new Location("location_t1");
                    loc_t1.setLatitude(t1.getCoordX());
                    loc_t1.setLongitude(t1.getCoordY());

                    float distance_comic = location.distanceTo(loc_comic);
                    float distance_t1 = location.distanceTo(loc_t1);

                    float difference = distance_comic - distance_t1;

                    int result;
                    if (difference > 0) { result = 1;
                    } else if (difference < 0) { result = -1;
                    } else {result = 0;}

                    return result;
                }
            });

            return ComicsList;
        }else{
            return getAllComics();
        }
    }

    public List<String> getComicRecordID(){return getComicDao().getComicRecordID();}

    public List<String> getComicImgUrl(){return getComicDao().getImgUrl();}

    public void insertComic(Comic comic){getComicDao().insertComic(comic);}

    public void updateComic(Comic comic){getComicDao().updateComic(comic);}

}
