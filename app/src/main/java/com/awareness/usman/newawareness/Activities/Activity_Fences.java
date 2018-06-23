package com.awareness.usman.newawareness.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.awareness.fence.AwarenessFence;
import com.google.android.gms.awareness.fence.DetectedActivityFence;
import com.google.android.gms.awareness.fence.HeadphoneFence;
import com.google.android.gms.awareness.fence.LocationFence;
import com.google.android.gms.awareness.fence.TimeFence;
import com.google.android.gms.awareness.state.HeadphoneState;

import java.text.ParseException;

/**
 * Created by usman on 4/2/2017.
 */

public class Activity_Fences extends Activity {

    Activity_Fence_Create fence_create;
    public AwarenessFence Headphone(int headphone) {
        AwarenessFence headphoneFence = null;
        if (headphone != 0) {
            if (headphone == 1) {
                headphoneFence = HeadphoneFence.during(HeadphoneState.PLUGGED_IN);
            } else {
                headphoneFence = HeadphoneFence.during(HeadphoneState.UNPLUGGED);
            }
        }
        return headphoneFence;
    }

    public AwarenessFence Activity(int activity) {
        AwarenessFence activityfence = null;
        if (activity != 0) {
            if (activity == 1) {
                activityfence = DetectedActivityFence.during(DetectedActivityFence.IN_VEHICLE);

            }
            if (activity == 2) {
                activityfence = DetectedActivityFence.during(DetectedActivityFence.ON_BICYCLE);

            }
            if (activity == 3) {
                activityfence = DetectedActivityFence.during(DetectedActivityFence.ON_FOOT);

            }
            if (activity == 4) {
                activityfence = DetectedActivityFence.during(DetectedActivityFence.RUNNING);

            }
            if (activity == 5) {
                activityfence = DetectedActivityFence.during(DetectedActivityFence.STILL);

            }
            if (activity == 6) {
                activityfence = DetectedActivityFence.during(DetectedActivityFence.TILTING);

            }
            if (activity == 7) {
                activityfence = DetectedActivityFence.during(DetectedActivityFence.WALKING);

            }
        }
        return activityfence;
    }

    public AwarenessFence Location(Double lat, Double longi, Double locationradius,String hours,String minutes) throws ParseException
    {
        try{
            if (ActivityCompat.checkSelfPermission(Activity_Fences.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            }
        }catch (Exception e)
        {

        }
        fence_create=new Activity_Fence_Create();

        AwarenessFence inLocationFence = LocationFence.in(
                lat, longi, locationradius,fence_create.Create_Minute(hours,minutes)
        );
        return inLocationFence;
    }

    public AwarenessFence Time(Long time, Long time1) {
        AwarenessFence timefence;
        timefence= TimeFence.inInterval(time,time1);
   return timefence; }

    }