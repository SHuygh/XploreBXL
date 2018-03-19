package be.ehb.xplorebxl.Model;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by Q on 18-3-2018.
 */

@Dao
public interface StreetArtDAO {

    @Query("SELECT * FROM StreetArt")
    List<StreetArt>getAllStreetArt();
}
