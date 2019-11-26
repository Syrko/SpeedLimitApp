package com.example.speedlimitapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Timestamp;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // Request Codes
    private static final int REQ_CODE_LOC = 700;
    private static final int REQ_CODE_SETTINGS = 710;
    private static final int REQ_CODE_SPEECH_REC = 720;

    // Declare UI components
    private Button startButton;
    private Button stopButton;
    private Button viewViolButton;
    private Button helpButton;
    private Button voiceCommands;
    private TextView speedText;

    // Location managements objects
    LocationManager locationManager;
    LocationListener locationListener;

    // Other Variables
    private static final long MIN_TIME_LOC_UPDATE = 1000;

    private float currentSpeed;
    private float currentLimit;
    private float defaultLimit;
    private String currentSpeedType;
    private String defaultSpeedType;
    private SharedPreferences sharedPreferences;
    private TTSWarnings ttsWarnings;

    // Voice Commands Available
    private static final ArrayList<String> commands = new ArrayList<String>(){{add("start"); add("stop"); add("help"); add("history");}};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI components
        startButton = findViewById(R.id.start_button);
        stopButton = findViewById(R.id.stop_button);
        viewViolButton = findViewById(R.id.view_speed_viol_button);
        helpButton = findViewById(R.id.help_button);
        voiceCommands = findViewById(R.id.voice_commands_button);
        speedText = findViewById(R.id.speedText);

        // Initialize location objects
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        setUpLocationListener();

        // Setup Button Listeners
        setUpButtonListeners();

        //Initialize other variables
        currentSpeed = 0;
        sharedPreferences = this.getSharedPreferences(getString(R.string.settings_preferences), this.MODE_PRIVATE);
        defaultLimit = 0;
        defaultSpeedType = "m/s";
        currentLimit = sharedPreferences.getFloat(getString(R.string.sp_speed_limit), defaultLimit);
        currentSpeedType = sharedPreferences.getString(getString(R.string.sp_speed_type), defaultSpeedType);
        ttsWarnings = new TTSWarnings(getApplicationContext());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.app_settings:
            {
                // Starts settings activity after sending current limit and type
                Intent settingIntent = new Intent(this, Settings.class);
                settingIntent.putExtra("currentSpeedLimit", currentLimit);
                settingIntent.putExtra("currentSpeedType", currentSpeedType);
                startActivityForResult(settingIntent, REQ_CODE_SETTINGS);
                return  true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Sets up listeners for all the buttons
     */
    private void setUpButtonListeners(){
        // Setting up Start button with 'this' activity as listener
        // Easier to provide arguments this way
        startButton.setOnClickListener(this);

        // Setting up the other buttons
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationManager.removeUpdates(locationListener);
                speedText.setText(R.string.speed_view_default);
            }
        });

        viewViolButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), ViewViolationsMenu.class));
            }
        });

        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), Help.class));
            }
        });

        voiceCommands.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent voiceRecognition = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                voiceRecognition.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                voiceRecognition.putExtra(RecognizerIntent.EXTRA_PROMPT, "What is your command?");
                startActivityForResult(voiceRecognition, REQ_CODE_SPEECH_REC);
            }
        });
    }

    /**
     * Sets up location listener
     */
    private void setUpLocationListener(){
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                currentSpeed = transformSpeed(location.getSpeed(), currentSpeedType);
                speedText.setText(getString(R.string.speed_view_with_value, currentSpeed, currentSpeedType));
                HandleSpeedLimitViolation(location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
    }

    // OnClick listener for the Start button
    @Override
    public void onClick(View v) {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQ_CODE_LOC);
        }
        else{
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_LOC_UPDATE,0, locationListener);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQ_CODE_LOC: {
                // If permission for location is granted click again on start button
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    startButton.performClick();
                return;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQ_CODE_SETTINGS: {
                // After Settings activity returns results, if ok, update current speed limit
                // and speed type
                if(resultCode == RESULT_OK) {
                    currentLimit = sharedPreferences.getFloat(getString(R.string.sp_speed_limit), defaultLimit);
                    currentSpeedType = sharedPreferences.getString(getString(R.string.sp_speed_type), defaultSpeedType);
                }
                return;
            }
            case REQ_CODE_SPEECH_REC:{
                if(resultCode==RESULT_OK){
                    ArrayList<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    for (String command : results) {
                        if(!commands.contains(command.toLowerCase())){
                            continue;
                        }
                        else{
                            switch(command){
                                case "start":{
                                    startButton.performClick();
                                    return;
                                }
                                case "stop":{
                                    stopButton.performClick();
                                    return;
                                }
                                case "help":{
                                    helpButton.performClick();
                                    return;
                                }
                                case "history":{
                                    viewViolButton.performClick();
                                    return;
                                }
                            }
                        }
                    }
                    Toast.makeText(getBaseContext(), "Unrecognizable command. Please try again or click on the help button.", Toast.LENGTH_LONG);
                }
            }
        }
    }
    /**
     * Transforms speed according to the type given
     * @param speed speed in m/s
     * @param type  type (e.g m/s, km/h, etc.)
     * @return  Speed transformed according to type
     */
    private float transformSpeed(float speed, String type){
        switch(type){
            case "m/s":
                return speed;
            case "km/h":
                return (speed*3600)/1000;
        }
        return speed;
    }

    private int counter = 0; // Counter to see if the violation will be saved
    private static final int SECS_BETWEEN_VIOLATIONS = 10;
    /**
     * Handles the speed limit violation by inserting it into the database
     * and displaying warning
     * @param location  The location of the violation
     */
    private void HandleSpeedLimitViolation(Location location){
        counter++;
        if(counter < SECS_BETWEEN_VIOLATIONS){
            return;
        }
        else{
            counter = 0;
            if(currentSpeed > currentLimit){
                SpeedLimitViolation violation = new SpeedLimitViolation(location.getLongitude(),
                        location.getLatitude(),
                        transformSpeed(location.getSpeed(), currentSpeedType) + currentSpeedType,
                        new Timestamp(location.getTime()));
                DatabaseHelper.getInstance(getBaseContext()).InsertSpeedLimitViolation(violation);
                Toast.makeText(this, "SPEED LIMIT VIOLATION", Toast.LENGTH_LONG).show();
                ttsWarnings.SpeakSpeedWarning();
            }
        }
    }
}
