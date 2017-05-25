package com.example.usman.newawareness.Activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.codemybrainsout.placesearch.PlaceSearchDialog;
import com.example.usman.newawareness.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Activity_Maps extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnMarkerDragListener,
        GoogleMap.OnMapLongClickListener,
        LocationListener,
        GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMarkerClickListener,
        View.OnClickListener {

    private GoogleMap mMap;
    Button btn_search;
    ImageButton btn_zoomin, btn_zoomout;
    EditText edt_search;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    Button location_one, location_two, radius_one, radius_two, current_location, duration_two;
    private GoogleApiClient googleApiClient;
    LocationRequest mLocationRequest;
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private final static int REQUEST_PERMISSION_RESULT_CODE = 42;
    private Handler mHandler = new Handler();
    String g;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        preferences = PreferenceManager.getDefaultSharedPreferences(Activity_Maps.this);
        editor = preferences.edit();

        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();

        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000);
        btn_search = (Button) findViewById(R.id.maps_activity_btn);
        edt_search = (EditText) findViewById(R.id.maps_activity_edt);
        btn_zoomin = (ImageButton) findViewById(R.id.btn_zoomin);
        btn_zoomout = (ImageButton) findViewById(R.id.maps_activity_btn_zoomout);
        current_location = (Button) findViewById(R.id.maps_btn_current_location);
        current_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Current_Location();
            }
        });
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Click_Listeners();

    }

    public void Click_Listeners() {
        btn_search.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                search();

            }
        });
        btn_zoomin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.animateCamera(CameraUpdateFactory.zoomIn());
            }
        });

        btn_zoomout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.animateCamera(CameraUpdateFactory.zoomOut());
            }
        });
    }

    protected void search() {


        PlaceSearchDialog placeSearchDialog = new PlaceSearchDialog.Builder(this)
                .setLocationNameListener(new PlaceSearchDialog.LocationNameListener() {
                    @Override
                    public void locationName(String locationName) {
                       g=locationName;
                        List<Address> addressList = null;

                        if (g != null || !g.equals("")) {
                            Geocoder geocoder = new Geocoder(Activity_Maps.this);
                            try {
                                addressList = geocoder.getFromLocationName(g, 3);

                            } catch (IOException e) {
                                //e.printStackTrace();
                            }
                            if(addressList!=null)
                            {
                                Address address = addressList.get(0);
                                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                                mMap.addMarker(new MarkerOptions().position(latLng).title("Marker"));
                                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                                // mMap.animateCamera(CameraUpdateFactory.zoomTo(5));

                            }

                        }
                    }
                })
                .build();
        placeSearchDialog.show();



    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.
                PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestLocationPermission();

        }
        Location currentLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if (currentLocation == null) {
            // Blank for a moment...
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            googleApiClient.connect();
        } else {
            final LatLng myLatLng = new LatLng(currentLocation.getLatitude(),
                    currentLocation.getLongitude());
            mMap.clear();
            mMap.addMarker(new MarkerOptions().position(myLatLng));
            mMap.animateCamera(CameraUpdateFactory.newLatLng(myLatLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(5));

        }
        mMap.setMyLocationEnabled(true);
        //mMap.animateCamera(CameraUpdateFactory.zoomTo(5));
        /**LatLng sydney = new LatLng(-34, 151);
         mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
         mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(final LatLng latLng) {
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(latLng));
                final Double lat = latLng.latitude;
                final Double longi = latLng.longitude;
                final android.app.AlertDialog.Builder logoutDialogBuilder = new android.app.AlertDialog.Builder(Activity_Maps.this);
                logoutDialogBuilder.setTitle(R.string.use_this_location).setCancelable(true).setPositiveButton(R.string.yes,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                final Dialog d = new Dialog(Activity_Maps.this);
                                d.setTitle("Radius");
                                d.setContentView(R.layout.page_fence_selection_radius_time_picker);
                                location_one = (Button) d.findViewById(R.id.btn_medicalid_numberpicker);
                                location_two = (Button) d.findViewById(R.id.btn_medicalid_numberpickercancel);
                                final NumberPicker np = (NumberPicker) d.findViewById(R.id.np_medicalid_numberPicker);
                                EditText edtunitvalue = (EditText) d.findViewById(R.id.edt_medicalid_numberpickerunit);
                                edtunitvalue.setText("Meters");
                                np.setMinValue(1);
                                //Specify the maximum value/number of NumberPicker
                                np.setMaxValue(1000);

                                //Gets whether the selector wheel wraps when reaching the min/max value.
                                np.setWrapSelectorWheel(true);
                                d.show();
                                location_one.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        editor.putInt("radius", np.getValue());
                                        final Dialog d = new Dialog(Activity_Maps.this);
                                        d.setTitle("Duration");
                                        d.setContentView(R.layout.page_fence_selection_time_picker);
                                        radius_one = (Button) d.findViewById(R.id.btn_medicalid_numberpicker);
                                        radius_two = (Button) d.findViewById(R.id.btn_medicalid_numberpickercancel);
                                        final NumberPicker np = (NumberPicker) d.findViewById(R.id.np_medicalid_numberPicker);
                                        final NumberPicker np1 = (NumberPicker) d.findViewById(R.id.np_medicalid_numberPicker_minute);

                                        final EditText edtunitvalue = (EditText) d.findViewById(R.id.edt_medicalid_numberpickerunit);
                                        final EditText edtunitvalue1 = (EditText) d.findViewById(R.id.edt_medicalid_numberpickerunit_minute);

                                        edtunitvalue1.setText("Minutes");
                                        edtunitvalue.setText("Hours");

                                        np.setMinValue(0);
                                        np.setMaxValue(10);
                                        np.setWrapSelectorWheel(true);
                                        np1.setMinValue(0);
                                        np1.setMaxValue(60);
                                        np1.setWrapSelectorWheel(true);

                                        d.show();
                                        radius_one.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                editor.putString("hours", String.valueOf(np.getValue()));
                                                editor.putString("minutes", String.valueOf(np1.getValue()));
                                                Intent intent = new Intent(Activity_Maps.this, Activity_Fence_Selection.class);
                                                Geocoder myLocation = new Geocoder(Activity_Maps.this, Locale.getDefault());
                                                List<Address> myList = null;
                                                String address = "";
                                                Geocoder geoCoder = new Geocoder( getBaseContext(), Locale.getDefault());
                                                try {
                                                    List<Address> addresses = geoCoder.getFromLocation(lat ,longi , 1);
                                                    if (addresses.size() > 0)
                                                    {
                                                        for (int index = 0;
                                                             index < addresses.get(0).getMaxAddressLineIndex(); index++)

                                                            address += addresses.get(0).getAddressLine(index) + " ";
                                                        editor.putString("address", address);

                                                    }
                                                }
                                                catch (IOException e) {
                                                    e.printStackTrace();
                                                }
                                                editor.putString("latstring", String.valueOf(lat));
                                                    editor.putString("longistring", String.valueOf(longi));
                                                    editor.putLong("lat", Double.doubleToRawLongBits(lat.longValue()));
                                                    editor.putLong("long", Double.doubleToRawLongBits(longi.longValue()));
                                                    editor.apply();
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


                                                    // Add new Flag to start new Activity
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    startActivity(intent);
                                                }

                                            });
                                        //d.dismiss();
                                        radius_two.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                d.dismiss();
                                            }
                                        });
                                    }

                                });

                                location_two.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        d.dismiss();
                                    }
                                });


                            }
                        }).setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();

                    }
                });
                logoutDialogBuilder.show();
            }
        });

        // Add a marker in Sydney and move the camera

    }

    private boolean isGPSEnabled() {
        LocationManager cm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return cm.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public void Current_Location() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestLocationPermission();
            return;
        }
        if(!isGPSEnabled())
        {
            {
                new AlertDialog.Builder(this)
                        .setMessage("Please activate your GPS Location!")
                        .setCancelable(false)
                        .setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(i);

                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            }
        }
        else
        {
            mMap.setMyLocationEnabled(true);
            Location currentLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            final LatLng myLatLng = new LatLng(currentLocation.getLatitude(),
                    currentLocation.getLongitude());
            //mMap.clear();
            mMap.addMarker(new MarkerOptions().position(myLatLng));
            final Double lat = myLatLng.latitude;
            final Double longi = myLatLng.longitude;
            final android.app.AlertDialog.Builder logoutDialogBuilder = new android.app.AlertDialog.Builder(Activity_Maps.this);
            logoutDialogBuilder.setTitle(R.string.use_this_location).setCancelable(true).setPositiveButton(R.string.yes,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            final Dialog d = new Dialog(Activity_Maps.this);
                            d.setTitle("Radius");
                            d.setContentView(R.layout.page_fence_selection_radius_time_picker);
                            location_one = (Button) d.findViewById(R.id.btn_medicalid_numberpicker);
                            location_two = (Button) d.findViewById(R.id.btn_medicalid_numberpickercancel);
                            final NumberPicker np = (NumberPicker) d.findViewById(R.id.np_medicalid_numberPicker);
                            EditText edtunitvalue = (EditText) d.findViewById(R.id.edt_medicalid_numberpickerunit);
                            edtunitvalue.setText("Meters");
                            np.setMinValue(1);
                            np.setMaxValue(1000);
                            np.setWrapSelectorWheel(true);
                            d.show();
                            location_one.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    editor.putInt("radius", np.getValue());
                                    final Dialog d = new Dialog(Activity_Maps.this);
                                    d.setTitle("Duration");
                                    d.setContentView(R.layout.page_fence_selection_time_picker);
                                    radius_one = (Button) d.findViewById(R.id.btn_medicalid_numberpicker);
                                    radius_two = (Button) d.findViewById(R.id.btn_medicalid_numberpickercancel);
                                    final NumberPicker np = (NumberPicker) d.findViewById(R.id.np_medicalid_numberPicker);
                                    final NumberPicker np1 = (NumberPicker) d.findViewById(R.id.np_medicalid_numberPicker_minute);

                                    final EditText edtunitvalue = (EditText) d.findViewById(R.id.edt_medicalid_numberpickerunit);
                                    final EditText edtunitvalue1 = (EditText) d.findViewById(R.id.edt_medicalid_numberpickerunit_minute);

                                    edtunitvalue1.setText("Minutes");
                                    edtunitvalue.setText("Hours");

                                    np.setMinValue(0);
                                    np.setMaxValue(10);
                                    np.setWrapSelectorWheel(true);
                                    np1.setMinValue(0);
                                    np1.setMaxValue(60);
                                    np1.setWrapSelectorWheel(true);

                                    d.show();
                                    radius_one.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            editor.putString("hours", String.valueOf(np.getValue()));
                                            editor.putString("minutes", String.valueOf(np1.getValue()));
                                            Intent intent = new Intent(Activity_Maps.this, Activity_Fence_Selection.class);
                                            Geocoder myLocation = new Geocoder(Activity_Maps.this, Locale.getDefault());
                                            List<Address> myList = null;
                                            String address = "";
                                            Geocoder geoCoder = new Geocoder( getBaseContext(), Locale.getDefault());
                                            try {
                                                List<Address> addresses = geoCoder.getFromLocation(lat ,longi , 1);
                                                if (addresses.size() > 0)
                                                {
                                                    for (int index = 0;
                                                         index < addresses.get(0).getMaxAddressLineIndex(); index++)

                                                        address += addresses.get(0).getAddressLine(index) + " ";
                                                    if(address!=null)
                                                    {
                                                        editor.putString("address", address);
                                                        editor.apply();

                                                    }
                                                }
                                                else {
                                                    editor.putString("address", "Location Name Unknown");
                                                    editor.apply();
                                                }
                                            }
                                            catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                            String area = preferences.getString("address", "a");
                                            if(area.equals("a"))
                                            {editor.putString("address", "Location Name Unknown");
                                                editor.apply();

                                            }




                                            editor.putString("latstring", String.valueOf(lat));
                                            editor.putString("longistring", String.valueOf(longi));
                                            editor.putLong("lat", Double.doubleToRawLongBits(lat.longValue()));
                                            editor.putLong("long", Double.doubleToRawLongBits(longi.longValue()));
                                            editor.apply();
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


                                            // Add new Flag to start new Activity
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                        }


                                    });
                                    //d.dismiss();
                                    radius_two.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            d.dismiss();
                                        }
                                    });
                                }

                            });

                            location_two.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    d.dismiss();
                                }
                            });


                        }
                    }).setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();

                }
            });
            logoutDialogBuilder.show();
        }


    }

    private void requestLocationPermission()
    {

        ActivityCompat.requestPermissions(
                Activity_Maps.this,
                new String[]{ Manifest.permission.ACCESS_FINE_LOCATION },
                REQUEST_PERMISSION_RESULT_CODE );


    }

    @Override
    public void onClick(View v) {

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_RESULT_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //granted
                } else {
                    Log.e("Tuts+", "Location permission denied.");
                }
            }
        }
    }
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        //My_Location();
        requestLocationPermission();
        //My_Location();
    }

    private void My_Location() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.
                ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            requestLocationPermission();
        }
        if(!isGPSEnabled())
        {
            {
                new AlertDialog.Builder(this)
                        .setMessage("Please activate your GPS Location!")
                        .setCancelable(false)
                        .setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                                //i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(i);
                                Activity_Maps.this.finishAffinity();
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show();

            }
        }
        else
        {
            Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            final LatLng myLatLng = new LatLng(mLastLocation.getLatitude(),
                    mLastLocation.getLongitude());
            mMap.clear();
            mMap.addMarker(new MarkerOptions().position(myLatLng));
            mMap.animateCamera(CameraUpdateFactory.newLatLng(myLatLng));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLatLng, 15));
        }



    }


    private void handleNewLocation(Location location) {
        Log.d("tag", location.toString());
    }
    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Log.i("TAG", "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    @Override
    public void onMapLongClick(LatLng latLng) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {

    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onLocationChanged(Location location) {

    }


}

