/* Contact Manager Program
*
*  A simple android app that mimics the functionality of a contact manager
*  in either Android or iOS phones. Users can view their list of contacts
*  sorted in alphabetical order by last name. They may also add, edit,
*  and remove a contact from the contact list when a contact is tapped on.
*
*  When adding a contact, users must add the contacts first name, last name,
*  phone number, date of birth, and date of first contact.
*
*  Written by Supachai Main for CS4301.002, ContactManager Part.1 , starting March 9th, 2020.
        NetID: sxm163830 */

package com.example.contactmanager;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

/*This class detects phone shakes by the user.
* Intensity of the phone shake can be adjusted here.*/

public class PhoneShake implements SensorEventListener {

    private static final String TAG = "PhoneShake";

    private OnShakeListener mShakeListener;
    private long mPreviousShakeTime;

    private static final float SHAKE_THRESHOLD = 2.5F;
    private static final int SHAKE_RESET_TIME = 800;

    public void setOnShakeListener(OnShakeListener listener) {
        this.mShakeListener = listener;
    }

    public interface OnShakeListener {
        public void onShake();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        // grab sensor values
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        // Get current time
        long now = System.currentTimeMillis();

        // Calc acceleration
        double acceleration = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) +
                Math.pow(z, 2)) - SensorManager.GRAVITY_EARTH;

        // Log.d(TAG, "onSensorChanged: Acceleration ------>  ");

        // Return if user didn't wait long enough between shakes
        if ((now - mPreviousShakeTime) > SHAKE_RESET_TIME) {
            return;
        }

        if (acceleration > SHAKE_THRESHOLD) {
            // Shake detected
            Log.d(TAG, "onSensorChanged: Phone shake detected");
            //Log.d(TAG, "onSensorChanged: FInally a shake...smh");
            mPreviousShakeTime = now;
            mShakeListener.onShake();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Not using
    }
}
