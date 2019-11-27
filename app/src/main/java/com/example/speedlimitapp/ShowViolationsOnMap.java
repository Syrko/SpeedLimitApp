package com.example.speedlimitapp;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class ShowViolationsOnMap extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private int mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_violations_on_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Get mode from extras -- default all time mode
        mode = getIntent().getIntExtra("mode", 0);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Get violations depending on mode
        ArrayList<SpeedLimitViolation> violations;
        switch (mode){
            // All time mode
            case 0:
            {
                violations = DatabaseHelper.getInstance(getBaseContext()).getAllTimeViolations();
                break;
            }
            // This day mode
            case 1:
            {
                violations = DatabaseHelper.getInstance(getBaseContext()).getTodayViolations();
                break;
            }
            // In case of error
            default:
            {
                Toast.makeText(getBaseContext(), "Error loading map!", Toast.LENGTH_LONG).show();
                finish();
                return;
            }
        }

        // Shows error if no violations in database
        if(violations.isEmpty()){
            Toast.makeText(getBaseContext(), "Alert -- No violations to show on map!", Toast.LENGTH_LONG).show();
            return;
        }

        // Placing markers on map
        LatLng temp = null;
        for(SpeedLimitViolation violation : violations){
            temp = new LatLng(violation.getLatitude(), violation.getLongitude());
            String title = violation.getSpeedAsString() + " | " + violation.getTimestampAsString().substring(0,10);
            mMap.addMarker(new MarkerOptions().position(temp).title(title));
        }

        // Zooming in lst marker position
        if(temp!=null)
            mMap.moveCamera(CameraUpdateFactory.newLatLng(temp));
    }
}
