package com.nsappsstudio.shravanimela;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.Random;

public class SOS extends FragmentActivity implements OnMapReadyCallback {
    private static final String TAG = com.nsappsstudio.shravanimela.SOS.class.getSimpleName();

    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    private static final int REQUEST_CHECK_SETTINGS = 0x1;

    private FusedLocationProviderClient mFusedLocationClient;
    private SettingsClient mSettingsClient;
    private LocationRequest mLocationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;
    private LocationCallback mLocationCallback;
    private Location mCurrentLocation;

    private DatabaseReference mDatabaseReference;
    private GoogleMap mMap;
    private boolean mapIsReady;
    private FirebaseUser user;
    private String uPhoneNo;
    private SharedPreferences sharedPref;
    private ScrollView scrollView;
    private CardView dangerCard;
    private CardView kidnapCard;
    private CardView lostCard;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sos);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mSettingsClient = LocationServices.getSettingsClient(this);
        mDatabaseReference= FirebaseDatabase.getInstance().getReference();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        user= mAuth.getCurrentUser();
        scrollView=findViewById(R.id.scroll);
        dangerCard=findViewById(R.id.danger_card);
        lostCard=findViewById(R.id.lost_card);
        kidnapCard=findViewById(R.id.kidnap_card);

        if (user!=null) {
            uPhoneNo = user.getPhoneNumber();
        }
        sharedPref=getSharedPreferences("userInfo", Context.MODE_PRIVATE);


        createLocationCallback();
        createLocationRequest();
        buildLocationSettingsRequest();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mapIsReady=true;

        LatLng latLng = new LatLng(25.254602, 86.738479);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));

        // Add a marker in Sydney and move the camera

    }



    @Override
    public void onResume() {
        super.onResume();
        if (checkPermissions()) {
            startLocationUpdates();
        } else if (!checkPermissions()) {
            requestPermissions();
        }
    }
    public void helpDanger(View v){
        String type= "Help I am in Danger";
        String status="HN";
        sos(type,status);
    }
    public void helpKidnap(View v){
        String type= "Help I am being Kidnap";
        String status="HN";
        sos(type,status);

    }
    public void helpLost(View v){
        String type= "Help I am Lost";
        String status="HN";
        sos(type,status);

    }
    public void safe(View v){
        String type= "Now I am Safe.Marked By User";
        String status="Safe";
        sos(type,status);

        onBackPressed();
        stopLocationUpdates();


    }

    private void sos(String type, String status){
        if(!status.equals("Safe")) {
            Toast.makeText(this, "We are sending your location to control room", Toast.LENGTH_LONG).show();
        }
        if (user!=null && uPhoneNo!=null) {

            DatabaseReference mSosRef = mDatabaseReference.child("ControlRoom").child("SOS").child(uPhoneNo);
            mSosRef.child("type").setValue(type);
            mSosRef.child("status").setValue(status).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    slideDown(scrollView);
                    dangerCard.setFocusable(false);
                    kidnapCard.setFocusable(false);
                    lostCard.setFocusable(false);
                }
            });
        }else {
            String userId=sharedPref.getString("userId",null);

            if (userId == null) {
                int userNo;
                Random n = new Random();
                userNo = n.nextInt(1000000 );
                userId=String.valueOf(userNo);

                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("userId", userId);
                editor.apply();
            }
            DatabaseReference mSosRef = mDatabaseReference.child("ControlRoom").child("SOS").child(userId);
            mSosRef.child("type").setValue(type);
            mSosRef.child("status").setValue(status).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    slideDown(scrollView);
                    dangerCard.setFocusable(false);
                    kidnapCard.setFocusable(false);
                    lostCard.setFocusable(false);
                }
            });

        }

    }


    protected void createLocationRequest() {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(15000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }
    private void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }
    private void stopLocationUpdates() {
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }

    private void createLocationCallback() {
        mLocationCallback = new LocationCallback() {
            @RequiresApi(api = Build.VERSION_CODES.N)

            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                if (mapIsReady) {
                    mMap.clear();
                    mCurrentLocation = locationResult.getLastLocation();
                    double mLatitude = mCurrentLocation.getLatitude();
                    double mLongitude = mCurrentLocation.getLongitude();
                    LatLng latLng = new LatLng(mLatitude, mLongitude);
                    mMap.addMarker(new MarkerOptions().position(latLng).title("My Current Location"));
                    //mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));

                    if (user!=null && uPhoneNo!=null) {
                        DatabaseReference mSosRef = mDatabaseReference.child("ControlRoom").child("SOS").child("");
                        mSosRef.child("lat").setValue(mLatitude);
                        mSosRef.child("lang").setValue(mLongitude);
                        mSosRef.child("lastTime").setValue(ServerValue.TIMESTAMP);
                    }else {
                        String userId=sharedPref.getString("userId",null);

                        if (userId == null) {
                            int userNo;
                            Random n = new Random();
                            userNo = n.nextInt(1000000 );
                            userId=String.valueOf(userNo);

                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString("userId", userId);
                            editor.apply();
                            }

                        DatabaseReference mSosRef = mDatabaseReference.child("ControlRoom").child("SOS").child(userId);
                        mSosRef.child("lat").setValue(mLatitude);
                        mSosRef.child("lang").setValue(mLongitude);
                        mSosRef.child("lastTime").setValue(ServerValue.TIMESTAMP);
                    }

                }
            }
        };
    }


    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION);

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.");
            showSnackbar(R.string.permission_rationale,
                    android.R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Request permission
                            ActivityCompat.requestPermissions(com.nsappsstudio.shravanimela.SOS.this,
                                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                                    REQUEST_PERMISSIONS_REQUEST_CODE);
                        }
                    });
        } else {
            Log.i(TAG, "Requesting permission");
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            ActivityCompat.requestPermissions(com.nsappsstudio.shravanimela.SOS.this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    private void showSnackbar(final int mainTextStringId, final int actionStringId,
                              View.OnClickListener listener) {
        Snackbar.make(
                findViewById(android.R.id.content),
                getString(mainTextStringId),
                Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(actionStringId), listener).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Log.i(TAG, "onRequestPermissionResult");
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                // If user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
                Log.i(TAG, "User interaction was cancelled.");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Log.i(TAG, "Permission granted, updates requested, starting location updates");
                startLocationUpdates();

            } else {
                // Permission denied.

                // Notify the user via a SnackBar that they have rejected a core permission for the
                // app, which makes the Activity useless. In a real app, core permissions would
                // typically be best requested during a welcome-screen flow.

                // Additionally, it is important to remember that a permission might have been
                // rejected without asking the user for permission (device policy or "Never ask
                // again" prompts). Therefore, a user interface affordance is typically implemented
                // when permissions are denied. Otherwise, your app could appear unresponsive to
                // touches or interactions which have required permissions.
                showSnackbar(R.string.permission_denied_explanation,
                        R.string.settings, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Build intent that displays the App settings screen.
                                Intent intent = new Intent();
                                intent.setAction(
                                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package",
                                        BuildConfig.APPLICATION_ID, null);
                                intent.setData(uri);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        });
            }
        }
    }

    private void startLocationUpdates() {
        // Begin by checking if the device has the necessary location settings.
        mSettingsClient.checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        Log.i(TAG, "All location settings are satisfied.");

                        //noinspection MissingPermission
                        if (ActivityCompat.checkSelfPermission(com.nsappsstudio.shravanimela.SOS.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(com.nsappsstudio.shravanimela.SOS.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                                mLocationCallback, Looper.myLooper());
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                Log.i(TAG, "Location settings are not satisfied. Attempting to upgrade " +
                                        "location settings ");
                                try {
                                    // Show the dialog by calling startResolutionForResult(), and check the
                                    // result in onActivityResult().
                                    ResolvableApiException rae = (ResolvableApiException) e;
                                    rae.startResolutionForResult(com.nsappsstudio.shravanimela.SOS.this, REQUEST_CHECK_SETTINGS);
                                } catch (IntentSender.SendIntentException sie) {
                                    Log.i(TAG, "PendingIntent unable to execute request.");
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                String errorMessage = "Location settings are inadequate, and cannot be " +
                                        "fixed here. Fix in Settings.";
                                Log.e(TAG, errorMessage);
                                Toast.makeText(com.nsappsstudio.shravanimela.SOS.this, errorMessage, Toast.LENGTH_LONG).show();
                        }

                    }
                });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Log.i(TAG, "User agreed to make required location settings changes.");
                        // Nothing to do. startLocationUpdates() gets called in onResume again.
                        break;
                    case Activity.RESULT_CANCELED:
                        Log.i(TAG, "User choose not to make required location settings changes.");
                        break;
                }
                break;


        }
    }

    private void slideDown(final View view){

        Animation grow = AnimationUtils.loadAnimation(this, R.anim.slide_down);
        view.startAnimation(grow);
        new CountDownTimer(500, 500) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                view.setVisibility(View.GONE);


            }
        }.start();
    }



}
