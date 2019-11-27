package com.example.speedlimitapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Activity for a menu used to choose mode of viewing.
 * The user can choose between seeing all the violations in the database or
 * just the violations of the day.
 */
public class ViewViolationsMenu extends AppCompatActivity {

    // Declaring UI components
    Button allTime;
    Button thisWeek;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_violations_menu);

        // Initializing UI components
        allTime = findViewById(R.id.all_time_button);
        thisWeek = findViewById(R.id.this_week_button);

        setUpButtonListeners();
    }

    /**
     * Sets up the required button listeners
     */
    private void setUpButtonListeners(){
        allTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewViol = new Intent(getBaseContext(), ViewViolations.class);
                viewViol.putExtra("mode", ViewViolations.ALL_MODE);
                startActivity(viewViol);
            }
        });

        thisWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewViol = new Intent(getBaseContext(), ViewViolations.class);
                viewViol.putExtra("mode", ViewViolations.DAY_MODE);
                startActivity(viewViol);
            }
        });
    }
}
