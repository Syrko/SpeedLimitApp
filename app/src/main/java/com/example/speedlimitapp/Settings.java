package com.example.speedlimitapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import java.util.HashMap;

/**
 * Activity containing the app settings
 * List of settings implemented:
 *      Speed Limit: The user inputs a number and chooses the units
 *      Clear DB:    After checking a switch, a button is enabled that when long clicked
 *                   clears the database of the app
 */
public class Settings extends AppCompatActivity {

    // Declare UI components
    private EditText speedLimit;
    private Spinner speedType;
    private Button confirmButton;
    private Switch switchClear;
    private Button buttonClear;

    // Other variables
    private SharedPreferences sharedPreferences;
    private Float initSpeedLimit;
    private String initSpeedType;
    private HashMap<String, Integer> speedTypes = new HashMap<String, Integer>(){{
        put("m/s", 0);
        put("km/h", 1);
    }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Initializing UI
        speedLimit = findViewById(R.id.settings_speed_limit_input);
        speedType = findViewById(R.id.speed_type_spinner);
        confirmButton = findViewById(R.id.confirm_settings_button);
        buttonClear = findViewById(R.id.clearDbButton);
        switchClear = findViewById(R.id.clearDbSwitch);

        // Initialize other variables
        sharedPreferences = this.getSharedPreferences(getString(R.string.settings_preferences), Context.MODE_PRIVATE);
        Intent intent = getIntent();
        initSpeedLimit = intent.getFloatExtra("currentSpeedLimit", 0);
        initSpeedType = intent.getStringExtra("currentSpeedType");

        // Initialize settings
        speedLimit.setText(String.format("%.2f", initSpeedLimit));
        speedType.setSelection(speedTypes.get(initSpeedType));
        switchClear.setChecked(false);
        buttonClear.setEnabled(false);

        // Setup Listeners
        setUpSwitchListeners();
        setUpButtonListeners();
    }

    /**
     * Sets up the required button listeners
     */
    private void setUpButtonListeners(){
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                // Writes to shared prefs and makes speed limit equal to input
                // If no input is given defaults to 0
                if(speedLimit.getText().toString().equals("")){
                    editor.putFloat(getString(R.string.sp_speed_limit), initSpeedLimit);
                }
                else{
                    editor.putFloat(getString(R.string.sp_speed_limit), Float.parseFloat(speedLimit.getText().toString()));
                }
                editor.putString(getString(R.string.sp_speed_type), speedType.getSelectedItem().toString());
                editor.commit();
                Toast.makeText(getBaseContext(), "Settings Saved!", Toast.LENGTH_LONG).show();
                setResult(RESULT_OK);
                finish();
            }
        });

        buttonClear.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                DatabaseHelper.getInstance(getBaseContext()).ClearDatabase();
                Toast.makeText(getBaseContext(), "Database Cleared!", Toast.LENGTH_LONG).show();
                switchClear.setChecked(false);
                return true;
            }
        });
    }

    /**
     * Sets up the required switch listeners
     */
    private void setUpSwitchListeners(){
        switchClear.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                buttonClear.setEnabled(isChecked);
            }
        });
    }
}
