package com.example.speedlimitapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ViewViolations extends AppCompatActivity {
    public static final int ALL_MODE = 0;
    public static final int WEEK_MODE = 1;


    // Mode for all-time violations or week's violations
    private int mode;

    // Declaring UI components
    Button viewOnMapButton;
    TextView title;
    TextView data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_violations);

        // Getting mode from extras
        mode = getIntent().getIntExtra("mode", ALL_MODE);

        // Initializing UI components
        viewOnMapButton = findViewById(R.id.view_violations_on_map_button);
        title = findViewById(R.id.view_violations_on_map_title);
        data = findViewById(R.id.view_violations_text);

        setTitle();
        setUpButtonListeners();
        showData();
    }

    /**
     *  Sets the title depending on mode
     */
    private void setTitle(){
        switch(mode){
            case ALL_MODE:{
                title.setText(R.string.all_time_viol_title);
                return;
            }
            case WEEK_MODE:{
                title.setText(R.string.week_viol_title);
                return;
            }
        }
    }

    private void setUpButtonListeners(){
        // TODO implement
    }

    /**
     * Shows data depending on mode
     */
    private void showData(){
        StringBuilder dataToShow = new StringBuilder();
        switch(mode){
            case ALL_MODE:{
                ArrayList<SpeedLimitViolation> violations = DatabaseHelper.getInstance(this).getAllTimeViolations();
                for(SpeedLimitViolation violation : violations){
                    dataToShow.append("Longitude: " + violation.getLongitudeAsString() + "\n");
                    dataToShow.append("Latitude: " + violation.getLatitudeAsString() + "\n");
                    dataToShow.append("Speed: " + violation.getSpeedAsString() + "\n");
                    dataToShow.append("Time: " + violation.getTimestampAsString() + "\n");
                    dataToShow.append("============\n");
                }
                if(dataToShow.toString().equals(""))
                    Toast.makeText(getBaseContext(), "No violations recorded!", Toast.LENGTH_SHORT).show();
                data.setText(dataToShow.toString());
                return;
            }
            case WEEK_MODE:{
                // TODO implement
                return;
            }
        }
    }
}