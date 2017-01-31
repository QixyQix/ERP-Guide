package qixyqix.com.erpguide;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class Splash extends AppCompatActivity {
    private Data dataObj;
    private DataBundle dataBundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void onStart() {
        super.onStart();

        //Splash Screen thread
        Thread thread = new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    dataObj = new Data(Splash.this.getApplicationContext());
                    dataBundle = dataObj.getDataBundle();
                    sleep(2500); //Sleep for 2.5seconds
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //Get the imageView
                            ImageView imageView = (ImageView) findViewById(R.id.imageViewSplash);
                            //Set it to the second image
                            imageView.setBackgroundResource(R.mipmap.splashpt2);
                        }
                    });
                    sleep(500); //Sleep for half a second
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //Load animation
                            Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.zoom_in);
                            //Set duration to 3 seconds (but it will move to the next activity halfway)
                            animation.setDuration(3000);
                            //Get the imageView
                            ImageView imageView = (ImageView) findViewById(R.id.imageViewSplash);
                            //Start the animation
                            imageView.startAnimation(animation);
                        }
                    });

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally{
                    //Create bundle to pass the data over
                    Bundle b = new Bundle();
                    b.putSerializable("data",dataBundle);
                    //Create new intent for main activity
                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                    intent.putExtras(b);
                    //Start the activity
                    startActivity(intent);
                    //close this activity (So user cannot go back)
                    finish();
                }
            }
        };

        thread.start();
    }

    }
