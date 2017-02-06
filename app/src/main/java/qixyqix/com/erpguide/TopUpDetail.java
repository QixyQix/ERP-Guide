package qixyqix.com.erpguide;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.vision.text.Text;

public class TopUpDetail extends AppCompatActivity {

    private GoogleMap topUpMap;
    private DatabaseHandler db;
    private boolean favourite;
    private int ID;
    private double lat;
    private double lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_up_detail);

        db = new DatabaseHandler(this);

        //Get the topUp location details
        Intent usedIntent = getIntent();
        //Get all the data from the bundle
        final TopUpLocation topUpLocation = (TopUpLocation) usedIntent.getExtras().getSerializable("location");
        ID = topUpLocation.getID();
        favourite = db.isFavourite("TopUp",ID);

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

        Button btnDirect = (Button) findViewById(R.id.btnDirections);
        btnDirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri gmmIntentUri = Uri.parse("google.navigation:q="+lat+","+lng+"&mode=d&avoid=t");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });

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
                    if(db.deleteFavourite("TopUp",ID)){
                        item.setIcon(R.drawable.ic_star_border_white_24dp);
                        item.setTitle("Add to favorites");
                        favourite = false;
                        Toast.makeText(this,"Successfully removed from favorites",Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(this,"Error removing from favorites",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    //else make it a favorite
                    if(db.insertFavourite("TopUp",ID)){
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
}
