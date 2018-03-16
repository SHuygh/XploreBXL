package be.ehb.xplorebxl.Model;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

/**
 * Created by TDS-Team on 16/03/2018.
 */

public class Museum implements Serializable {

    private String recordId, name, city, adres, url, tel, eMail;
    private double coordX, coordY;


    public Museum() {
    }

    public Museum(String recordId, String name, String city, String adres, String url, String tel, String eMail, double coordX, double coordY) {
        this.recordId = recordId;
        this.name = name;
        this.city = city;
        this.adres = adres;
        this.url = url;
        this.tel = tel;
        this.eMail = eMail;
        this.coordX = coordX;
        this.coordY = coordY;
    }

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

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
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

}
