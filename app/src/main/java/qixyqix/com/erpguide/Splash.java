package qixyqix.com.erpguide;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by QiXiang on 19/11/2016.
 */

public class Splash extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Launch splash layout
        setContentView(R.layout.splash);
        Thread splashThread = new Thread() {

            public void run() {
                try {
                    //3 seconds
                    sleep(3000);
                }  catch(Exception e) {
                    //Show error
                    e.printStackTrace();
                } finally
                {
                    //Start the main activity
                    Intent intent = new Intent(Splash.this, MainActivity.class);
                    startActivity(intent);
                }

            }
        };
        // To Start the thread
        splashThread.start();
    }
}

