package com.example.usman.newawareness.Activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.usman.newawareness.Fence_Receiver;
import com.example.usman.newawareness.Objects.Object_Situation;
import com.example.usman.newawareness.Objects.Time_Class;
import com.example.usman.newawareness.R;
import com.example.usman.newawareness.Objects.Realm_Awareness_Fence;
import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.awareness.fence.AwarenessFence;
import com.google.android.gms.awareness.fence.FenceUpdateRequest;
import com.google.android.gms.awareness.snapshot.WeatherResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.ResultCallbacks;
import com.google.android.gms.common.api.Status;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;

import io.realm.Case;
import io.realm.Realm;

import static android.content.ContentValues.TAG;

/**
 * Created by usman on 3/28/2017.
 */

public class Activity_Fence_Selection extends AppCompatActivity {
    RelativeLayout headphone, weather, location, activity, time;
    TextView tv_headphone, tv_weather, tv_activity, tv_time, tv_time1, tv_location1, tv_action;
    EditText edt_name;
    Button btn_date, btn_time, btn_action;
    GoogleApiClient mGoogleApiClient;
    PendingIntent myPendingIntent;
    Activity_Fences fences;
    Activity_Fence_Create fence_create;
    Realm realm;
    Time_Class timeclass;
    Object_Situation object_situation;
    Fence_Receiver myfencereceiver;
    IntentFilter filter1;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    Double latdouble, longdouble;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_fence_selection);
        timeclass = new Time_Class();
        myfencereceiver = new Fence_Receiver();
        fence_create = new Activity_Fence_Create();
        sharedpreferences = PreferenceManager.getDefaultSharedPreferences(Activity_Fence_Selection.this);
        editor = sharedpreferences.edit();
        realm.init(getApplication());
        realm = Realm.getDefaultInstance();
        fences = new Activity_Fences();

        object_situation = new Object_Situation();
        mGoogleApiClient = new GoogleApiClient.Builder(Activity_Fence_Selection.this)
                .addApi(Awareness.API)
                .build();
        mGoogleApiClient.connect();
        Get_Widgets();
        Value_Setter();

        Intent intent = new Intent("1");
        myPendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        filter1 = new IntentFilter();
        filter1.addAction("1");
        registerReceiver(myfencereceiver, filter1);
        Click_Listeners();

    }

    private void Value_Setter() {

        String area = sharedpreferences.getString("address", "Choose Location");
        String action = sharedpreferences.getString("action", null);
        String latstring = sharedpreferences.getString("latstring", null);
        String longistring = sharedpreferences.getString("longistring", null);
        if (latstring != null && longistring != null) {
            latdouble = Double.parseDouble(latstring);
            longdouble = Double.parseDouble(longistring);
        }

        String headphone = sharedpreferences.getString("headphone", "Choose State");
        String weather = sharedpreferences.getString("weather", "Choose State");
        String activity = sharedpreferences.getString("activity", "Choose State");
        String time = sharedpreferences.getString("time", null);
        String date = sharedpreferences.getString("date", null);
        String situationnames = sharedpreferences.getString("situationname", "");
        int headphonestate = sharedpreferences.getInt("headphone_state", 0);
        int weatherstate = sharedpreferences.getInt("weather_state", 0);
        int activitystate = sharedpreferences.getInt("activity_state", 0);
        int radius = sharedpreferences.getInt("radius", 0);
        String duration = sharedpreferences.getString("hours", null);
        String hours = sharedpreferences.getString("hours", null);
        String minutes = sharedpreferences.getString("minutes", null);
        String appname = sharedpreferences.getString("appname", null);

        object_situation.setMinutes(minutes);
        object_situation.setHours(hours);
        object_situation.setWeather(weatherstate);
        object_situation.setHeadphone(headphonestate);
        object_situation.setActivity(activitystate);
        object_situation.setLocationname(area);
        object_situation.setAction(action);
        object_situation.setLat(latdouble);
        object_situation.setLongi(longdouble);
        object_situation.setRadius(radius);
        object_situation.setHeadphone_txt(headphone);
        object_situation.setWeather_txt(weather);
        object_situation.setActivity_txt(activity);
        object_situation.setAppname(appname);
        timeclass.setTime(time);
        timeclass.setDate(date);
        edt_name.setText(situationnames);
        tv_location1.setText(area);
        tv_action.setText(action);
        tv_headphone.setText(headphone);
        tv_weather.setText(weather);
        tv_activity.setText(activity);
        tv_time.setText(date);
        tv_time1.setText(time);
    }


    private void Get_Widgets() {
        headphone = (RelativeLayout) findViewById(R.id.page_fence_selection_relativelayout_headphone);
        weather = (RelativeLayout) findViewById(R.id.page_fence_selection_relativelayout_weather);
        location = (RelativeLayout) findViewById(R.id.page_fence_selection_relativelayout_location);
        activity = (RelativeLayout) findViewById(R.id.page_fence_selection_relativelayout_activity);
        time = (RelativeLayout) findViewById(R.id.page_fence_selection_relativelayout_time);
        tv_headphone = (TextView) findViewById(R.id.page_fence_selection_tv_headphone1);
        tv_weather = (TextView) findViewById(R.id.page_fence_selection_tv_weather1);
        tv_location1 = (TextView) findViewById(R.id.page_fence_selection_tv_location1);
        tv_activity = (TextView) findViewById(R.id.page_fence_selection_tv_activity1);
        tv_time = (TextView) findViewById(R.id.page_fence_selection_tv_time1);
        btn_date = (Button) findViewById(R.id.page_fence_selection_btn_date);
        btn_time = (Button) findViewById(R.id.page_fence_selection_btn_time);
        tv_time1 = (TextView) findViewById(R.id.page_fence_selection_tv_time2);
        btn_action = (Button) findViewById(R.id.page_fence_selection_btn_action);
        tv_action = (TextView) findViewById(R.id.page_fence_selection_tv_action);
        edt_name = (EditText) findViewById(R.id.page_fence_selection_tv_name1);

    }


    private void Click_Listeners() {
        headphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] items = {"Headphone Plugged In", "Headphone Plugged Out"};

                final AlertDialog.Builder builder = new AlertDialog.Builder(Activity_Fence_Selection.this);
                builder.setTitle("Select Headphone State");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        // Do something with the selection
                        tv_headphone.setText(items[item]);
                        object_situation.setHeadphone(item + 1);
                        object_situation.setHeadphone_txt(items[item].toString());
                        editor.putString("headphone", items[item].toString());
                        editor.putInt("headphone_state", item + 1);
                        editor.apply();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();

            }
        });
        weather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] items = {"Clear", "Cloudy", "Foggy", "Hazy", "Icy", "Rainy", "Snowy", "Stormy", "Windy"};

                AlertDialog.Builder builder = new AlertDialog.Builder(Activity_Fence_Selection.this);
                builder.setTitle("Make your selection");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        // Do something with the selection
                        tv_weather.setText(items[item]);
                        object_situation.setWeather(item);
                        object_situation.setWeather_txt(items[item].toString());
                        editor.putString("weather", items[item].toString());
                        editor.putInt("weather_state", item + 1);
                        editor.apply();

                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
        activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] items = {"In Vehicle", "On Bicycle", "On Foot", "Running", "Still", "Tilting", "Walking"};

                AlertDialog.Builder builder = new AlertDialog.Builder(Activity_Fence_Selection.this);
                builder.setTitle("Make your selection");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        // Do something with the selection
                        tv_activity.setText(items[item]);
                        object_situation.setActivity(item + 1);
                        object_situation.setActivity_txt(items[item].toString());
                        editor.putString("activity", items[item].toString());

                        editor.putInt("activity_state", item + 1);

                        editor.apply();

                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        btn_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int mYear = calendar.get(Calendar.YEAR);
                int mMonth = calendar.get(Calendar.MONTH);
                int mDay = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(Activity_Fence_Selection.this, myDateListener, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        btn_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(Activity_Fence_Selection.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String minute = String.format("%02d", selectedMinute);
                        String hour = String.format("%02d", selectedHour);
                        tv_time1.setText(hour + ":" + minute);
                        timeclass.setTime(hour + ":" + minute);

                        editor.putString("time", hour + ":" + minute);

                        editor.apply();


                    }
                }, hour, minute, true);//Yes 24 hour timeMinuteDays
                mTimePicker.setTitle("Select Time_Class");
                mTimePicker.show();

            }
        });

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_Fence_Selection.this, Activity_Maps.class);
                startActivity(intent);
            }
        });
        btn_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] items = {"Open Application", "Show Notification"};

                AlertDialog.Builder builder = new AlertDialog.Builder(Activity_Fence_Selection.this);
                builder.setTitle("Make your selection");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        if (items[item] == items[0]) {

                            //Toast.makeText(Activity_Fence_Selection.this, "App", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Activity_Fence_Selection.this, Activity_List_Installed_Apps.class);
                            startActivity(intent);
                        }
                        if (items[item] == items[1]) {


                            object_situation.setNotification("notification");
                            tv_action.setText("Notification");
                        }
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();

            }
        });
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            Calendar cal = Calendar.getInstance();
            cal.set(arg1, arg2, arg3);
            DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(Activity_Fence_Selection.this);
            tv_time.setText(dateFormat.format(cal.getTime()));
            timeclass.setDate(dateFormat.format(cal.getTime()));
            int a=arg2+1;
            editor.putString("date", a+"/"+arg3+"/"+arg1);

            editor.apply();
            editor.commit();


        }
    };

    public void Create_Fence(final Realm realm) throws ParseException {
        AwarenessFence andFence = null;
        object_situation.setSituationname(edt_name.getText().toString());
        editor.putString("situationname", object_situation.getSituationname());
        editor.apply();
        if (object_situation.getSituationname().equals("")) {
            Toast.makeText(Activity_Fence_Selection.this, "PLZ GIVE SOME SITUATION NAME", Toast.LENGTH_LONG).show();

        } else {
            if (object_situation.getAction() == null && object_situation.getNotification() == null) {
                Toast.makeText(Activity_Fence_Selection.this, "PLZ CHOOSE SOME RESPONCE", Toast.LENGTH_LONG).show();
            } else {
                Long count = realm.where(Realm_Awareness_Fence.class).
                        equalTo("situationname", object_situation.getSituationname(), Case.INSENSITIVE).count();
                if (count != 0) {
                    Toast.makeText(Activity_Fence_Selection.this, "Situation Name Already Exists", Toast.LENGTH_LONG).show();

                } else {
                    if (object_situation.getHeadphone_txt() == "Choose State" && object_situation.getWeather_txt() == "Choose State"
                            && object_situation.getLat() == null && object_situation.getActivity_txt() == "Choose State"
                            && object_situation.getTime() == null && timeclass.getDate() == null && timeclass.getTime() == null) {
                        Toast.makeText(Activity_Fence_Selection.this, "Null Fence", Toast.LENGTH_LONG).show();


                    } else if (object_situation.getHeadphone_txt() == "Choose State" && object_situation.getWeather_txt() != "Choose State"
                            && object_situation.getLocationname() == "Choose Location" && object_situation.getActivity_txt() == "Choose State"
                            && object_situation.getTime() == null && timeclass.getDate() == null && timeclass.getTime() == null) {
                        Toast.makeText(Activity_Fence_Selection.this, "Please Choose one more situation", Toast.LENGTH_LONG).show();
                        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        Awareness.SnapshotApi.getWeather(mGoogleApiClient)
                                .setResultCallback(new ResultCallback<WeatherResult>() {
                                    @Override
                                    public void onResult(@NonNull WeatherResult weatherResult) {
                                        if (weatherResult.getStatus().isSuccess())
                                        {
                                        }
                                    }
                                });

                    } else {
                        String time_value = fence_create.Time_Create(timeclass);
                        andFence = fence_create.Create_Fence(object_situation, timeclass);
                        if (andFence == null && object_situation.getWeather_txt() == null) {
                            Toast.makeText(Activity_Fence_Selection.this, "Null fence", Toast.LENGTH_LONG).show();
                        } else
                            {
                            if (andFence == null && object_situation.getWeather_txt() != null)
                            {
                                Toast.makeText(Activity_Fence_Selection.this, "Please Choose one more situation", Toast.LENGTH_LONG).show();

                            } else {
                                registerReceiver(myfencereceiver, filter1);
                                Situation_Create(andFence, object_situation, timeclass);
                            }

                        }
                    }

                }
            }
        }
    }
    public void Situation_Create(AwarenessFence andFences, final Object_Situation object_situation, final Time_Class timeclass)
    {
        Awareness.FenceApi.updateFences(
                mGoogleApiClient,
                new FenceUpdateRequest.Builder()
                        .addFence(object_situation.getSituationname(), andFences, myPendingIntent)
                        .build())
                .setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        if (status.isSuccess()) {
                            //Log.i(TAG, "Fence was successfully registered.");
                            realm=Realm.getDefaultInstance();
                            Toast.makeText(Activity_Fence_Selection.this, "Fence was successfully registered", Toast.LENGTH_LONG).show();
                            realm.executeTransaction(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    Realm_Awareness_Fence fence = realm.createObject(Realm_Awareness_Fence.class);
                                    fence.setSituationname(object_situation.getSituationname());

                                    if (object_situation.getHeadphone_txt() != null) {
                                        fence.setHeadphone(object_situation.getHeadphone_txt());
                                    }
                                    if (object_situation.getHeadphone() != 0) {
                                        fence.setHeadphone_txt(object_situation.getHeadphone());
                                    }
                                    if (object_situation.getWeather() != 0) {
                                        fence.setWeather_txt(object_situation.getWeather());
                                    }

                                    if (object_situation.getWeather_txt() != null) {
                                        fence.setWeather(object_situation.getWeather_txt());

                                    }
                                    if (object_situation.getActivity_txt() != null) {
                                        fence.setActivity(object_situation.getActivity_txt());
                                    }
                                    if (object_situation.getActivity() != 0) {
                                        fence.setActivity_txt(object_situation.getActivity());
                                    }
                                    if (object_situation.getLat() != null) {
                                        fence.setLat(object_situation.getLat());
                                        fence.setLongi(object_situation.getLongi());
                                        fence.setLocation(object_situation.getLocationname());

                                    }
                                    if (timeclass.getTime() != null) {
                                        fence.setTime(timeclass.getTime());

                                    }
                                    if (timeclass.getDate() != null) {
                                        fence.setDate(timeclass.getDate());

                                    }
                                    fence.setMinutes(object_situation.getMinutes());
                                    fence.setHours(object_situation.getHours());
                                    if (object_situation.getNotification() != null) {
                                        fence.setNotification(object_situation.getNotification());
                                    }
                                    else
                                    {
                                        fence.setAppname(object_situation.getAppname());
                                        fence.setAppopen(object_situation.getAction());

                                    }
                                    fence.setActive(true);
                                    editor.clear();
                                    editor.commit();

                                    Intent intent=new Intent(Activity_Fence_Selection.this,Activity_Main.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


                                    // Add new Flag to start new Activity
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                }
                            });
                        } else {
                            Log.e(TAG, "Fence could not be registered: " + status);
                            //Toast.makeText(Activity_Fence_Selection.this, "Fence was not registered", Toast.LENGTH_LONG).show();


                        }
                    }
                });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mybutton:
                try {
                    Create_Fence(realm);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    public void registerFence(final String fenceKey, final AwarenessFence fence,Context context) {
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addApi(Awareness.API)
                .build();
        mGoogleApiClient.connect();
        Intent intent = new Intent("1");
        IntentFilter filter1 = new IntentFilter();

        myPendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        filter1.addAction("1");
        Fence_Receiver myfencereceiver = new Fence_Receiver();

        context.registerReceiver(myfencereceiver, filter1);

        Awareness.FenceApi.updateFences(
                mGoogleApiClient,
                new FenceUpdateRequest.Builder()
                        .addFence(fenceKey, fence, myPendingIntent)
                        .build())
                .setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        if(status.isSuccess()) {
                            Log.i(TAG, "Fence was successfully registered.");
                            //queryFence(fenceKey);
                        } else {
                            Log.e(TAG, "Fence could not be registered: " + status);
                        }
                    }
                });
    }
    public void unregisterFence(final String fenceKey,Context context) {
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addApi(Awareness.API)
                .build();
        mGoogleApiClient.connect();
        Awareness.FenceApi.updateFences(
                mGoogleApiClient,
                new FenceUpdateRequest.Builder()
                        .removeFence(fenceKey)
                        .build()).setResultCallback(new ResultCallbacks<Status>() {
            @Override
            public void onSuccess(@NonNull Status status) {
                Log.i(TAG, "Fence " + fenceKey + " successfully removed.");
            }

            @Override
            public void onFailure(@NonNull Status status) {
                Log.i(TAG, "Fence " + fenceKey + " could NOT be removed.");
            }
        });
    }
    @Override
    protected void onPause() {
        super.onPause();

        unregisterReceiver(myfencereceiver);
    }
}
