package com.lencorp.com.arguilechessclock;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

public class Settings extends AppCompatActivity {

    Button btnSave;
    //EditText etTime;
    ToggleButton tbSound;

    Spinner spTime;
    ArrayAdapter<CharSequence> adapter;

    boolean isSound;
    boolean isCustomTimeSelected;
    boolean _ignore;
    //boolean doesDecimalExist;
    long selectedSpinnerItem;

    KeyListener etKeyListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        btnSave = (Button) findViewById(R.id.btnSave);
        //etTime = (EditText) findViewById(R.id.etTime);
        spTime = (Spinner) findViewById(R.id.spTime);
        tbSound = (ToggleButton) findViewById(R.id.tbSound);

        Intent myLocalIntent = getIntent();

        selectedSpinnerItem = myLocalIntent.getLongExtra("key3", 0);
        isSound = myLocalIntent.getBooleanExtra("key4", true);

        //for spTime listener
        //selectedSpinnerItem = 0;

        //default sound on
        //isSound = true;

        if(isSound == true)
        {
            //default sound being on
            tbSound.setChecked(true);
        }
        else
            tbSound.setChecked(false);


        tbSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tbSound.isChecked() == true)
                {
                    isSound = true;
                }
                else
                    isSound = false;
            }
        });


        //global used for SaveSettingsMethod
        isCustomTimeSelected = false;
        //used in TextWatcher
        //doesDecimalExist = false;
        //for text watcher
        _ignore = false;

        //used for making etTime on focusable
        //etKeyListener = etTime.getKeyListener();
        //disables focus
        //etTime.setKeyListener(null);

        //parameters context, string array to use, layout of spinner item
        adapter = ArrayAdapter.createFromResource(this,R.array.default_game_times, android.R.layout.simple_spinner_item);
        //layout of dropdown items when selecting
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //set adapter to spinner object
        spTime.setAdapter(adapter);
        if(selectedSpinnerItem != 0)
        {
            //set default value
            spTime.setSelection((int)selectedSpinnerItem);
        }

        //handles when an item is selected on the spinner
        spTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    //assign selected item position to global
                    selectedSpinnerItem = position;
                    //pop up that displays selected drop down item
                    Toast.makeText(getBaseContext(), parent.getItemAtPosition(position)+ " selected.", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
/*
    //no longer used
    public void OnRadioButtonClicked(View v)
    {
        //is button checked?
        boolean checked = ((RadioButton) v).isChecked();

        //check which radio button was clicked
        switch(v.getId())
        {
            case R.id.rbCustomTime:
                if(checked == true)
                {
                    etTime.setEnabled(true);
                    spTime.setEnabled(false);
                    //for use in SaveSettings method
                    isCustomTimeSelected = true;
                    //enables widget focus
                    etTime.setKeyListener(etKeyListener);
                }
                break;
            case R.id.rbPresetTimes:
                if(checked == true)
                {
                    etTime.setEnabled(false);
                    spTime.setEnabled(true);
                    //for use in SaveSettings method
                    isCustomTimeSelected = false;
                    //disables focus
                    etTime.setKeyListener(null);
                }
                break;
        }
    }
*/
    public void SaveSettings(View v)
    {
        //create an intent to call main activity
        Intent myIntent = new Intent(this, play.class);

        //myIntent.putExtra("key1", isCustomTimeSelected);
/*
        if(isCustomTimeSelected == true)
        {
            //get data from edit text
            //long customTime = (long)Double.parseDouble(etTime.getText().toString());
            double customTime = Double.parseDouble(etTime.getText().toString());

            Log.d("***CUSTOMTIME***", String.valueOf(customTime));

            //time in seconds
            customTime = customTime * 60000;

            //supply intent with extra to pass data to second activity
            myIntent.putExtra("key2", customTime);
        }
*/
        //adding +1 since value is a position in array
        if(selectedSpinnerItem != 0)
        {
            myIntent.putExtra("key3", (selectedSpinnerItem) * 60000);
        }
        else
            myIntent.putExtra("key3", (selectedSpinnerItem + 1) * 60000);


        //sound is off or on
        myIntent.putExtra("key4", isSound);

        //start activity by intent
        startActivity(myIntent);
    }
}








