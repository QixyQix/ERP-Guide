package qixyqix.com.erpguide;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ListViewCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ERPDetail extends AppCompatActivity {
    private DatabaseHandler db;
    private ArrayList<Pricing> pricings;
    private String operationStatus;
    private boolean favourite;
    private int erpID;
    private boolean twelveHr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_erpdetail);

        SharedPreferences preferences = getSharedPreferences("ERPGuide",MODE_PRIVATE);
        twelveHr = preferences.getBoolean("tweleveHr",false);

        db = new DatabaseHandler(this);

        TextView erpTitle = (TextView) findViewById(R.id.txtERPTitle);
        TextView erpStatus = (TextView) findViewById(R.id.txtERPStatus);

        Intent usedIntent = this.getIntent();
        ERPGantry gantry = (ERPGantry) usedIntent.getExtras().getSerializable("gantry");

        favourite = db.isFavourite("ERP",gantry.getID());
        erpID = gantry.getID();

        erpTitle.setText(gantry.getTitle());
        getOperationStatus(gantry.getZone());
        erpStatus.setText(operationStatus);

        setupTabHost();

        ListView carList = (ListView) findViewById(R.id.listCar);
        carList.setAdapter(getAdapter(this,gantry.getZone(),"Passenger Cars"));

        ListView motorCycleList = (ListView) findViewById(R.id.listMotorcycle);
        motorCycleList.setAdapter(getAdapter(this,gantry.getZone(),"Motorcycles"));

        ListView taxiList = (ListView) findViewById(R.id.listTaxi);
        taxiList.setAdapter(getAdapter(this,gantry.getZone(),"Taxis"));

        ListView heavyVehicle = (ListView) findViewById(R.id.listHeavy);
        taxiList.setAdapter(getAdapter(this,gantry.getZone(),"Heavy Goods Vehicles"));

        ListView veryHeavyVehicle = (ListView) findViewById(R.id.listVeryHeavy);
        veryHeavyVehicle.setAdapter(getAdapter(this,gantry.getZone(),"Very Heavy Goods Vehicles"));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_menu,menu);
        MenuItem favouriteItem = (MenuItem) menu.findItem(R.id.menuItemFavourite);
        if(favourite){
            favouriteItem.setIcon(R.drawable.ic_star_white_24dp);
            favouriteItem.setTitle("Remove from favorites");
        }else{
            favouriteItem.setTitle("Add to favorites");
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemSelected = item.getItemId();
        switch (itemSelected){
            case R.id.menuItemFavourite:
                if(favourite){
                    //if it is a favourite, remove from db
                    if(db.deleteFavourite("ERP",erpID)){
                        item.setIcon(R.drawable.ic_star_border_white_24dp);
                        item.setTitle("Add to favorites");
                        favourite = false;
                        Toast.makeText(this,"Successfully removed from favorites",Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(this,"Error removing from favorites",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    //else make it a favorite
                    if(db.insertFavourite("ERP",erpID)){
                        item.setIcon(R.drawable.ic_star_white_24dp);
                        item.setTitle("Remove from favorites");
                        favourite = true;
                        Toast.makeText(this,"Successfully added to favorites",Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(this,"Error adding to favorites",Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.menuItemSettings:
                Intent intent = new Intent(this,Settings.class);
                startActivity(intent);
                break;
        }
        return true;
    }

    public void getOperationStatus(String zone){
        pricings = db.getAllPricingsOfZone(zone);

        operationStatus = "Not in operation";
        boolean inOperation = false;
        for(Pricing pricing : pricings){
            inOperation = pricing.inOperation();
            if(pricing.inOperation()){
                operationStatus = "In operation till "+pricing.getEndTime(twelveHr);
                break;
            }
        }
    }

    public void setupTabHost(){
        TabHost tabHost =(TabHost) findViewById(R.id.tabERPDetail);
        tabHost.setup();

        //Tab cars
        TabHost.TabSpec spec = tabHost.newTabSpec("Tab Car");
        spec.setContent(R.id.tabCar);
        spec.setIndicator("Car");
        tabHost.addTab(spec);

        //Tab Motorcycles
        spec = tabHost.newTabSpec("Tab Motorcycle");
        spec.setContent(R.id.tabCar);
        spec.setIndicator("MotorCycle");
        tabHost.addTab(spec);

        //Tab Taxi
        spec = tabHost.newTabSpec("Taxi");
        spec.setContent(R.id.tabTaxi);
        spec.setIndicator("Taxi");
        tabHost.addTab(spec);

        //Tab Heavy Vehicles
        spec = tabHost.newTabSpec("Tab Heavy Vehicles");
        spec.setContent(R.id.tabHeavy);
        spec.setIndicator("Bus/Heavy Veh");
        tabHost.addTab(spec);

        //Tab Very Heavy Vehicles
        spec = tabHost.newTabSpec("Tab Very Heavy Vehicles");
        spec.setContent(R.id.tabVeryHeavy);
        spec.setIndicator("Very Heavy Veh");
        tabHost.addTab(spec);
    }

    public TimingAdapter getAdapter(Context context,String zone,String vehicleClass){
        ArrayList<Pricing> pricingCar = db.getAllPricingsOfVehicle(zone,vehicleClass);
        TimingAdapter timingAdapter = new TimingAdapter(this,pricingCar);
        return timingAdapter;
    }

    private class TimingAdapter extends ArrayAdapter<Pricing>{

        public TimingAdapter(Context context, List<Pricing> pricings) {
            super(context, 0, pricings);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Pricing pricing = getItem(position);

            if(convertView == null){
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_erp_rate, parent, false);
            }

            TextView txtStartTime = (TextView) convertView.findViewById(R.id.txtPriceItemStart);
            TextView txtEndTime = (TextView) convertView.findViewById(R.id.txtPriceItemEnd);
            TextView txtPricing = (TextView) convertView.findViewById(R.id.txtPriceItemPrice);

            txtStartTime.setText(pricing.getStartTime(twelveHr));
            txtEndTime.setText(pricing.getEndTime(twelveHr));
            txtPricing.setText("$"+pricing.getChargeAmount());

            return convertView;
        }
    }
}


