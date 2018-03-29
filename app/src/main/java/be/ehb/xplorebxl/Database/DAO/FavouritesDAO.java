package be.ehb.xplorebxl.Database.DAO;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import be.ehb.xplorebxl.Model.Favourites;

/**
 * Created by huyghstijn on 29/03/2018.
 */

@Dao
public interface FavouritesDAO {

    @Query("SELECT recordId FROM Favourites")
    List<String> getAllRecordID();

    @Query("SELECT * FROM Favourites")
    List<Favourites> getAllFavourites();

    @Insert
    void insertFav(Favourites f);

    @Delete
    void deleteFav(Favourites f);
}
