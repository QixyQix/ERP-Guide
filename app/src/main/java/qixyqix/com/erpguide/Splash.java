package qixyqix.com.erpguide;

import android.content.Intent;
import android.content.res.Resources;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

public class Splash extends AppCompatActivity implements Animation.AnimationListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void onStart() {
        super.onStart();
        startSplash();
    }

    private void startSplash(){
        ImageView splashImageView = (ImageView) findViewById(R.id.imageViewSplash);

        splashImageView.setBackgroundResource(R.mipmap.splashpt1);

        splashImageView.setVisibility(View.VISIBLE);

        SystemClock.sleep(TimeUnit.SECONDS.toMillis(5));

        splashImageView.setBackgroundResource(R.mipmap.splashpt2);

        SystemClock.sleep(TimeUnit.SECONDS.toMillis(4));

        Animation anim = AnimationUtils.loadAnimation(this,R.anim.zoom_in);

        anim.setDuration(300);

        anim.setAnimationListener(this);

        splashImageView.startAnimation(anim);
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        //Start the main activity
        Intent intent = new Intent(Splash.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
