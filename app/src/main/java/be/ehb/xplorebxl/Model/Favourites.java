package be.ehb.xplorebxl.Model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Created by huyghstijn on 29/03/2018.
 */
@Entity
public class Favourites {

    public final static String TYPE_MUSEUM = "museum", TYPE_STREETART = "streetart", TYPE_COMIC = "comic";

    @PrimaryKey
    @NonNull
    private String recordId;
    private String type;

    public Favourites() {
    }

    public Favourites(StreetArt streetArt) {
        this.recordId = streetArt.getRecordId();
        this.type = TYPE_STREETART;
    }

    public Favourites(Comic comic) {
        this.recordId = comic.getRecordId();
        this.type = TYPE_COMIC;
    }

    public Favourites(Museum museum) {
        this.recordId = museum.getRecordId();
        this.type = TYPE_MUSEUM;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Favourites that = (Favourites) o;

        return recordId.equals(that.recordId);
    }

    @Override
    public int hashCode() {
        return recordId.hashCode();
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


}
