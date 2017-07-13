package com.task.eliaddegabli.moveo_task;

import io.realm.Realm;
import io.realm.RealmObject;

/**
 * Created by Eliad Degabli on 12/07/2017.
 */

public class Gyroscope extends RealmObject {

    private Double x;
    private Double y;
    private Double z;


    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }

    public Double getZ() {
        return z;
    }

    public void setZ(Double z) {
        this.z = z;
    }

    @Override
    public String toString() {
        return
                x +
                        "," + y +
                        "," + z ;
    }


}
