package qixyqix.com.erpguide;

import java.io.Serializable;

/**
 * Created by QiXiang on 19/11/2016.
 */

public class ERPGantry implements Serializable{
    private int ID;
    private String title;
    private double lat;
    private double lng;
    private String zone;

    public ERPGantry() {
    }

    public ERPGantry(int ID, String title, double lat, double lng, String zone) {
        this.ID = ID;
        this.title = title;
        this.lat = lat;
        this.lng = lng;
        this.zone = zone;
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

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }
}
