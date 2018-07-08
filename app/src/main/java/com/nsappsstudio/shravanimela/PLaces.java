package com.nsappsstudio.shravanimela;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
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
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class PLaces extends FragmentActivity implements OnMapReadyCallback {
    private static final String TAG = PLaces.class.getSimpleName();

    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    private static final int REQUEST_CHECK_SETTINGS = 0x1;

    private FusedLocationProviderClient mFusedLocationClient;
    private SettingsClient mSettingsClient;
    private LocationRequest mLocationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;
    private LocationCallback mLocationCallback;
    private Location mCurrentLocation;
    private RecyclerView recyclerView;

    private GoogleMap mMap;
    private double mLatitude;
    private double mLongitude;
    private boolean flagRecyclerView;
    private ArrayList<Float> distanceList= new ArrayList<>();
    private CardView detailCard;

    private String fileName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        flagRecyclerView=false;
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mSettingsClient = LocationServices.getSettingsClient(this);
        recyclerView=findViewById(R.id.places_recyclerView);
        detailCard=findViewById(R.id.p_detail_card);
        detailCard.setVisibility(View.GONE);

        try {
            Intent intent=getIntent();
            String typeID = intent.getStringExtra("type");
            if (typeID!=null){
                fileName=typeID;
            }
        }catch (IllegalArgumentException error){
            //do nothing
            fileName="Toilet.json";
        }


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
        // Add a marker in Sydney and move the camera
        fillRecyclerView();
    }
    public String loadJSONFromAsset() {
        String json ;
        try {
            Context c=this;
            InputStream is = c.getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
    private void fillRecyclerView(){
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<PlacesItem> placesItems = new ArrayList<>();
        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset());
            JSONArray m_jArry = obj.getJSONArray("sightings");

            for (int i = 0; i < m_jArry.length(); i++) {
                JSONObject jo_inside = m_jArry.getJSONObject(i);
                String category = jo_inside.getString("Category");
                String place_name = jo_inside.getString("Place_Name");
                double latitude = jo_inside.getDouble("Latitude");
                double longitude = jo_inside.getDouble("Longitude");
                String distance;
                int np;
                if (mCurrentLocation!=null) {

                    float dist=GetDistance(latitude,longitude);
                    int roundDistance=Math.round(dist);
                    if (roundDistance>1000){
                        double distKm=(double) roundDistance/1000.0;
                        distance = "Air Distance: " +distKm + "Km";

                    }else {
                        distance = "Air Distance: " +roundDistance + "m";

                    }
                    if (i==m_jArry.length()-1){
                        np=GetNearestPlace();
                        recyclerView.scrollToPosition(np);
                        detailCard.setVisibility(View.VISIBLE);
                        if (np==i){
                            onPlaceClicked(latitude,longitude,place_name);
                            detailCard(latitude,longitude,place_name,distance,category);
                        }else {
                            onPlaceClicked(placesItems.get(np).getLat(),placesItems.get(np).getLang(),placesItems.get(np).getTitle());
                            detailCard(placesItems.get(np).getLat(),placesItems.get(np).getLang(),placesItems.get(np).getTitle(),placesItems.get(np).getSubtitle(),placesItems.get(np).getItemType());

                        }
                    }
                }else {
                    distance=null;
                }

                LatLng placeMarker = new LatLng(latitude, longitude);
                mMap.addMarker(new MarkerOptions().position(placeMarker).title(place_name));


                String title=String.valueOf(i+1)+". "+place_name;
                PlacesItem placesItem = new PlacesItem(title, distance,category,i,null ,latitude,longitude);
                placesItems.add(placesItem);
                RecyclerView.Adapter adapter = new Places_list_Adapter(placesItems, PLaces.this);
                recyclerView.setAdapter(adapter);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void onPlaceClicked(double lat,double lang, String place_name){
        LatLng placeMarker = new LatLng(lat, lang);
        CameraUpdate location = CameraUpdateFactory.newLatLngZoom(placeMarker, 16);
        mMap.animateCamera(location);

    }
    public void reLoadRecyclerView(View v){
        mMap.clear();
        fillRecyclerView();
    }
    public void startNavigation(double lat,double lang){
        String url="google.navigation:q="+lat+","+lang;//+"&mode=w";
        Uri gmmIntentUri = Uri.parse(url);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }
    public void detailCard(final double lat, final double lang, String place_name, String subtitle, String category){
        TextView titleView=findViewById(R.id.p_card_title);
        TextView subTitleView=findViewById(R.id.p_card_title2);
        ImageView imageView=findViewById(R.id.p_card_image);
        CardView gmap=findViewById(R.id.p_gmap);
        CardView navigate=findViewById(R.id.p_navigate);


        switch (category){
            case "Toilet":
                imageView.setImageResource(R.drawable.toilet);
                break;
            case "Jharna":
                imageView.setImageResource(R.drawable.waterfall);
                break;
            case "Police Station":
                imageView.setImageResource(R.drawable.police);
                break;
            case "Stay Place":
                imageView.setImageResource(R.drawable.rest_room);
                break;
            case "Control Room ":
                imageView.setImageResource(R.drawable.call_center);
                break;
        }

        String title=place_name+" | "+category;
        titleView.setText(title);
        subTitleView.setText(subtitle);

        gmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url="geo:"+lat+","+lang+"?z=15";

                Uri gmmIntentUri = Uri.parse(url);

                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(mapIntent);
                }
            }
        });
        navigate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url="google.navigation:q="+lat+","+lang;//+"&mode=w";
                Uri gmmIntentUri = Uri.parse(url);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(mapIntent);
                }
            }
        });





    }
    private float GetDistance(double uLatitude,double uLongitude){

            float[] results = new float[10];
            Location.distanceBetween(mLatitude, mLongitude,uLatitude, uLongitude, results);
            distanceList.add(results[0]);
            return results[0];
    }
    private int GetNearestPlace(){

        float nearestDistance= distanceList.get(0);
        int i=0;
        for (int n=1;n<distanceList.size();n++){
            if (nearestDistance>distanceList.get(n)){
                nearestDistance=distanceList.get(n);
               i=n;
            }
        }
        return i;
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

    @Override
    protected void onStop() {
        super.onStop();
        stopLocationUpdates();
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

                    mCurrentLocation = locationResult.getLastLocation();
                    mLatitude = mCurrentLocation.getLatitude();
                    mLongitude = mCurrentLocation.getLongitude();
                    LatLng latLng = new LatLng(mLatitude, mLongitude);
                    mMap.addMarker(new MarkerOptions().position(latLng).title("My Current Location"));
                    //mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                    if (!flagRecyclerView){
                        flagRecyclerView=true;
                        mMap.clear();
                        fillRecyclerView();

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
                            ActivityCompat.requestPermissions(PLaces.this,
                                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                                    REQUEST_PERMISSIONS_REQUEST_CODE);
                        }
                    });
        } else {
            Log.i(TAG, "Requesting permission");
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            ActivityCompat.requestPermissions(PLaces.this,
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
                        if (ActivityCompat.checkSelfPermission(PLaces.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(PLaces.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
                                    rae.startResolutionForResult(PLaces.this, REQUEST_CHECK_SETTINGS);
                                } catch (IntentSender.SendIntentException sie) {
                                    Log.i(TAG, "PendingIntent unable to execute request.");
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                String errorMessage = "Location settings are inadequate, and cannot be " +
                                        "fixed here. Fix in Settings.";
                                Log.e(TAG, errorMessage);
                                Toast.makeText(PLaces.this, errorMessage, Toast.LENGTH_LONG).show();
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
    private void toastMessage(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();

    }
}
