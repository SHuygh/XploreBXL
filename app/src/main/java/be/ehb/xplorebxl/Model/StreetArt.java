package be.ehb.xplorebxl.Model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

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
    private double coordX;
    private double coordY;

    public StreetArt() {
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

    @Override
    public String toString() {
        return nameOfArt;
    }
}
