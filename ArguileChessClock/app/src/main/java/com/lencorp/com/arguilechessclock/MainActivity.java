package com.lencorp.com.arguilechessclock;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    long cdTimer = 10000;
    Button btnTimer;
    CountDownTimer cdt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnTimer = (Button) findViewById(R.id.btnTimer);
    }

    public void StartStopTimer(View v) {
        //CountDownTimer total time and countdown in increments
        cdt = new CountDownTimer(10000, 100) {
            @Override
            public void onTick(long millisUntilFinished) {
                //change the text of the button per millisecond
                btnTimer.setText("" + cdTimer);
                cdTimer-= 100;
                Log.e("TIME", "Timer is " + cdTimer);
            }

            @Override
            public void onFinish() {
                //when timer hits 0
                btnTimer.setText("Timer Finished!");
            }
        }.start();
    }
}
