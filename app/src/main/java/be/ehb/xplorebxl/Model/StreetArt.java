package be.ehb.xplorebxl.Model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

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
    private String nameOfArt;
    private String photo;
    private String nameOfArtist;
    private String address;
    private String explanation;
    private String imgUrl;
    private double coordX;
    private double coordY;

    public StreetArt() {
    }

    public StreetArt(@NonNull String recordId, String nameOfArt, String photo, String nameOfArtist, String address, String explanation, String imgID, double coordX, double coordY) {
        this.recordId = recordId;
        this.nameOfArt = nameOfArt;
        this.photo = photo;
        this.nameOfArtist = nameOfArtist;
        this.address = address;
        this.explanation = explanation;
        this.imgUrl = "https://opendata.brussel.be/explore/dataset/streetart/files/"+imgUrl+"/300/";
        this.coordX = coordX;
        this.coordY = coordY;
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

    public String getNameOfArt() {
        return nameOfArt;
    }

    public void setNameOfArt(String nameOfArt) {
        this.nameOfArt = nameOfArt;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
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
        return nameOfArt;
    }
}
