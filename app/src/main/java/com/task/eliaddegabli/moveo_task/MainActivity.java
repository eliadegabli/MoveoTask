package com.task.eliaddegabli.moveo_task;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity  {

    final String TAG = "MAIN_THREADS";
    boolean mStopHandler = false;
    Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        deleteFromDB();
        Intent intent=new  Intent(getApplicationContext(),GyroDataService.class);
        startService(intent);


        mHandler=new Handler();
        Runnable runnable = new Runnable() {

            @Override
            public void run() {
                readAndShow();
                if (!mStopHandler) {

                    mHandler.postDelayed(this,300);
                }
            }
        };

        mHandler.post(runnable);

    }


    // readAndShow - read from realm DB the last record of gyro data and show different color on screen.
    public void readAndShow() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Gyroscope> guests = realm.where(Gyroscope.class).findAll();

        realm.beginTransaction();
        Log.v(TAG ,"Size of data:" +  guests.size());
        if(!(guests.size()==0)) {
            String s = guests.last().toString();

            if (!s.equals("null,null,null")) {
                String part[] = s.split(",");
                Double part1 = Double.parseDouble(part[0]);
                Double part2 = Double.parseDouble(part[1]);
                Double part3 = Double.parseDouble(part[2]);
                Log.v(TAG,"X= " + part1 + ", Y= " + part2 + ", Z= " + part3);
                if (part3 < 8.0 && part2 > 4.0) {
                    getWindow().getDecorView().setBackgroundColor(Color.GREEN);
                } else if (part3 < -5.0) {
                    getWindow().getDecorView().setBackgroundColor(Color.RED);
                } else if (part3 > 5.0)
                    getWindow().getDecorView().setBackgroundColor(Color.BLUE);
            }
        }
        realm.commitTransaction();
    }


    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        deleteFromDB();
    }

    private void deleteFromDB(){
        Realm realm= Realm.getDefaultInstance();
        final RealmResults<Gyroscope> results = realm.where(Gyroscope.class).findAll();

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                // remove single match
                //results.deleteFirstFromRealm();
                //results.deleteLastFromRealm();

                // remove a single object
               /* Student student = results.get(5);
                student.deleteFromRealm();*/

                // Delete all matches
                results.deleteAllFromRealm();
            }
        });
    }

}


