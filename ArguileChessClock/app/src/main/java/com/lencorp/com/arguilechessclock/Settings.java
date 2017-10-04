package com.lencorp.com.arguilechessclock;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Settings extends AppCompatActivity {

    Button btnSave;
    EditText etTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        btnSave = (Button) findViewById(R.id.btnSave);
        etTime = (EditText) findViewById(R.id.etTime);
    }

    public void SaveSettings(View v)
    {
        //get data from edit text
        long data = Long.parseLong(etTime.getText().toString());

        //time in seconds
        data = data * 1000;

        //create an intent to call main activity
        Intent myIntent = new Intent(this, MainActivity.class);

        //supply intent with extra to pass data to second activity
        myIntent.putExtra("key1", data);

        //start activity by intent
        startActivity(myIntent);
    }
}








