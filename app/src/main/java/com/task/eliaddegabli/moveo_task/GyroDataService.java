package com.task.eliaddegabli.moveo_task;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Eliad Degabli on 12/07/2017.
 */

public class GyroDataService extends IntentService implements SensorEventListener {

    final String TAG = "INTENT_SERVICE";
    private final int NUM_OF_RECORD=500;
    private SensorManager sensorManager;
    private Sensor gyroscopeSensor;
    private int sizeOfDB;
    private Double x;
    private Double y;
    private Double z;
    private Timer mBackGroundTimer;

    public GyroDataService() {
        super("E");
        this.mBackGroundTimer=new Timer();
    }

    @Override
    public void onStart(@Nullable Intent intent, int startId) {
        super.onStart(intent, startId);

        sensorManager=(SensorManager) getSystemService(SENSOR_SERVICE);;
        gyroscopeSensor=sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this,
                gyroscopeSensor,
                SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        mBackGroundTimer.schedule(new TimerTask()
        {
            public void run() {
                Realm realm = Realm.getDefaultInstance();
                try {
                    if (!(sizeOfDB < NUM_OF_RECORD)) { // if we have more than 500 records in DB delet the first one.
                        deleteFromDB();
                        sizeOfDB--;
                    }
                    realm.beginTransaction(); // adding a data sample to Realm.


                    Gyroscope gyro = realm.createObject(Gyroscope.class);
                    gyro.setX(x);
                    gyro.setY(y);
                    gyro.setZ(z);
                    Log.v(TAG, "Every 300 milliseconds");
                    sizeOfDB++;
                    realm.commitTransaction();

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        },0,300);

    }






    // deleteFromDB - Delete from realm DB the first record of gyro data.
    private void deleteFromDB(){
        Realm realm= Realm.getDefaultInstance();
        final RealmResults<Gyroscope> results = realm.where(Gyroscope.class).findAll();

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                // remove single match
                results.deleteFirstFromRealm();
                //results.deleteLastFromRealm();

                // remove a single object
               /* Student student = results.get(5);
                student.deleteFromRealm();*/

                // Delete all matches
                //results.deleteAllFromRealm();
            }
        });
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        float x1= event.values[0];
        float y1= event.values[1];
        float z1= event.values[2];
        x= Double.parseDouble(String.valueOf(x1));
        y= Double.parseDouble(String.valueOf(y1));
        z= Double.parseDouble(String.valueOf(z1));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
