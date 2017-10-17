package com.lencorp.com.arguilechessclock;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.*;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class play extends AppCompatActivity {

    //declare buttons to be used
    Button btnP1Timer;
    Button btnP2Timer;
    Button btnPause;
    Button btnSettings;
    Button btnCancel;

    //sound
    MediaPlayer turnSound;

    //variable that handles countdown timer paused status
    boolean isPaused;
    boolean isCancelled;
    //handles which player is active
    boolean isP1Active;
    boolean isP2Active;
    boolean isGameStarted;
    boolean isCustomTime;
    boolean isSound;
    boolean isGameOver;
    //holds remaining time for countdown timer
    long p1TimeRemaining;
    long p2TimeRemaining;
    long assignTime;

    int tempActivePlayer;

    //universal timer
    long universalIntervalTimer;
    long modTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        //initialize variables
        btnP1Timer = (Button) findViewById(R.id.btnP1Timer);
        btnP2Timer = (Button) findViewById(R.id.btnP2Timer);
        btnPause = (Button) findViewById(R.id.btnPause);
        btnSettings = (Button) findViewById(R.id.btnSettings);
        btnCancel = (Button) findViewById(R.id.btnCancel);

        //initially disable pause, resume, cancel button
        btnPause.setEnabled(false);
        btnCancel.setEnabled(false);

        isPaused = false;
        isCancelled = false;
        isGameOver = false;

        isP1Active = false;
        isP2Active = false;
        isGameStarted = false;
        //for timer
        universalIntervalTimer = 250;
        modTimer = 1000;
        //for pause button
        tempActivePlayer = 0;
        //change background color of button to RED
        btnPause.setBackgroundColor(Color.RED);

        //pick call made to settings via Intent
        Intent myLocalIntent = getIntent();
        //default is preset time
        isCustomTime = myLocalIntent.getBooleanExtra("key1", false);
        //check if custom time was selected
        if(isCustomTime == true)
        {
            assignTime = myLocalIntent.getLongExtra("key2", 180000); // 180000 value is incase there is no data from intent
        }
        else
            assignTime = myLocalIntent.getLongExtra("key3", 180000); // 180000 value is incase there is no data from intent

        //check if sound is off or on
        isSound = myLocalIntent.getBooleanExtra("key4", true); //sound on by default

        //assigns sound file
        turnSound = MediaPlayer.create(this, R.raw.chessclock);

        p1TimeRemaining = assignTime; //3 minutes
        p2TimeRemaining = assignTime; //3 minutes

        //set starting text of buttons based on set time
        btnP1Timer.setText("" + assignTime / 60000 + ":00:000");
        btnP2Timer.setText("" + assignTime / 60000 + ":00:000");
    }

    public void OpenSettingsMenu(View v)
    {
        //set an intent and assign it to the settings class
        Intent settingsIntent = new Intent(this, Settings.class);

        settingsIntent.putExtra("key3", assignTime / 60000);
        settingsIntent.putExtra("key4", isSound);

        //open the settings menu when settings button is pressed
        startActivity(settingsIntent);
    }

    //for player one
    public void StartP1Timer(View v) {
        if(isPaused == true)
        {
            return;
        }

        if(isP1Active == true && isSound == true)
        {
            //plays sound
            turnSound.start();
        }

        //if game hasn't started
        if(isGameStarted == false)
        {
            //set starting time for each player
            //btnP1Timer.setText("03:00:000");
            //btnP2Timer.setText("03:00:000");

            isPaused = false;
            isCancelled = false;
            //game has started
            isGameStarted = true;
            isP1Active = true;
            isP2Active = false;

            //enable the pause and cancel button
            btnPause.setEnabled(true);
            btnCancel.setEnabled(true);

            //default time info
            long millisInFuture = assignTime; //3 minutes
            long countDownInterval = universalIntervalTimer; //100 milliseconds

            android.os.CountDownTimer cdt;

            //initialize countdowntimer
            cdt = new android.os.CountDownTimer(millisInFuture, countDownInterval) {
                @Override
                public void onTick(long millisUntilFinished) {
                    //do something every tick
                    if(isPaused == true || isCancelled == true || isP2Active == true)
                    {
                        //if the user requests to cancel or pause the countdowntimer
                        //also if no longer players turn
                        //we will cancel the current instance
                        cancel();
                    }
                    else
                    {
                        //display the remaining seconds to app interface
                        //1 second == 1000 milliseconds
                        btnP1Timer.setText("" + (millisUntilFinished / (1000 * 60)) % 60 + ":"
                                + (millisUntilFinished / 1000) % 60 + ":"
                                + millisUntilFinished % modTimer);
                        //put count down timer remaining time in a variable
                        p1TimeRemaining = millisUntilFinished;

                        //change color red if less then 30 seconds, yellow if less then 60 seconds
                        if(p1TimeRemaining <= 30000)
                        {
                            btnP1Timer.setBackgroundColor(Color.RED);
                        }
                        else if(p1TimeRemaining <= 60000)
                            btnP1Timer.setBackgroundColor(Color.YELLOW);
                    }
                }

                @Override
                public void onFinish() {
                    //do something when count down is finished
                    btnP1Timer.setText("Timer has run out!");
                    //disable pause and cancel buttons
                    btnPause.setEnabled(false);
                    //btnCancel.setEnabled(false);
                    //Pause game since game over
                    PauseTimer();
                    //reset game start
                    isGameStarted = false;
                }
            }.start(); // start count down timer
        }
        else if(isPaused == true && isGameStarted == true)//if paused is true, resume timer
        {
            ResumeP1Timer();
        }
        else if(isP1Active == true && isGameStarted == true)//if p1 is done with his turn, stop his clock and start p2's clock
        {
            ResumeP2Timer();
        }
    }

    //for player two
    public void StartP2Timer(View v) {

        if(isPaused == true)
        {
            return;
        }

        if(isP2Active == true && isSound == true)
        {
            //plays sound
            turnSound.start();
        }

        //if game hasn't started
        if(isGameStarted == false)
        {
            //set starting time for each player
            //btnP1Timer.setText("03:00:000");
            //btnP2Timer.setText("03:00:000");

            isPaused = false;
            isCancelled = false;
            //game has started
            isGameStarted = true;
            isP2Active = true;
            isP1Active = false;

            //enable the pause and cancel button
            btnPause.setEnabled(true);
            btnCancel.setEnabled(true);

            android.os.CountDownTimer cdt2;

            //default time info
            long millisInFuture = assignTime; //30 seconds
            long countDownInterval = universalIntervalTimer; //100 milliseconds

            //initialize countdowntimer
            cdt2 = new android.os.CountDownTimer(millisInFuture, countDownInterval) {
                @Override
                public void onTick(long millisUntilFinished) {
                    //do something every tick
                    if(isPaused == true || isCancelled == true || isP1Active == true)
                    {
                        //if the user requests to cancel or pause the countdowntimer
                        //also if no longer players turn
                        //we will cancel the current instance
                        cancel();
                    }
                    else
                    {
                        //diplay the remaining seconds to app interface
                        //1 second == 1000 milliseconds
                        btnP2Timer.setText("" + (millisUntilFinished / (1000 * 60)) % 60 + ":"
                                + (millisUntilFinished / 1000) % 60 + ":"
                                + millisUntilFinished % modTimer);
                        //put count down timer remaining time in a variable
                        p2TimeRemaining = millisUntilFinished;

                        //change color red if less then 30 seconds, yellow if less then 60 seconds
                        if(p2TimeRemaining <= 30000)
                        {
                            btnP2Timer.setBackgroundColor(Color.RED);
                        }
                        else if(p2TimeRemaining <= 60000)
                            btnP2Timer.setBackgroundColor(Color.YELLOW);
                    }

                }

                @Override
                public void onFinish() {
                    //do something when count down is finished
                    btnP2Timer.setText("Timer has run out!");

                    //disable pause and cancel buttons
                    btnPause.setEnabled(false);
                    //btnCancel.setEnabled(false);
                    //Pause game since game over
                    PauseTimer();
                }
            }.start(); // start count down timer
        }
        else if(isPaused == true && isGameStarted == true)//if paused is true, resume timer
        {
            ResumeP2Timer();
        }
        else if(isP2Active == true && isGameStarted == true)//if p2 is done with his turn, stop his clock and start p1's clock
        {
            ResumeP1Timer();
        }
    }

    //for pause button -- calls PauseTimer()
    public void CallPauseTimer(View v) {
        PauseTimer();
    }

    public void PauseTimer() {
        if(isPaused == false)
        {
            //change background color of button to GREEN
            btnPause.setBackgroundColor(Color.GREEN);
            //check who was active before pause was pressed
            if(isP1Active == true)
            {
                tempActivePlayer = 1;
            }
            else tempActivePlayer = 2;

            //when user is requesting to pause the count down timer
            //pauses both player clocks
            isPaused = true;
            isP1Active = false;
            isP2Active = false;

            //enable the cancel button
            btnCancel.setEnabled(true);

            //disable the pause button
            //btnPause.setEnabled(false);

            //change text of pause button to "Resume"
            btnPause.setText("Resume");
        }
        else if(isPaused == true)
        {
            //change background color of button to RED
            btnPause.setBackgroundColor(Color.RED);

            isPaused = false;
            //set text back to "Pause"
            btnPause.setText("Pause");

            if(tempActivePlayer == 1)
            {
                ResumeP1Timer();
            }
            else ResumeP2Timer();
        }
    }

    //for resume button player one
    public void ResumeP1Timer()
    {
        //enable the pause and cancel button
        btnPause.setEnabled(true);
        btnCancel.setEnabled(true);
        //set who is active again
        isP1Active = true;
        isP2Active = false;


        if(isCancelled == true)
        {
            btnP1Timer.setText("" + assignTime / 60000 + ":00:000");
            btnP2Timer.setText("" + assignTime / 60000 + ":00:000");
        }

        //specify the current state is not paused and cancelled
        isPaused = false;
        isCancelled = false;

        //initialize a new count down timer instance
        long millisInFuture = p1TimeRemaining;
        long countDownInterval = universalIntervalTimer; //100 milliseconds
        android.os.CountDownTimer newCDT;

        newCDT = new android.os.CountDownTimer(millisInFuture, countDownInterval) {
            @Override
            public void onTick(long millisUntilFinished) {
                //do something every tick
                if(isCancelled == true || isPaused == true || isP2Active == true)
                {
                    //if the user requested to pause or cancel the timer
                    //or if other players turn
                    cancel();
                }
                else
                {
                    //display timer on button
                    btnP1Timer.setText("" + (millisUntilFinished / (1000 * 60)) % 60 + ":"
                            + (millisUntilFinished / 1000) % 60 + ":"
                            + millisUntilFinished % modTimer);
                    //put count down timer remaining time in a variable
                    p1TimeRemaining = millisUntilFinished;

                    //change color red if less then 30 seconds, yellow if less then 60 seconds
                    if(p1TimeRemaining <= 30000)
                    {
                        btnP1Timer.setBackgroundColor(Color.RED);
                    }
                    else if(p1TimeRemaining <= 60000)
                        btnP1Timer.setBackgroundColor(Color.YELLOW);
                }
            }

            @Override
            public void onFinish() {
                //do something when the count down is done
                btnP1Timer.setText("Timer has run out!");
                //disable the pause and cancel buttons
                btnPause.setEnabled(false);
                //btnCancel.setEnabled(false);
                //Pause game since game over
                isGameOver = true;
                PauseTimer();
                //CancelTimer();
            }
        }.start(); //start timer
    }

    //for resume button player 2
    public void ResumeP2Timer()
    {
        //enable the pause and cancel button
        btnPause.setEnabled(true);
        btnCancel.setEnabled(true);

        if(isCancelled == true)
        {
            btnP1Timer.setText("" + assignTime / 60000 + ":00:000");
            btnP2Timer.setText("" + assignTime / 60000 + ":00:000");
        }

        //specify the current state is not paused and cancelled
        isPaused = false;
        isCancelled = false;
        //set who is active again
        isP1Active = false;
        isP2Active = true;

        //initialize a new count down timer instance
        long millisInFuture = p2TimeRemaining;
        long countDownInterval = universalIntervalTimer; //100 milliseconds
        android.os.CountDownTimer newCDT2;

        newCDT2 = new android.os.CountDownTimer(millisInFuture, countDownInterval) {
            @Override
            public void onTick(long millisUntilFinished) {
                //do something every tick
                if(isCancelled == true || isPaused == true || isP1Active == true)
                {
                    //if the user requested to pause or cancel the timer
                    //or if other players turn
                    cancel();
                }
                else
                {
                    //display timer on button
                    btnP2Timer.setText("" + (millisUntilFinished / (1000 * 60)) % 60 + ":"
                            + (millisUntilFinished / 1000) % 60 + ":"
                            + millisUntilFinished % modTimer);
                    //put count down timer remaining time in a variable
                    p2TimeRemaining = millisUntilFinished;

                    //change color red if less then 30 seconds, yellow if less then 60 seconds
                    if(p2TimeRemaining <= 30000)
                    {
                        btnP2Timer.setBackgroundColor(Color.RED);
                    }
                    else if(p2TimeRemaining <= 60000)
                        btnP2Timer.setBackgroundColor(Color.YELLOW);
                }
            }

            @Override
            public void onFinish() {
                //do something when the count down is done
                btnP2Timer.setText("Timer has run out!");
                //disable the pause and cancel buttons
                btnPause.setEnabled(false);
                //btnCancel.setEnabled(false);
                //Pause game since game over
                isGameOver = true;
                PauseTimer();
                //CancelTimer();
            }
        }.start(); //start timer
    }

    //for cancel/stop button
    public void CancelTimer() {

        if(isGameOver == true)
        {
            // do nothing
        }
        else {
            //notify user that count down timer is cancelled/stopped
            btnP1Timer.setText("" + assignTime / 60000 + ":00:000");
            btnP2Timer.setText("" + assignTime / 60000 + ":00:000");
        }
        //reset game variables
        isGameOver = false;
        isPaused = false;

        //change background color of button to GREEN
        btnPause.setBackgroundColor(Color.RED);

        btnP1Timer.setBackgroundColor(Color.GRAY);
        btnP2Timer.setBackgroundColor(Color.GRAY);

        //user wants timer to be cancelled
        isCancelled = true;
        //set game back to not being started
        isGameStarted = false;
        //players are back to noone being ready
        isP1Active = false;
        isP2Active = false;

        //disable the cancel and pause button
        btnPause.setEnabled(false);
        btnCancel.setEnabled(false);

        //time remaining for both players needs to be reset
        p1TimeRemaining = assignTime;
        p2TimeRemaining = assignTime;
    }

    public void CallCancelTimer(View v)
    {
        CancelTimer();
    }

}
