package com.example.speedlimitapp;

import java.sql.Timestamp;

public class SpeedLimitViolation {

    private double longitude;
    private double latitude;
    private String speed;
    private String timestamp;

    /**
     * Constructs violation object with arguments as strings from the database
     * @param longitude longitude of the violation
     * @param latitude  latitude of the violation
     * @param speed     speed of the violation
     * @param timestamp timestamp of the violation
     */
    public SpeedLimitViolation(String longitude, String latitude, String speed, String timestamp){
        this.longitude = Double.parseDouble(longitude);
        this.latitude = Double.parseDouble(latitude);
        this.speed = speed;
        this.timestamp = timestamp;
    }

    /**
     * Constructs violation object with data from the Location of the app
     * @param longitude longitude of the violation
     * @param latitude  latitude of the violation
     * @param speed     speed of the violation
     * @param timestamp timestamp of the violation
     */
    public SpeedLimitViolation(double longitude, double latitude, String speed, Timestamp timestamp){
        this.longitude = longitude;
        this.latitude = latitude;
        this.speed = speed;
        this.timestamp = timestamp.toString();
    }
    //================================================
    // Simple getters
    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getSpeed() {
        return speed;
    }

    public String getTimestamp() {
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
