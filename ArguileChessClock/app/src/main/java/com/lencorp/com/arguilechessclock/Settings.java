package com.lencorp.com.arguilechessclock;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        btnSave = (Button) findViewById(R.id.btnSave);
        etTime = (EditText) findViewById(R.id.etTime);
        spTime = (Spinner) findViewById(R.id.spTime);


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
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

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
                }
                break;
            case R.id.rbPresetTimes:
                if(checked == true)
                {
                    etTime.setEnabled(false);
                    spTime.setEnabled(true);
                }
                break;
        }
    }

    public void SaveSettings(View v)
    {
        //get data from edit text
        long customTime = Long.parseLong(etTime.getText().toString());

        //time in seconds
        customTime = customTime * 10000;

        //create an intent to call main activity
        Intent myIntent = new Intent(this, MainActivity.class);

        //supply intent with extra to pass data to second activity
        myIntent.putExtra("key1", customTime);

        //start activity by intent
        startActivity(myIntent);
    }
}








