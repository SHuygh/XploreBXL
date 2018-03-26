package be.ehb.xplorebxl.Database.DAO;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import be.ehb.xplorebxl.Model.Comic;

/**
 * Created by Millmaster on 20/03/2018.
 */

@Dao
public interface ComicDAO {

    @Query("SELECT * FROM Comic")
    List<Comic> getAllComics();

    @Query("SELECT recordId FROM Comic")
    List<String> getComicRecordID();

    @Query("SELECT imgUrl FROM Comic")
    List<String> getImgUrl();

    @Insert
    void insertComic(Comic comic);

    @Update
    void updateComic(Comic comic);


}
