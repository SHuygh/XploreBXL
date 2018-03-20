package be.ehb.xplorebxl.Model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

/**
 * Created by Q on 18-3-2018.
 */
@Entity
public class StreetArt implements Serializable {

    @PrimaryKey
    @NonNull
    private String recordId;
    private String nameOfArtist;
    private String address;
    private String explanation;
    private String imgUrl;
    private double coordX;
    private double coordY;
    private boolean hasIMG;

    public StreetArt() {
    }

    public StreetArt(@NonNull String recordId, String nameOfArtist, String address, String explanation, String imgID, double coordX, double coordY) {
        this.recordId = recordId;
        this.nameOfArtist = nameOfArtist;
        this.address = address + ", Brussel";
        this.explanation = explanation;
        hasIMG = !TextUtils.isEmpty(imgID);
        this.imgUrl = hasIMG ? "https://opendata.brussel.be/explore/dataset/streetart/files/"+imgUrl+"/300/" : "";
        this.coordX = coordX;
        this.coordY = coordY;
    }

    public boolean isHasIMG() {
        return hasIMG;
    }

    public void setHasIMG(boolean hasIMG) {
        this.hasIMG = hasIMG;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getNameOfArtist() {
        return nameOfArtist;
    }

    public void setNameOfArtist(String nameOfArtist) {
        this.nameOfArtist = nameOfArtist;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
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
        return nameOfArtist;
    }
}
