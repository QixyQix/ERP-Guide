package qixyqix.com.erpguide;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.vision.text.Text;

public class TopUpDetail extends AppCompatActivity {

    GoogleMap topUpMap;
    double lat;
    double lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_up_detail);

        //Get the topUp location details
        Intent usedIntent = getIntent();
        //Get all the data from the bundle
        final TopUpLocation topUpLocation = (TopUpLocation) usedIntent.getExtras().getSerializable("location");

        //Get the widget references
        TextView txtTitle = (TextView) findViewById(R.id.txtTopUpTitle);
        TextView txtDescription = (TextView) findViewById(R.id.txtDescription);
        TextView txtAddress = (TextView) findViewById(R.id.txtAddress);

        //Set the data into the activity
        txtTitle.setText(topUpLocation.getTitle());
        txtDescription.setText(topUpLocation.getDescription());
        txtAddress.setText(topUpLocation.getAddress());
        lat = topUpLocation.getLat();
        lng = topUpLocation.getLng();

        //Setup Map
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.fragmentTopUpDetail);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                topUpMap = googleMap;

                LatLng latLng = new LatLng(lat,lng);
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng,15);
                topUpMap.moveCamera(cameraUpdate);

                MarkerOptions marker = new MarkerOptions()
                        .position(latLng);
                topUpMap.addMarker(marker);
            }
        });
    }
}
