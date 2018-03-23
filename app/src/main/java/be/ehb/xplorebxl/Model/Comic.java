package be.ehb.xplorebxl.Model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;


import java.io.Serializable;

/**
 * Created by Millmaster on 20/03/2018.
 */
@Entity
public class Comic implements Serializable {

    @PrimaryKey
    @NonNull
    private String recordId;
    private String nameOfIllustrator;
    private String personnage;
    private String imgUrl;
    private double coordX;
    private double coordY;
    private boolean hasIMG;


    public Comic() {
    }

    public Comic(@NonNull String recordId, String nameOfIllustrator, String personnage, String imgID, double coordX, double coordY) {
        this.recordId = recordId;
        this.nameOfIllustrator = nameOfIllustrator;
        this.personnage = personnage;
        hasIMG = !TextUtils.isEmpty(imgID);
        this.imgUrl = hasIMG ? "https://opendata.brussel.be/explore/dataset/comic-book-route/files/"+imgID+"/300/" : "";
        this.coordX = coordX;
        this.coordY = coordY;
    }

    public boolean isHasIMG() {
        return hasIMG;
    }

    public void setHasIMG(boolean hasIMG) {
        this.hasIMG = hasIMG;
    }

    @NonNull
    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(@NonNull String recordId) {
        this.recordId = recordId;
    }

    public String getNameOfIllustrator() {
        return nameOfIllustrator;
    }

    public void setNameOfIllustrator(String nameOfIllustrator) {
        this.nameOfIllustrator = nameOfIllustrator;
    }

    public String getPersonnage() {
        return personnage;
    }

    public void setPersonnage(String personnage) {
        this.personnage = personnage;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public double getCoordX() {
        return coordX;
    }

    public void setCoordX(double coordX) {
        this.coordX = coordX;
    }

    public double getCoordY() {
        return coordY;
    }

    public void setCoordY(double coordY) {
        this.coordY = coordY;
    }

    public LatLng getCoord(){return new LatLng(coordX, coordY);}



    @Override
    public String toString() {
        return personnage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Comic comic = (Comic) o;

        return recordId.equals(comic.recordId);
    }

    @Override
    public int hashCode() {
        return recordId.hashCode();
    }
}
