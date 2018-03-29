package be.ehb.xplorebxl.Database.DAO;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import be.ehb.xplorebxl.Model.Comic;
import be.ehb.xplorebxl.Model.StreetArt;

/**
 * Created by Q on 18-3-2018.
 */

@Dao
public interface StreetArtDAO {

    @Query("SELECT * FROM StreetArt")
    List<StreetArt>getAllStreetArt();

    @Query("SELECT recordId FROM StreetArt")
    List<String> getStreetArtRecordID();

    @Query("SELECT imgUrl FROM StreetArt")
    List<String> getImgUrl();

    @Query("SELECT * FROM StreetArt WHERE recordId LIKE :id")
    List<StreetArt> getStreetArtOnId(String id);

    @Insert
    void insertStreetArt(StreetArt s);

    @Update
    void updateStreetArt(StreetArt s);

}
