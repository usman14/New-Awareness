package com.example.usman.newawareness.Activities;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.usman.newawareness.Fence_Receiver;
import com.example.usman.newawareness.Objects.Object_Situation;
import com.example.usman.newawareness.Objects.Time_Class;
import com.example.usman.newawareness.Objects.Realm_Awareness_Fence;
import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.awareness.fence.AwarenessFence;
import com.google.android.gms.common.api.GoogleApiClient;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by usman on 4/3/2017.
 */

public class Activity_Fence_Create extends AppCompatActivity {

    Activity_Fences fences=new Activity_Fences();
    GoogleApiClient mGoogleApiClient;
    private PendingIntent myPendingIntent;
    Fence_Receiver myfencereceiver;
    IntentFilter filter1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGoogleApiClient = new GoogleApiClient.Builder(Activity_Fence_Create.this)
                .addApi(Awareness.API)
                .build();
        mGoogleApiClient.connect();
        Intent intent = new Intent("2");
        myPendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        filter1 = new IntentFilter();
        filter1.addAction("2");
        myfencereceiver = new Fence_Receiver();
        registerReceiver(myfencereceiver, filter1);
    }

    public AwarenessFence Create_Fence(Object_Situation object_situation, Time_Class timeclass) throws ParseException {
        AwarenessFence andFence=null;
        Long datelong = null;
        Long datelongnew=null;
         ArrayList<AwarenessFence> addresses = new ArrayList<AwarenessFence>();


        String combo=timeclass.getDate()+"/"+timeclass.getTime();
                DateFormat readFormat = new SimpleDateFormat( "MM/dd/yyyy/hh:mm");
                DateFormat writeFormat = new SimpleDateFormat( "yyyy-MM-dd HH:mm");
                Date date = null;
        if(!combo.equals("null/null"))
        {
            try {
                    date = readFormat.parse( combo );
                } catch ( ParseException e ) {
                    e.printStackTrace();
                }

                String formattedDate = "";
                if( date != null ) {
                    formattedDate = writeFormat.format( date );
                }
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
             if(formattedDate!=null)
                {
                    datelong= date.getTime();
                    Date newdate=dateFormat.parse(formattedDate);
                    Calendar c=Calendar.getInstance();
                    c.setTime(newdate);
                    c.add(c.MINUTE,5);
                    String newTime = dateFormat.format(c.getTime());
                    datelongnew= (dateFormat.parse(newTime).getTime());

                }
        }

                AwarenessFence time = null;
                AwarenessFence headphone = null;
                AwarenessFence activity = null;
                AwarenessFence location = null;
                if(datelong!=null)
                {
                    time = fences.Time(datelong,datelongnew);

                }
                if (object_situation.getHeadphone() != 0)
                {
                    headphone = fences.Headphone(object_situation.getHeadphone());
                }
                if (object_situation.getActivity() != 0)
                {
                    activity = fences.Activity(object_situation.getActivity());
                }
        if (object_situation.getActivity() != 0)
        {
            activity = fences.Activity(object_situation.getActivity());
        }
                if (object_situation.getLat() != null)
                {
                    location= fences.Location(object_situation.getLat(),object_situation.getLongi(),Double.valueOf(object_situation.getRadius()),object_situation.getHours(),object_situation.getMinutes());
                }

               if(time!=null)
               {
                   addresses.add(time);
               }
        if(headphone!=null)
        {
            addresses.add(headphone);
        }
        if(location!=null)
        {
            addresses.add(location);
        }
        if(activity!=null)
        {
            addresses.add(activity);
        }
        andFence=AwarenessFence.and(addresses);
        return andFence;}

    public String Time_Create(Time_Class timeclass) throws ParseException {
        String finaltime = null;
        String combo = timeclass.getDate() + "/" + timeclass.getTime();
        if (!combo.equals("null/null")) {
            if (timeclass.getDate() == null) {
                String date;
                Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                date = month + "/" + day + "/" + year;
                timeclass.setDate(date);

            }
            if (timeclass.getTime() == null) {
                String time;
                Calendar c = Calendar.getInstance();

                String minute = String.format("%02d", c.get(Calendar.MINUTE));
                String hour = String.format("%02d", c.get(Calendar.HOUR_OF_DAY));
                time = minute + ":" + hour;
                timeclass.setTime(time);

            }
            DateFormat readFormat = new SimpleDateFormat("MM/dd/yyyy/hh:mm");
            DateFormat writeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date date = null;
            String combo1 = timeclass.getDate() + "/" + timeclass.getTime();
            try {

                date = readFormat.parse(combo1);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            String formattedDate = "";
            if (date != null) {
                formattedDate = writeFormat.format(date);
            }
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy/hh:mm");
            if (formattedDate == null) {

                return null;
            } else {
                finaltime = String.valueOf(dateFormat.parse(combo1).getTime());
            }
        }
        return finaltime;
    }

    public Long Create_Minute(String hours,String minutes) throws ParseException {

        long time = Long.valueOf(hours) * 60;
        long total_time = Long.valueOf(minutes + time) * 6000;

        return total_time;
    }

    public AwarenessFence Upgrade_Situation(Realm_Awareness_Fence fence) throws ParseException {

        Object_Situation object_situation=new Object_Situation();
        object_situation.setSituationname(fence.getSituationname());
        Time_Class timeclass=new Time_Class();

        if (fence.getSituationname() != null) {
            object_situation.setSituationname(fence.getSituationname());
        }

        if (fence.getHeadphone() != null) {
            object_situation.setHeadphone_txt(fence.getHeadphone());
        }
        if (fence.getHeadphone_txt() != 0) {
            object_situation.setHeadphone(fence.getHeadphone_txt());
        }
        if (fence.getWeather_txt() != 0) {
            object_situation.setWeather_txt(fence.getWeather());
        }
        if (fence.getWeather() != null) {
            object_situation.setWeather_txt(fence.getWeather());
        }
        if (fence.getActivity_txt() != 0) {
            object_situation.setActivity(fence.getActivity_txt());
        }

        if (fence.getActivity() != null) {
            object_situation.setActivity_txt((fence.getActivity()));
        }
        if (fence.getLat() != null) {
            object_situation.setLat(fence.getLat());
            object_situation.setLongi(fence.getLongi());
            object_situation.setLocationname(fence.getLocation());
            object_situation.setRadius(fence.getRadius());
            object_situation.setMinutes(fence.getMinutes());
            object_situation.setHours(fence.getHours());
        }
        if (fence.getTime() != null) {
            timeclass.setTime(fence.getTime());
        }
        if (fence.getDate() != null) {
            timeclass.setDate(fence.getDate());

        }

        if (fence.getNotification() != null) {
            object_situation.setNotification(fence.getNotification());
        }
        else
        {
            object_situation.setAction(fence.getAppopen());
            object_situation.setAppname(fence.getAppname());
        }
        object_situation.setActive(true);

        AwarenessFence fence_final=Create_Fence(object_situation,timeclass);
    return fence_final;
    }
     }
