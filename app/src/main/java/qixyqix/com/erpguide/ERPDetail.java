package qixyqix.com.erpguide;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TextView;

public class ERPDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_erpdetail);

        TextView erpTitle = (TextView) findViewById(R.id.txtERPTitle);
        TextView erpStatus = (TextView) findViewById(R.id.txtERPStatus);

        Intent usedIntent = this.getIntent();
        ERPGantry gantry = (ERPGantry) usedIntent.getExtras().getSerializable("gantry");

        erpTitle.setText(gantry.getTitle());
        erpStatus.setText("Not in operation");

        TabHost tabHost =(TabHost) findViewById(R.id.tabERPDetail);
        tabHost.setup();

        //Tab cars & light vehicles
        TabHost.TabSpec spec = tabHost.newTabSpec("Tab Car");
        spec.setContent(R.id.tabCar);
        spec.setIndicator("Cars & Light Vehicles");
        tabHost.addTab(spec);

        //Tab buses
        spec = tabHost.newTabSpec("Tab Bus");
        spec.setContent(R.id.tabBus);
        spec.setIndicator("Buses");
        tabHost.addTab(spec);

        //Tab Heavy Vehicles
        spec = tabHost.newTabSpec("Tab Heavy Vehicles");
        spec.setContent(R.id.tabHeavy);
        spec.setIndicator("Heavy Vehicles");
        tabHost.addTab(spec);
        
    }
}
