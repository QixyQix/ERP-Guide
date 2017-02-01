package qixyqix.com.erpguide;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap erpLocationMap;
    private GoogleMap topUpLocationMap;
    private DataBundle data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Get the data
        Intent usedIntent = this.getIntent();
        DataBundle data = (DataBundle) usedIntent.getExtras().getSerializable("data");
        this.data = data;

        TabHost tabHost = (TabHost) findViewById(R.id.tabHostMainActivity);
        tabHost.setup();

        //Tab 1
        TabHost.TabSpec spec = tabHost.newTabSpec("Tab One");
        spec.setContent(R.id.tab1);
        spec.setIndicator("ERP Locations");
        tabHost.addTab(spec);
        //Tab 2
        spec = tabHost.newTabSpec("Tab Two");
        spec.setContent(R.id.tab2);
        spec.setIndicator("TopUp Locations");
        tabHost.addTab(spec);

        initializeMap();
    }

    @Override
    protected void onResume() {
        super.onResume();
        populateMarkers();
    }

    //Reference for the initialize map code
    //https://www.youtube.com/watch?v=lchyOhPREh4
    private void initializeMap() {
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.fragmentMapERPLocation);
        mapFragment.getMapAsync(this);
        MapFragment mapFragment2 = (MapFragment) getFragmentManager().findFragmentById(R.id.fragmentMapTopUpLocation);
        mapFragment2.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        //Set up ERP Location Map
        if (this.erpLocationMap == null) {
            this.erpLocationMap = googleMap;
            this.erpLocationMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {
                        int ID = Integer.parseInt(marker.getTitle().substring(3, 5));
                        goToPage("ERP",ID);
                }
            });
        }
        else {
            this.topUpLocationMap = googleMap;
            this.topUpLocationMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {
                    int ID = Integer.parseInt(marker.getTitle().substring(0,3));
                    goToPage("TopUp",ID);
                }
            });
        }

        if(null != googleMap){
            googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                @Override
                public View getInfoWindow(Marker marker) {
                    return null;
                }

                @Override
                public View getInfoContents(Marker marker) {
                    View v = getLayoutInflater().inflate(R.layout.list_item,null);

                    ImageView imgIcon = (ImageView) v.findViewById(R.id.imgListIcon);
                    TextView title = (TextView) v.findViewById(R.id.txtListTitle);
                    TextView desc = (TextView) v.findViewById(R.id.txtListDesc);

                    title.setText(marker.getTitle());
                    desc.setText(marker.getSnippet());
                    if(marker.getTitle().substring(0,3).equals("ERP")){
                        imgIcon.setImageResource(R.drawable.erp_icon);
                    }else{
                        imgIcon.setImageResource(R.drawable.top_up_icon);
                    }

                    return v;
                }
            });

        }

        populateMarkers();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id){
            case R.id.menuItemMainSetting:
                Intent intent = new Intent(this,Settings.class);
                startActivity(intent);
                break;
        }
        return true;
    }

    public void zoomToLocation(double lat, double lng, float zoom, GoogleMap map){
        LatLng latLng = new LatLng(lat,lng);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng,zoom);
        map.moveCamera(cameraUpdate);

    }

    public void populateMarkers(){
        SharedPreferences preferences = getSharedPreferences("ERPGuide",MODE_PRIVATE);
        boolean twelveHr = preferences.getBoolean("tweleveHr",false);

        if(null != erpLocationMap && null != topUpLocationMap) {
            //Clear existing markers
            erpLocationMap.clear();
            topUpLocationMap.clear();
            //Zoom to marina bay area
            zoomToLocation(1.287268, 103.855933,12,erpLocationMap);
            zoomToLocation(1.287268, 103.855933,12,topUpLocationMap);

            //Add markers for gantries
            for(ERPGantry gantry : data.getErpGantries()){
                String operationStatus = "Not in operation";
                boolean inOperation = false;
                for(Pricing pricing : data.getPricings()){
                    inOperation = pricing.inOperation();
                    if(pricing.getZoneID().equals(gantry.getZone()) && pricing.inOperation()){
                        operationStatus = "In operation till "+pricing.getEndTime(twelveHr);
                        break;
                    }
                }

                if(inOperation){
                    MarkerOptions marker = new MarkerOptions()
                            .title("ERP" + gantry.getID() + " "+gantry.getTitle())
                            .snippet(operationStatus)
                            .position(new LatLng(gantry.getLat(),gantry.getLng()))
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                    erpLocationMap.addMarker(marker);
                }else {
                    MarkerOptions marker = new MarkerOptions()
                            .title("ERP" + gantry.getID() + " " + gantry.getTitle())
                            .snippet(operationStatus)
                            .position(new LatLng(gantry.getLat(), gantry.getLng()));
                    erpLocationMap.addMarker(marker);
                }
            }

            //Add markers for topup locations
            for(TopUpLocation location : data.getTopUpLocations()){
                MarkerOptions marker = new MarkerOptions()
                        .title(location.getID()+" "+location.getTitle())
                        .snippet(location.getAddress())
                        .position(new LatLng(location.getLat(),location.getLng()));
                topUpLocationMap.addMarker(marker);
            }
        }
    }

    public void goToPage(String type, int id){
        switch(type) {
            case "ERP":
                for (ERPGantry gantry : data.getErpGantries()) {
                    if (gantry.getID() == id) {
                        Intent intent = new Intent(this, ERPDetail.class);
                        Bundle b = new Bundle();
                        b.putSerializable("gantry", gantry);
                        intent.putExtras(b);

                        startActivity(intent);
                    }
                }
                break;
            case "TopUp":
                for(TopUpLocation location : data.getTopUpLocations()){
                    if(location.getID() == id){
                        Intent intent = new Intent(this,TopUpDetail.class);
                        Bundle b = new Bundle();
                        b.putSerializable("location",location);
                        intent.putExtras(b);

                        startActivity(intent);
                    }
                }
                break;
        }
    }

    public void btnClicked(View v){
        switch (v.getId()){
            case R.id.imgBtnFavourites:
                Intent intent = new Intent(this,Favourites.class);
                Bundle b = new Bundle();
                b.putSerializable("data",data);
                intent.putExtras(b);
                startActivity(intent);
                break;
        }
    }
}
