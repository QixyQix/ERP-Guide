package qixyqix.com.erpguide;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by QiXiang on 30/01/2017.
 */

public class DatabaseHandler extends SQLiteOpenHelper{

    private static final String TABLE_ERPRATES = "tblERPRates";
    private static final String TABLE_FAVOURITES = "tblFavourites";

    //ERPRates
    private static final String KEY_RATE_ID = "id";
    private static final String KEY_VEHICLE_TYPE = "vehicleType";
    private static final String KEY_DAY_TYPE = "dayType";
    private static final String KEY_START_TIME = "startTime";
    private static final String KEY_END_TIME = "endTime";
    private static final String KEY_ZONE_ID = "zoneID";
    private static final String KEY_CHARGE_AMT = "chargeAmt";

    //Favourites
    private static final String KEY_FAVOURITE_ID = "id";
    private static final String KEY_TYPE = "type";
    private static final String KEY_ID = "ERPId";

    private static String DATABASE_NAME = "erpManager";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Step 4: Define the table
        String CREATE_ERPRATES_TABLE = "CREATE TABLE " + TABLE_ERPRATES + " (" +
                KEY_RATE_ID + " INTEGER PRIMARY KEY," +
                KEY_VEHICLE_TYPE + " TEXT," +
                KEY_DAY_TYPE + " TEXT," +
                KEY_START_TIME + " TEXT," +
                KEY_END_TIME + " TEXT," +
                KEY_ZONE_ID + " TEXT," +
                KEY_CHARGE_AMT + " DOUBLE" + ")";

        db.execSQL(CREATE_ERPRATES_TABLE);

        String CREATE_FAVOURITES_TABLE = "CREATE TABLE "+TABLE_FAVOURITES+" (" +
                KEY_FAVOURITE_ID+" INTEGER PRIMARY KEY," +
                KEY_TYPE+" TEXT,"+
                KEY_ID+" INTEGER )";

        db.execSQL(CREATE_FAVOURITES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    //Methods to POST
    public void insertPrice(Pricing pricing){
        //Get the db
        SQLiteDatabase db = this.getWritableDatabase();
        //Get the values
        ContentValues values = new ContentValues();
        //Populate the values
        values.put(KEY_VEHICLE_TYPE,pricing.getVehicleType());
        values.put(KEY_DAY_TYPE,pricing.getDayType());
        values.put(KEY_START_TIME,pricing.getStartTime(false));
        values.put(KEY_END_TIME,pricing.getEndTime(false));
        values.put(KEY_ZONE_ID,pricing.getZoneID());
        values.put(KEY_CHARGE_AMT,pricing.getChargeAmount());
        //Insert
        db.insert(TABLE_ERPRATES,null,values);
        db.close();
    }



    public boolean insertFavourite(String type,int favID){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_TYPE,type);
        values.put(KEY_ID,favID);

        long rowsAffected = db.insert(TABLE_FAVOURITES,null,values);
        db.close();

        if(rowsAffected > 0){
            return true;
        }else{
            return false;
        }
    }


    //Methods to GET
    public ArrayList<Pricing> getAllPricings(){
        ArrayList<Pricing> pricingList = new ArrayList<Pricing>();

        String selectString = "SELECT * FROM "+TABLE_ERPRATES;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(selectString,null);

        if(cursor.moveToFirst()){
            do{
                Pricing pricing = new Pricing();
                pricing.setVehicleType(cursor.getString(1));
                pricing.setDayType(cursor.getString(2));
                pricing.setStartTime(cursor.getString(3));
                pricing.setEndTime(cursor.getString(4));
                pricing.setZoneID(cursor.getString(5));
                pricing.setChargeAmount(Double.parseDouble(cursor.getString(6)));

                pricingList.add(pricing);
            }while(cursor.moveToNext());
        }
        return pricingList;
    }

    public ArrayList<Pricing> getAllPricingsOfZone(String zoneID){
        ArrayList<Pricing> pricingList = new ArrayList<Pricing>();

        String selectString = "SELECT * FROM "+TABLE_ERPRATES+" WHERE "+KEY_ZONE_ID+" LIKE '"+zoneID+"'";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(selectString,null);

        if(cursor.moveToFirst()){
            do{
                Pricing pricing = new Pricing();
                pricing.setVehicleType(cursor.getString(1));
                pricing.setDayType(cursor.getString(2));
                pricing.setStartTime(cursor.getString(3));
                pricing.setEndTime(cursor.getString(4));
                pricing.setZoneID(cursor.getString(5));
                pricing.setChargeAmount(Double.parseDouble(cursor.getString(6)));

                pricingList.add(pricing);
            }while(cursor.moveToNext());
        }
        return pricingList;
    }

    public ArrayList<Pricing> getAllPricingsOfVehicle(String zoneID,String vehicleClass){
        ArrayList<Pricing> pricingList = new ArrayList<Pricing>();

        String selectString = "SELECT * FROM "+TABLE_ERPRATES+" WHERE "+KEY_ZONE_ID+" LIKE '"+zoneID+"' " +
                "AND "+KEY_VEHICLE_TYPE+" LIKE '"+vehicleClass+"'";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(selectString,null);

        if(cursor.moveToFirst()){
            do{
                Pricing pricing = new Pricing();
                pricing.setVehicleType(cursor.getString(1));
                pricing.setDayType(cursor.getString(2));
                pricing.setStartTime(cursor.getString(3));
                pricing.setEndTime(cursor.getString(4));
                pricing.setZoneID(cursor.getString(5));
                pricing.setChargeAmount(Double.parseDouble(cursor.getString(6)));

                pricingList.add(pricing);
            }while(cursor.moveToNext());
        }
        return pricingList;
    }

    public ArrayList<Integer> getAllFavourites(String type){
        ArrayList<Integer> favouriteIDs = new ArrayList<Integer>();

        String selectString = "SELECT * FROM "+TABLE_FAVOURITES+" WHERE "+KEY_TYPE+" LIKE '"+type+"'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectString,null);

        if(cursor.moveToFirst()){
            do{
                favouriteIDs.add(cursor.getInt(2));
            }while (cursor.moveToNext());
        }

        return favouriteIDs;
    }

    public boolean isFavourite(String type, int id){
        String selectString = "SELECT * FROM "+TABLE_FAVOURITES+" WHERE "+KEY_TYPE+" LIKE '"+type+
                "' AND "+KEY_ID+" = "+id;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectString,null);

        if(cursor.moveToFirst()){
            return true;
        }else{
            return false;
        }
    }

    //Methods to UPDATE
    public int updatePricing(Pricing pricing){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_CHARGE_AMT,pricing.getChargeAmount());

        String where = KEY_VEHICLE_TYPE+" = ? AND "+
                KEY_DAY_TYPE+" = ? AND "+
                KEY_START_TIME+" = ? AND "+
                KEY_END_TIME+" = ? AND "+
                KEY_ZONE_ID+" = ?";

        return db.update(TABLE_ERPRATES,values,where,new String[]{pricing.getVehicleType(),
        pricing.getDayType(),
        pricing.getStartTime(false),
        pricing.getEndTime(false),
        pricing.getZoneID()} );
    }

    public int updatePricings(ArrayList<Pricing> pricings){

        SQLiteDatabase db = this.getWritableDatabase();

        int updated = 0;

        for(Pricing pricing : pricings) {
            ContentValues values = new ContentValues();

            values.put(KEY_CHARGE_AMT, pricing.getChargeAmount());

            String where = KEY_VEHICLE_TYPE + " = ? AND " +
                    KEY_DAY_TYPE + " = ? AND " +
                    KEY_START_TIME + " = ? AND " +
                    KEY_END_TIME + " = ? AND " +
                    KEY_ZONE_ID + " = ?";

            updated += db.update(TABLE_ERPRATES, values, where, new String[]{pricing.getVehicleType(),
                    pricing.getDayType(),
                    pricing.getStartTime(false),
                    pricing.getEndTime(false),
                    pricing.getZoneID()});
        }
        return updated;
    }

    //Methods to DELETE
    public boolean deleteFavourite(String type, int ID){
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.delete(TABLE_FAVOURITES,KEY_TYPE + "= ? AND "+KEY_ID+" = ?",
                new String[]{type,String.valueOf(ID)});
        db.close();//Assume no more operation

        if(rowsAffected > 0){
            return true;
        }else{
            return false;
        }
    }
}
