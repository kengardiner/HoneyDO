package com.cybermoosemoosemedia.honeydo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.cybermoosemoosemedia.honeydo.R;


/**
 * Created by ken on 8/27/16.
 */
public class HoneyDoSplashScreenActivity extends Activity {

        int SPLASH_TIMER = 3000;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_splash_screen);

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    // this method will be executed once the timer is over
                    // start the app main activity
                    Intent i = new Intent(HoneyDoSplashScreenActivity.this, HoneyDoListActivity.class);
                    startActivity(i);

                    // close this activity
                    finish();
                }
            }, SPLASH_TIMER);
        }

    }

