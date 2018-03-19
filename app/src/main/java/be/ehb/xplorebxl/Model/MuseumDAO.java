package be.ehb.xplorebxl.Model;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * Created by Q on 18-3-2018.
 */
@Dao
public interface MuseumDAO {

    @Query("SELECT * FROM Museum")
    List<Museum> getAllMuseums();

    @Query("SELECT recordId FROM Museum")
    List<String> getMuseumRecordID();

    @Insert
    void insertMuseum(Museum m);

    @Update
    void updateMuseum(Museum m);
}
