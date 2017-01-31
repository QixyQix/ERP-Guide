package qixyqix.com.erpguide;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by QiXiang on 31/01/2017.
 */

public class DataBundle implements Serializable{
    private ArrayList<ERPGantry> erpGantries;
    private ArrayList<TopUpLocation> topUpLocations;
    private ArrayList<Pricing> pricings;

    public DataBundle(ArrayList<ERPGantry> erpGantries, ArrayList<TopUpLocation> topUpLocations, ArrayList<Pricing> pricings) {
        this.erpGantries = erpGantries;
        this.topUpLocations = topUpLocations;
        this.pricings = pricings;
    }

    public ArrayList<ERPGantry> getErpGantries() {
        return erpGantries;
    }

    public void setErpGantries(ArrayList<ERPGantry> erpGantries) {
        this.erpGantries = erpGantries;
    }

    public ArrayList<TopUpLocation> getTopUpLocations() {
        return topUpLocations;
    }

    public void setTopUpLocations(ArrayList<TopUpLocation> topUpLocations) {
        this.topUpLocations = topUpLocations;
    }

    public ArrayList<Pricing> getPricings() {
        return pricings;
    }

    public void setPricings(ArrayList<Pricing> pricings) {
        this.pricings = pricings;
    }
}
