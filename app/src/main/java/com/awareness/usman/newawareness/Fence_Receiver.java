package com.awareness.usman.newawareness;

import android.Manifest;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.awareness.usman.newawareness.Objects.Realm_Awareness_Fence;
import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.awareness.fence.FenceState;
import com.google.android.gms.awareness.snapshot.WeatherResult;
import com.google.android.gms.awareness.state.Weather;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;

import java.util.Random;

import io.realm.Realm;

import static android.content.ContentValues.TAG;

/**
 * Created by usman on 4/9/2017.
 */

public class Fence_Receiver extends BroadcastReceiver {
    Realm realm;
    private PackageManager packageManager = null;

    @Override
    public void onReceive(final Context context, Intent intent) {
        packageManager=context.getPackageManager();
        final GoogleApiClient[] mGoogleApiClient = {new GoogleApiClient.Builder(context)
                .addApi(Awareness.API)
                .build()};
        mGoogleApiClient[0].connect();
        FenceState fenceState = FenceState.extract(intent);

        if (TextUtils.equals(fenceState.getFenceKey(), fenceState.getFenceKey())) {
            realm.init(context);
            realm=Realm.getDefaultInstance();

            switch (fenceState.getCurrentState()) {
                case FenceState.TRUE:
                    final Realm_Awareness_Fence fence = realm.where(Realm_Awareness_Fence.class).equalTo("situationname", fenceState.getFenceKey()).findFirst();
                    final Boolean[] state = {false};
                    String key=fenceState.getFenceKey();
                    if(fence!=null && fence.getActive()) {
                        if (!fence.getWeather().equals("Choose State")) {
                            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                return;
                            }
                            Awareness.SnapshotApi.getWeather(mGoogleApiClient[0])
                                    .setResultCallback(new ResultCallback<WeatherResult>() {
                                        @Override
                                        public void onResult(@NonNull WeatherResult weatherResult) {
                                            if (!weatherResult.getStatus().isSuccess()) {
                                                Log.e(TAG, "Could not get weather.");
                                                return;
                                            }
                                            Weather weather = weatherResult.getWeather();

                                            int[] as = weather.getConditions();
                                            int a = as[0];
                                            int b = fence.getWeather_txt() + 1;
                                            if (b == a) {
                                                state[0] = false;
                                                return;
                                            } else {
                                                state[0] = true;
                                            }

                                            Log.i(TAG, "Weather: " + weather);
                                        }
                                    });
                        }
                        if (!state[0]) {
                            if (fence.getNotification() != null) {
                                NotificationCompat.Builder builder =
                                        new NotificationCompat.Builder(context)
                                                .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark_disabled)
                                                .setContentTitle("New Awareness Notification")
                                                .setContentText("Condition met for :  "+fence.getSituationname());
                                builder.setVibrate(new long[] { 1000, 1000});
                                builder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
                                Random random = new Random();
                                int m = random.nextInt(9999 - 1000) + 1000;
                                NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                                manager.notify(m, builder.build());

                            }
                            if (fence.getAppopen() != null) {
                                Intent intent1 = packageManager.getLaunchIntentForPackage(fence.getAppname());
                                if (intent1 != null) {
                                    context.startActivity(intent1);
                                }
                            }
                        }

                    }
                    break;
                case FenceState.FALSE:
                    Log.i(TAG, "Conditions not met.");
                    break;
                case FenceState.UNKNOWN:
                    Log.i(TAG, "The fence is in an unknown state.");

                    break;
            }

        }
    }
}
