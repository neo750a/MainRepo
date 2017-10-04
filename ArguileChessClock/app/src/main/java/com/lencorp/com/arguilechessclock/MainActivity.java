package com.lencorp.com.arguilechessclock;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void StartGame(View v)
    {
        //1 second = 1000 milliseconds
        long defaultTime = 180000; //3 minutes in milliseconds

        //set an intent and assign it to the settings class
        Intent playIntent = new Intent(this, play.class);

        //supply intent with extra to pass data to second activity
        playIntent.putExtra("time", defaultTime); //default is 3 minutes aka 60000 milliseconds
        //playIntent.putExtra("sound", ); //on or off

        //open the settings menu when settings button is pressed
        startActivity(playIntent);
    }


    public void OpenSettingsMenu(View v)
    {
        //set an intent and assign it to the settings class
        Intent settingsIntent = new Intent(this, Settings.class);

        //open the settings menu when settings button is pressed
        startActivity(settingsIntent);
    }




}








