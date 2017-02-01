package qixyqix.com.erpguide;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

public class Settings extends AppCompatActivity {
    private SharedPreferences preferences;
    private SharedPreferences.Editor preferenceEditor;

    boolean tweleveHr;
    boolean fastStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        preferences = getSharedPreferences("ERPGuide",MODE_PRIVATE);
        preferenceEditor = preferences.edit();

        tweleveHr = preferences.getBoolean("tweleveHr",false);
        fastStart = preferences.getBoolean("fastStart",false);

        Switch switch12h = (Switch) findViewById(R.id.switchSettingsTweleveHr);
        Switch switchFast = (Switch) findViewById(R.id.switchFastStart);

        switch12h.setChecked(tweleveHr);
        switchFast.setChecked(fastStart);

        switch12h.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                tweleveHr = !tweleveHr;
                preferenceEditor.putBoolean("tweleveHr",tweleveHr);
            }
        });

        switchFast.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                fastStart = !fastStart;
                preferenceEditor.putBoolean("fastStart",fastStart);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        preferenceEditor.commit();
    }
}
