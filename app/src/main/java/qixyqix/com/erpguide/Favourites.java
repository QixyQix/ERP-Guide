package qixyqix.com.erpguide;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TabHost;

public class Favourites extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);

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
    }
}
