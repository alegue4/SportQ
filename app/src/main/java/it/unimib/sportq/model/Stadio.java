package it.unimib.sportq.model;

import java.io.Serializable;


public class Stadio implements Serializable {
    private String id_stadium;
    private String stadiumName;
    private String address;
    private double latitude;
    private double longitude;
    private boolean isFavorite;
    private String fotoStadio;
    public Stadio() {
    }


    public Stadio(String id_stadium, String stadiumName, String address, double latitude, double longitude, String fotoStadio) {
        this.id_stadium = id_stadium;
        this.stadiumName = stadiumName;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.fotoStadio = fotoStadio;
        this.isFavorite = false;
    }

    public Stadio(String stadiumName, double latitude, double longitude) {
        this.stadiumName = stadiumName;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Stadio(String id_stadium, String stadiumName) {
        this.id_stadium = id_stadium;
        this.stadiumName = stadiumName;
    }

    public Stadio(String id_stadium) {
        this.id_stadium = id_stadium;
    }

    public String getId_stadium() {
        return id_stadium;
    }

    public void setId_stadium(String id_stadium) {this.id_stadium = id_stadium;}

    public String getStadiumName() {
        return stadiumName;
    }

    public void setStadiumName(String stadiumName) {
        this.stadiumName = stadiumName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getFotoStadio() {
        return fotoStadio;
    }

    public void setFotoStadio(String fotoStadio) {
        this.fotoStadio = fotoStadio;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    @Override
    public String toString() {
        return "Stadio{" +
                "id_stadium='" + id_stadium + '\'' +
                ", stadiumName='" + stadiumName + '\'' +
                ", address='" + address + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", isFavorite=" + isFavorite +
                ", fotoStadio=" + fotoStadio +
                '}';
    }
}

