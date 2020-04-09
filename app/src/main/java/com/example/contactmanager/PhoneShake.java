package com.example.contactmanager;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

public class PhoneShake implements SensorEventListener {

    private Sensor accelerometer;
    private SensorManager sm;
    private OnShakeListener mShakeListener;
    private long mPreviousShakeTime;

    private static final float SHAKE_THRESHOLD = 2.5F;
    private static final int SHAKE_RESET_TIME = 800;

    private static final String TAG = "PhoneShake";



    public void setOnShakeListener(OnShakeListener listener) {
        this.mShakeListener = listener;
    }

    public interface OnShakeListener {
        public void onShake();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        // Collect sensor values
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
