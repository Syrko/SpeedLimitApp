package com.example.speedlimitapp;

import java.sql.Timestamp;

public class SpeedLimitViolation {

    private float longitude;
    private float latitude;
    private String speed;
    private Timestamp timestamp;

    /**
     * Constructs violation object with arguments as strings from the database
     * @param longitude longitude of the violation
     * @param latitude  latitude of the violation
     * @param speed     speed of the violation
     * @param timestamp timestamp of the violation
     */
    public SpeedLimitViolation(String longitude, String latitude, String speed, String timestamp){
        this.longitude = Float.parseFloat(longitude);
        this.latitude = Float.parseFloat(latitude);
        this.speed = speed;
        this.timestamp = new Timestamp(Long.parseLong(timestamp));
    }

    /**
     * Constructs violation object with data from the Location of the app
     * @param longitude longitude of the violation
     * @param latitude  latitude of the violation
     * @param speed     speed of the violation
     * @param timestamp timestamp of the violation
     */
    public SpeedLimitViolation(float longitude, float latitude, String speed, Timestamp timestamp){
        this.longitude = longitude;
        this.latitude = latitude;
        this.speed = speed;
        this.timestamp = timestamp;
    }
    //================================================
    // Simple getters
    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public String getSpeed() {
        return speed;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }
    //================================================
    // Getters returning strings
    public String getLatitudeAsString() {
        return String.valueOf(latitude);
    }

    public String getLongitudeAsString() {
        return String.valueOf(longitude);
    }

    public String getSpeedAsString() {
        return speed;
    }

    public String getTimestampAsString() {
        return timestamp.toString();
    }
}
