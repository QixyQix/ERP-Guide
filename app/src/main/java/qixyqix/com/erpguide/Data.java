package qixyqix.com.erpguide;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.nio.Buffer;
import java.security.PrivateKey;
import java.util.*;

/**
 * Created by QiXiang on 26/01/2017.
 */

public class Data {
    private DatabaseHandler db;
    private ArrayList<ERPGantry> erpGantries;
    private ArrayList<TopUpLocation> topUpLocations;
    private ArrayList<Pricing> pricings;
    Context context;



    public Data(Context context){
        this.context = context;
        db = new DatabaseHandler(context);
        erpGantries = new ArrayList<ERPGantry>();
        topUpLocations = new ArrayList<TopUpLocation>();
        pricings = new ArrayList<Pricing>();

        readData();
    }

    public void readData(){
        //Code to read the gantry and topup location data
        try {

            JSONArray jsonArray = readJSONFile("ERPLocations.json");
            if(jsonArray != null) {
                for (int i = 0; i <jsonArray.length(); i++) {
                    JSONObject aJSONObj = jsonArray.getJSONObject(i);
                    erpGantries.add(new ERPGantry(aJSONObj.getInt("ERPID"), aJSONObj.getString("Title"),
                            aJSONObj.getDouble("Lat"), aJSONObj.getDouble("Lng"), aJSONObj.getString("ZoneID")));
                }
            }

            //TODO: Uncomment this when the data for top up locations is here.
//            jsonArray = readJSONFile("TopUpLocations.json");
//            if(jsonArray != null){
//                for(int i = 0 ; i<jsonArray.length();i++){
//                    JSONObject aJSONObj = jsonArray.getJSONObject(i);
//                    topUpLocations.add(new TopUpLocation(aJSONObj.getInt("ID"),aJSONObj.getString("Title"),
//                            aJSONObj.getString("Description"),aJSONObj.getString("Address"),
//                            aJSONObj.getDouble("Lat"),aJSONObj.getDouble("Lng")));
//                }
//            }
        }catch(Exception e){
            e.printStackTrace();
        }

        //Code to update/create pricing records in database
        ArrayList<Pricing> pricings = GetDatamallData();

        if(null!=pricings){
            for(Pricing pricing : pricings){
                if(pricing.getChargeAmount() != 0.0) {
                    if (db.updatePricing(pricing) == 0) {
                        db.insertPrice(pricing);
                    }
                }
            }
        }

        this.pricings = db.getAllPricings();
    }

    public ArrayList<Pricing> GetDatamallData(){
        DatamallAPIHandler datamallAPIHandler = new DatamallAPIHandler();

        JSONObject apiData = datamallAPIHandler.callAPI();

        ArrayList<Pricing> datamallPricing = new ArrayList<Pricing>();

        try {
            //Get the pricing data
            JSONArray valuesArray = apiData.getJSONArray("value");

            for(int i = 0; i < valuesArray.length(); i++){
                JSONObject oneValue = valuesArray.getJSONObject(i);

                String vehicleClass = oneValue.getString("VehicleType");
                String dayType = oneValue.getString("DayType");
                String startTime = oneValue.getString("StartTime");
                String endTime = oneValue.getString("EndTime");
                String zoneID = oneValue.getString("ZoneID");
                double chargeAmount = oneValue.getDouble("ChargeAmount");
                String effectiveDate = oneValue.getString("EffectiveDate");

                Pricing pricing;

                if(vehicleClass.contains("Motorcycles")){
                    pricing = new Pricing(dayType,startTime,endTime,zoneID,chargeAmount,effectiveDate);
                    pricing.setVehicleType("Motorcycles");
                    datamallPricing.add(pricing);
                }
                if(vehicleClass.contains("Light Goods Vehicles")){
                    pricing = new Pricing(dayType,startTime,endTime,zoneID,chargeAmount,effectiveDate);
                    pricing.setVehicleType("Light Goods Vehicles");
                    datamallPricing.add(pricing);
                }
                if(vehicleClass.contains("Heavy Goods Vehicles")){
                    pricing = new Pricing(dayType,startTime,endTime,zoneID,chargeAmount,effectiveDate);
                    pricing.setVehicleType("Light Goods Vehicles");
                    datamallPricing.add(pricing);
                }
                if(vehicleClass.contains("Very Heavy Goods Vehicles")){
                    pricing = new Pricing(dayType,startTime,endTime,zoneID,chargeAmount,effectiveDate);
                    pricing.setVehicleType("Very Heavy Goods Vehicles");
                    datamallPricing.add(pricing);
                }
                if(vehicleClass.contains("Taxis")){
                    pricing = new Pricing(dayType,startTime,endTime,zoneID,chargeAmount,effectiveDate);
                    pricing.setVehicleType("Taxis");
                    datamallPricing.add(pricing);
                }
            }
            return datamallPricing;
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public JSONArray readJSONFile(String fileName){
        JSONArray jsonArray = null;
        try {
            InputStream inputStream = context.getAssets().open(fileName);

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String result = "";
            String line = "";
            while ((line = reader.readLine()) != null) {
                result += line;
            }

            jsonArray = new JSONArray(result);
        }catch(Exception e){
            e.printStackTrace();
        }
        return jsonArray;
    }

    public DataBundle getDataBundle(){
        return new DataBundle(erpGantries,topUpLocations,pricings);
    }
}
