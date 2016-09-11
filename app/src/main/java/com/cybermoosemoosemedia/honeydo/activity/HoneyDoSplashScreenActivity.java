package com.cybermoosemoosemedia.honeydo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.cybermoosemoosemedia.honeydo.R;

public class HoneyDoSplashScreenActivity extends Activity {
    int SPLASH_TIMER = 2000;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_splash_screen);

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    Intent i = new Intent(HoneyDoSplashScreenActivity.this, HoneyDoListActivity.class);
                    startActivity(i);
                    finish();
                }
            }, SPLASH_TIMER);
        }

    }

