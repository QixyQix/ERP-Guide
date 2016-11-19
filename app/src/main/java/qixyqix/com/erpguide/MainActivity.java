package qixyqix.com.erpguide;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeMap();
    }

    //Reference for the initialize map code
    //https://www.youtube.com/watch?v=lchyOhPREh4
    private void initializeMap() {
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapFragmentMainActivity);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        //Zoom to marina bay area aka downtown area aka where most of the ERPs are at
        zoomToLocation(1.287268, 103.855933,12);

        MarkerOptions marker = new MarkerOptions()
                .title("Fullerton Road Eastbound at Fullerton (63)")
                .position(new LatLng(1.286033, 103.853490));
        googleMap.addMarker(marker);

        MarkerOptions marker2 = new MarkerOptions()
                .title("Fullerton Road Westbound at One Fullerton (64)")
                .position(new LatLng(1.286091, 103.853961));
        googleMap.addMarker(marker2);
    }

    public void zoomToLocation(double lat, double lng, float zoom){
        LatLng latLng = new LatLng(lat,lng);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng,zoom);
        googleMap.moveCamera(cameraUpdate);

    }
}
