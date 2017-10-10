package com.lencorp.com.arguilechessclock;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.KeyListener;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

public class Settings extends AppCompatActivity {

    Button btnSave;
    EditText etTime;

    Spinner spTime;
    ArrayAdapter<CharSequence> adapter;

    boolean isCustomTimeSelected;
    //boolean doesDecimalExist;
    long selectedSpinnerItem;

    KeyListener etKeyListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        btnSave = (Button) findViewById(R.id.btnSave);
        etTime = (EditText) findViewById(R.id.etTime);
        spTime = (Spinner) findViewById(R.id.spTime);

        selectedSpinnerItem = 0;
        //global used for SaveSettingsMethod
        isCustomTimeSelected = false;
        //used in TextWatcher
        //doesDecimalExist = false;

        //used for making etTime on focusable
        etKeyListener = etTime.getKeyListener();
        //disables focus
        etTime.setKeyListener(null);

        //parameters context, string array to use, layout of spinner item
        adapter = ArrayAdapter.createFromResource(this,R.array.default_game_times, android.R.layout.simple_spinner_item);
        //layout of dropdown items when selecting
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //set adapter to spinner object
        spTime.setAdapter(adapter);

        //handles when an item is selected on the spinner
        spTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //pop up that displays selected drop down item
                Toast.makeText(getBaseContext(), parent.getItemAtPosition(position)+ " selected.", Toast.LENGTH_LONG).show();
                //assign selected item position to global
                selectedSpinnerItem = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        TextWatcher watcher;

        //text watcher that fires when text is changed in custom time
        //Goal is to stop multiple decimals
        etTime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int after) {
                //check if there is a decimal already in the string
                String currentString = etTime.getText().toString();
                //loop through string
                for(int x = 0; x < before; x++)
                {
                    if(currentString.substring(x,x+1).equals("."))
                    {
                        //exits the textwatcher
                        return;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

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

    public void SaveSettings(View v)
    {
        //create an intent to call main activity
        Intent myIntent = new Intent(this, MainActivity.class);

        if(isCustomTimeSelected == true)
        {
            //get data from edit text
            long customTime = Long.parseLong(etTime.getText().toString());

            //time in seconds
            customTime = customTime * 60000;

            //supply intent with extra to pass data to second activity
            myIntent.putExtra("key1", customTime);
        }
        //adding +1 since value is a position in array
        myIntent.putExtra("key2", (selectedSpinnerItem + 1) * 60000);

        //start activity by intent
        startActivity(myIntent);
    }
}








