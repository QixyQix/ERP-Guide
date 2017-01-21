package qixyqix.com.erpguide;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TabHost;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap erpLocationMap;
    GoogleMap topUpLocationMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TabHost host = (TabHost)findViewById(R.id.tabHostMainActivity);
        host.setup();

        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec("Tab One");
        spec.setContent(R.id.tab1);
        spec.setIndicator("ERP Locations");
        host.addTab(spec);

        spec = host.newTabSpec("Tab Two");
        spec.setContent(R.id.tab2);
        spec.setIndicator("TopUp Locations");
        host.addTab(spec);

        initializeMap(R.id.fragmentMapERPLocation);
        initializeMap(R.id.fragmentMapTopUpLocation);
    }

    //Reference for the initialize map code
    //https://www.youtube.com/watch?v=lchyOhPREh4
    private void initializeMap(int id) {
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(id);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        //Set up ERP Location Map
        this.erpLocationMap = googleMap;
        this.topUpLocationMap = googleMap;
        //Zoom to marina bay area aka downtown area aka where most of the ERPs are at
        zoomToLocation(1.287268, 103.855933,12,erpLocationMap);
        zoomToLocation(1.287268, 103.855933,12,topUpLocationMap);

        populateMarkers();
    }

    public void zoomToLocation(double lat, double lng, float zoom, GoogleMap map){
        LatLng latLng = new LatLng(lat,lng);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng,zoom);
        map.moveCamera(cameraUpdate);

    }

    public void populateMarkers(){
        MarkerOptions marker = new MarkerOptions()
                .title("Fullerton Road Eastbound at Fullerton (63) ERP LOCATION")
                .position(new LatLng(1.286033, 103.853490));
        erpLocationMap.addMarker(marker);

        MarkerOptions marker2 = new MarkerOptions()
                .title("Fullerton Road Westbound at One Fullerton (64) TOP UP LOCATION")
                .position(new LatLng(1.286091, 103.853961));
        topUpLocationMap.addMarker(marker2);
    }
}
