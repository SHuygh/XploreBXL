package be.ehb.xplorebxl.Database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import be.ehb.xplorebxl.Database.DAO.ComicDAO;
import be.ehb.xplorebxl.Database.DAO.FavouritesDAO;
import be.ehb.xplorebxl.Database.DAO.MuseumDAO;
import be.ehb.xplorebxl.Database.DAO.StreetArtDAO;
import be.ehb.xplorebxl.Model.Comic;
import be.ehb.xplorebxl.Model.Favourites;
import be.ehb.xplorebxl.Model.Museum;
import be.ehb.xplorebxl.Model.StreetArt;
import be.ehb.xplorebxl.R;

/**
 * Created by TDS-Team on 16/03/2018.
 */

@Database(entities = {Museum.class, StreetArt.class, Comic.class, Favourites.class}, version = 1 ,exportSchema = false)
public abstract class LandMarksDatabase extends RoomDatabase {

    //singleton
    private static LandMarksDatabase instance;

    public static LandMarksDatabase getInstance (Context context){
        if (instance == null){
            instance = Room.databaseBuilder(context, LandMarksDatabase.class, "landMarks.db").allowMainThreadQueries().build();
        }
        return instance;
    }

/**FAVOURITES**/
    public abstract FavouritesDAO getFavouritesDAO();

    public List<String> getFavRecordId(){
        return getFavouritesDAO().getAllRecordID();
    }

    public List<Favourites> getAllFav(){
        return getFavouritesDAO().getAllFavourites();
    }

    public boolean switchFav(Object o){
        if(o instanceof Museum){
            return switchFav((Museum) o);
        }else if(o instanceof StreetArt){
            return switchFav((StreetArt) o);
        }else if(o instanceof Comic){
            return switchFav((Comic) o);
        }
        return false;
    }

    public boolean switchFav(Comic c){
        Favourites f = new Favourites(c);
        return switchFav(f);
    }

    public boolean switchFav(StreetArt s){
        Favourites f = new Favourites(s);
        return switchFav(f);
    }

    public boolean switchFav(Museum m){
        Favourites f = new Favourites(m);
        return switchFav(f);
    }

    private boolean switchFav(Favourites f){
        if(getAllFav().contains(f)){
            deleteFav(f);
            return false;
        }else{
            insertFav(f);
            return true;
        }
    }

    public boolean checkFav(Object o){
        List<String> recordIdList = getFavRecordId();
        String recordId = "";
        if(o instanceof Museum){
            recordId = ((Museum) o).getRecordId();
        }else if(o instanceof StreetArt){
            recordId = ((StreetArt) o).getRecordId();
        }else if(o instanceof Comic){
            recordId = ((Comic) o).getRecordId();
        }

        return recordIdList.contains(recordId);

    }
    private void deleteFav(Favourites f){
        getFavouritesDAO().deleteFav(f);
    }

    private void insertFav(Favourites f){
        getFavouritesDAO().insertFav(f);
    }


    public ArrayList<Object> getAllFavObjects(){
        List<Favourites> favList = getAllFav();
        ArrayList<Object> objectList = new ArrayList<>();

        for(Favourites fav: favList){
            if(fav.getType().equals(Favourites.TYPE_MUSEUM)){
                List<Museum> list = getMuseumDao().getMuseumOnId(fav.getRecordId());
                if(list != null) {
                    Museum m = list.get(0);
                    objectList.add(m);
                }

            }else if(fav.getType().equals(Favourites.TYPE_COMIC)){
                List<Comic> list = getComicDao().getComicOnId(fav.getRecordId());
                if(list != null) {
                    Comic c = list.get(0);
                    objectList.add(c);
                }
            }else if(fav.getType().equals(Favourites.TYPE_STREETART)){
                List<StreetArt> list = getStreetArtDao().getStreetArtOnId(fav.getRecordId());
                if(list != null) {
                    StreetArt s = list.get(0);
                    objectList.add(s);
                }
            }
        }

        return objectList;

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

    public ArrayList<Object> getSortedList(final Location location, int filterId){

        ArrayList<Object> list = new ArrayList<>();

        switch (filterId){
            case R.id.pu_all:
                list.addAll(getAllComics());
                list.addAll(getAllStreetArt());
                list.addAll(getMuseums());
                break;
            case R.id.pu_musea:
                list.addAll(getMuseums());
                break;
            case R.id.pu_comic:
                list.addAll(getAllComics());
                break;
            case R.id.pu_streetart:
                list.addAll(getAllStreetArt());
                break;
            case R.id.pu_fav:
                list.addAll(getAllFavObjects());
                break;
        }


        if(location != null) {

            Collections.sort(list, new Comparator<Object>() {
                @Override
                public int compare(Object o, Object t1) {

                    LatLng latlng_o, latlng_t1;

                    latlng_o = getLatlng(o);
                    latlng_t1 = getLatlng(t1);

                    Location location1 = new Location("location");
                    location1.setLatitude(latlng_o.latitude);
                    location1.setLongitude(latlng_o.longitude);

                    Location loc_t1 = new Location("location_t1");
                    loc_t1.setLatitude(latlng_t1.latitude);
                    loc_t1.setLongitude(latlng_t1.longitude);

                    float distance = location.distanceTo(location1);
                    float distance_t1 = location.distanceTo(loc_t1);

                    float difference = distance - distance_t1;

                    int result;

                    if (difference > 0) { result = 1;
                    } else if (difference < 0) { result = -1;
                    } else {result = 0;}

                    return result;
                }

                private LatLng getLatlng(Object o) {

                    LatLng latLng;

                    //check if object is instanceof from Museum, Comic or streetart and get coordinates
                    if(o instanceof Museum){ latLng = new LatLng(((Museum) o).getCoordX(), ((Museum) o).getCoordY());
                    }else if(o instanceof Comic){ latLng = new LatLng(((Comic) o).getCoordX(), ((Comic) o).getCoordY());
                    }else{ latLng = new LatLng(((StreetArt) o).getCoordX(), ((StreetArt) o).getCoordY()); }

                    return latLng;
                }
            });


        }

        return list;
    }

}

