package com.example.speedlimitapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Simple activity showing some helpful text for the user and providing a button that link to
 * the project's GitHub page
 */
public class Help extends AppCompatActivity {

    // GitHub link of the project
    private static final String URL = "https://github.com/Syrko/SpeedLimitApp";

    // Declare UI components
    TextView helpText;
    Button goToSite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        // Initializing UI components
        helpText = findViewById(R.id.help_text);
        goToSite = findViewById(R.id.go_to_site_button);

        setHelpText();
        setUpButtonListeners();
    }

    /**
     * Displays the wall of helpful text
     */
    private void setHelpText(){
        String wallOfText = "\n" +
                "   This is a mobile application compatible with android 7.1+ and " +
                "developed in Android Studio with Java.\n\n" +
                "   Users can enable location tracking and get notified in case they violate " +
                "the speed limit. This speed limit can be set by the user in the app settings.\n\n" +
                "   Violation data are stored in the app's database and can be viewed by clicking " +
                "the relevant button.\n\n" +
                "   For the basic operations in the main menu voice commands are available! Just " +
                "say: 'Start', 'Stop', 'Help' or 'History'.\n\n" +
                "   In addition users may view their violations on a map.\n\n" +
                "For the source code click on the button below!\n\n" +
                "\nKonstantinos-Zois Syrios (2019)\n";
        helpText.setText(wallOfText);
    }

    /**
     * Sets up the required button listeners
     */
    private void setUpButtonListeners(){
        goToSite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(URL));
                startActivity(browserIntent);
            }
        });
    }
}
