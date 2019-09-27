package com.codeencounter.nagarro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.codeencounter.nagarro.Config.SessionManager;

public class SplashScreen extends AppCompatActivity {

    ImageView splash_image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        splash_image = (ImageView)findViewById(R.id.splashscreen_imageview);

        Animation myanim = AnimationUtils.loadAnimation(this,R.anim.mytransition);

        splash_image.startAnimation(myanim);

        final boolean logg = new SessionManager(getApplicationContext()).isLoggedIn();
        Log.e("splash", logg+"");

        Thread timer = new Thread()
        {
            public void run()
            {
                try{
                    sleep(3000);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally {
                    Intent i;
                    Log.e("splash", logg+"dddd");
                    if(logg==true) {
                        i = new Intent(getApplicationContext(), HomePage.class);
                        startActivity(i);
                    }else {
                        i = new Intent(getApplicationContext(), Login.class);
                        startActivity(i);
                    }
                    finish();
                }
            }
        };
        timer.start();
    }
}
