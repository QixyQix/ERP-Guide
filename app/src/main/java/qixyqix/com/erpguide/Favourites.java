package qixyqix.com.erpguide;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.IntegerRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.PreferenceChangeEvent;

public class Favourites extends AppCompatActivity {

    private DatabaseHandler db;
    private DataBundle data;
    boolean twelveHr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);

        Intent usedIntent = getIntent();
        data = (DataBundle) usedIntent.getExtras().getSerializable("data");
        twelveHr = getSharedPreferences("ERPGuide",MODE_PRIVATE).getBoolean("tweleveHr",false);
        db = new DatabaseHandler(this);

        ArrayList<Integer> favIds = db.getAllFavourites("ERP");
        ArrayList<ERPGantry> favGantries = new ArrayList<ERPGantry>();



        for(int ID : favIds){
            for(ERPGantry gantry : data.getErpGantries()){
                if(gantry.getID() == ID)
                    favGantries.add(gantry);
            }
        }

        favIds = db.getAllFavourites("TopUp");
        ArrayList<TopUpLocation> favTopUp = new ArrayList<TopUpLocation>();
        for(int ID : favIds){
            for(TopUpLocation location : data.getTopUpLocations()){
                if(location.getID() == ID)
                    favTopUp.add(location);
            }
        }

        TabHost tabHost = (TabHost) findViewById(R.id.tabFavourites);
        tabHost.setup();

        TabHost.TabSpec spec = tabHost.newTabSpec("Tab ERP Fav");
        spec.setContent(R.id.tabERPFav);
        spec.setIndicator("ERP Gantries");
        tabHost.addTab(spec);

        spec = tabHost.newTabSpec("Tab Top Up Fav");
        spec.setContent(R.id.tabTopUpFav);
        spec.setIndicator("Top Up Locations");
        tabHost.addTab(spec);

        ListView listFavGantries = (ListView) findViewById(R.id.listERPFav);
        listFavGantries.setAdapter(new GantryAdapter(this,favGantries));

        ListView listFavTopUp = (ListView) findViewById(R.id.listTopUpFav);
        listFavTopUp.setAdapter(new TopUpAdapter(this,favTopUp));

    }

    private class GantryAdapter extends ArrayAdapter<ERPGantry>{

        public GantryAdapter(Context context, List<ERPGantry> objects) {
            super(context, 0, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ERPGantry gantry = getItem(position);

            if(convertView == null){
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent,false);
            }

            boolean inOperation;
            String operationStatus = "Not In Operation";

            TextView txtTitle = (TextView) convertView.findViewById(R.id.txtListTitle);
            TextView txtDesc = (TextView) convertView.findViewById(R.id.txtListDesc);
            ImageView imageView = (ImageView) convertView.findViewById(R.id.imgListIcon);

            for(Pricing pricing : data.getPricings()){
                inOperation = pricing.inOperation();
                if(pricing.getZoneID().equals(gantry.getZone()) && pricing.inOperation()){
                    operationStatus = "In operation till "+pricing.getEndTime(twelveHr);
                    break;
                }
            }

            txtTitle.setText(gantry.getTitle());
            txtDesc.setText(operationStatus);
            imageView.setImageResource(R.drawable.erp_icon);

            return convertView;
        }
    }

    private class TopUpAdapter extends ArrayAdapter<TopUpLocation>{

        public TopUpAdapter(Context context, List<TopUpLocation> objects) {
            super(context, 0, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            TopUpLocation topUpLocation = getItem(position);

            if(convertView == null){
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
            }

            TextView txtTitle = (TextView) convertView.findViewById(R.id.txtListTitle);
            TextView txtDesc = (TextView) convertView.findViewById(R.id.txtListDesc);
            ImageView imageView = (ImageView) convertView.findViewById(R.id.imgListIcon);

            txtTitle.setText(topUpLocation.getTitle());
            txtDesc.setText(topUpLocation.getAddress());
            imageView.setImageResource(R.drawable.top_up_icon);

            return convertView;
        }
    }
}
