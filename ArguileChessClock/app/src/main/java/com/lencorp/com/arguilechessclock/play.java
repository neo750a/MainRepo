package com.lencorp.com.arguilechessclock;

import android.content.Intent;
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
    //holds remaining time for countdown timer
    long p1TimeRemaining;
    long p2TimeRemaining;

    long assignTime;

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

        isP1Active = false;
        isP2Active = false;
        isGameStarted = false;

        universalIntervalTimer = 250;
        modTimer = 1000;

        turnSound = MediaPlayer.create(this, R.raw.chessclock);

        //pick call made to settings via Intent
        Intent myLocalIntent = getIntent();
        assignTime = myLocalIntent.getLongExtra("key1", 180000); // 180000 value is incase there is no data from intent

        p1TimeRemaining = assignTime; //3 minutes
        p2TimeRemaining = assignTime; //3 minutes

        //set starting text of buttons based on set time
        btnP1Timer.setText("" + assignTime / 60000);
        btnP2Timer.setText("" + assignTime / 60000);
    }

    public void OpenSettingsMenu(View v)
    {
        //set an intent and assign it to the settings class
        Intent settingsIntent = new Intent(this, Settings.class);

        //open the settings menu when settings button is pressed
        startActivity(settingsIntent);
    }

    //for player one
    public void StartP1Timer(View v) {
        if(isP1Active == true)
        {
            //plays sound
            turnSound.start();
        }

        //if game hasn't started
        if(isGameStarted == false)
        {
            //set starting time for each player
            btnP1Timer.setText("03:00:000");
            btnP2Timer.setText("03:00:000");

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
        if(isP2Active == true)
        {
            //plays sound
            turnSound.start();
        }

        //if game hasn't started
        if(isGameStarted == false)
        {
            //set starting time for each player
            btnP1Timer.setText("03:00:000");
            btnP2Timer.setText("03:00:000");

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
        //when user is requesting to pause the count down timer
        //pauses both player clocks
        isPaused = true;
        isP1Active = false;
        isP2Active = false;


        //enable the cancel button
        btnCancel.setEnabled(true);

        //disable the start and pause button
        btnPause.setEnabled(false);

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
                PauseTimer();
            }
        }.start(); //start timer
    }

    //for resume button player 2
    public void ResumeP2Timer()
    {
        //enable the pause and cancel button
        btnPause.setEnabled(true);
        btnCancel.setEnabled(true);

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
                PauseTimer();
            }
        }.start(); //start timer
    }

    //for cancel/stop button
    public void CancelTimer(View v) {
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

        //notify user that count down timer is cancelled/stopped
        btnP1Timer.setText("Timer has been Reset.  Press to Start!");
        btnP2Timer.setText("Timer has been Reset.  Press to Start!");

        //time remaining for both players needs to be reset
        p1TimeRemaining = assignTime;//3 minutes
        p2TimeRemaining = assignTime;//3 minutes
    }



}
