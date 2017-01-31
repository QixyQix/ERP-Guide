package qixyqix.com.erpguide;

import java.io.Serializable;

/**
 * Created by QiXiang on 26/01/2017.
 */

public class TopUpLocation implements Serializable{
    private int ID;
    private String title;
    private String description;
    private String address;
    private double lat;
    private double lng;

    public TopUpLocation() {
    }

    public TopUpLocation(int ID, String title, String description, String address, double lat, double lng) {
        this.ID = ID;
        this.title = title;
        this.description = description;
        this.address = address;
        this.lat = lat;
        this.lng = lng;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}
