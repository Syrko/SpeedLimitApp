package com.example.speedlimitapp;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public final class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Speed_Limit_Violations_Db";
    private static final String VIOLATIONS_TABLE ="Violations";
    private static final int DATABASE_VERSION = 1;


    private static DatabaseHelper instance = null;
    private DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized DatabaseHelper getInstance(Context context){
        if(instance == null){
            instance = new DatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        CreateViolationsTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     * Creates the speed limit violations table
     * @param db    The database
     */
    private void CreateViolationsTable(SQLiteDatabase db){
        String sqlQuery = "CREATE TABLE IF NOT EXISTS " + VIOLATIONS_TABLE +" (" +
                            "longitude TEXT NOT NULL, " +
                            "latitude TEXT NOT NULL, " +
                            "speed TEXT NOT NULL, " +
                            "timestamp TEXT NOT NULL);";
        db.execSQL(sqlQuery);
    }

    /**
     * Inserts a violation entry in the speed limit violations table
     * @param violation Violation object to be inserted
     */
    public void InsertSpeedLimitViolation(SpeedLimitViolation violation){
        SQLiteDatabase db = getWritableDatabase();

        String sqlQuery = "INSERT INTO '%s'(longitude, latitude, speed, timestamp)" +
                            "VALUES('%s', '%s', '%s', '%s');";
        sqlQuery = String.format(sqlQuery, VIOLATIONS_TABLE, violation.getLongitudeAsString(), violation.getLatitudeAsString(),
                                    violation.getSpeedAsString(), violation.getTimestampAsString());

        db.beginTransaction();
        try{
            db.execSQL(sqlQuery);
            db.setTransactionSuccessful();
        }
        catch(SQLException e){
           e.printStackTrace();
        }
        finally {
            db.endTransaction();
        }
    }

    /**
     * Gets all the violations of the user from the database
     * @return  ArrayList with objects of SpeedLimitViolation
     */
    public ArrayList<SpeedLimitViolation> getAllTimeViolations(){
        // Initializing array list to be returned
        ArrayList<SpeedLimitViolation> violations = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        String sqlQuery = "SELECT * FROM '%s';";
        sqlQuery = String.format(sqlQuery, VIOLATIONS_TABLE);

        db.beginTransaction();
        try{
            Cursor result = db.rawQuery(sqlQuery, null);

            if(result != null){
                result.moveToFirst();
                do{
                    violations.add(new SpeedLimitViolation(result.getString(0),
                                                           result.getString(1),
                                                           result.getString(2),
                                                           result.getString(3)));
                }while(result.moveToNext());
            }
            db.setTransactionSuccessful();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally {
            db.endTransaction();
        }
        return violations;
    }
}
