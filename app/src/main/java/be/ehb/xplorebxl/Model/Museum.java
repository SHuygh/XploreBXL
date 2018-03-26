package be.ehb.xplorebxl.Model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

/**
 * Created by TDS-Team on 16/03/2018.
 */

@Entity
public class Museum implements Serializable {

    @PrimaryKey
    @NonNull
    private String recordId;
    private String name;
    private String city;
    private String adres;
    private String url;
    private String tel;
    private String email;
    private double coordX;
    private double coordY;

    @Ignore
    public Museum() {
    }

    public Museum(String recordId, String name, String city, String adres, String url, String tel, String email, double coordX, double coordY) {
        this.recordId = recordId;
        if(name.contains("(")) {
           name = name.split("[(]")[0];
        }
        if(name.contains("/")) {
            name = name.split("[/]")[0];
        }
        this.name = name;
        this.city = city;
        this.adres = adres;
        this.url = url;
        this.tel = tel;
        this.email = email;
        this.coordX = coordX;
        this.coordY = coordY;
    }

    @NonNull
    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAdres() {
        return adres;
    }

    public void setAdres(String adres) {
        this.adres = adres;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public LatLng getCoord(){
        return new LatLng(coordX, coordY);
    }


    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Museum museum = (Museum) o;

        return recordId.equals(museum.recordId);
    }

    @Override
    public int hashCode() {
        return recordId.hashCode();
    }
}
