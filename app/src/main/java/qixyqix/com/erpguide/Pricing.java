package qixyqix.com.erpguide;

import java.io.Serializable;

/**
 * Created by QiXiang on 26/01/2017.
 */

public class Pricing implements Serializable{
    private String vehicleType;
    private String dayType;
    private String startTime;
    private String endTime;
    private String zoneID;
    private double chargeAmount;
    private String effectiveDate;

    public Pricing(){

    }

    public Pricing(String vehicleType, String dayType, String startTime, String endTime, String zoneID, double chargeAmount, String effectiveDate) {
        this.vehicleType = vehicleType;
        this.dayType = dayType;
        this.startTime = startTime;
        this.endTime = endTime;
        this.zoneID = zoneID;
        this.chargeAmount = chargeAmount;
        this.effectiveDate = effectiveDate;
    }

    public Pricing(String dayType, String startTime, String endTime, String zoneID, double chargeAmount, String effectiveDate) {
        this.dayType = dayType;
        this.startTime = startTime;
        this.endTime = endTime;
        this.zoneID = zoneID;
        this.chargeAmount = chargeAmount;
        this.effectiveDate = effectiveDate;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getDayType() {
        return dayType;
    }

    public void setDayType(String dayType) {
        this.dayType = dayType;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getZoneID() {
        return zoneID;
    }

    public void setZoneID(String zoneID) {
        this.zoneID = zoneID;
    }

    public double getChargeAmount() {
        return chargeAmount;
    }

    public void setChargeAmount(double chargeAmount) {
        this.chargeAmount = chargeAmount;
    }

    public String getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(String effectiveDate) {
        this.effectiveDate = effectiveDate;
    }
}
