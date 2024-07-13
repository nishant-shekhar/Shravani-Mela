package com.nsappsstudio.shravanimela;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nsappsstudio.shravanimela.Adapter.PlacesAdapter;
import com.nsappsstudio.shravanimela.Model.PlacesModel;
import com.nsappsstudio.shravanimela.Utils.AppConstants;
import com.nsappsstudio.shravanimela.Utils.GpsUtils;
import com.nsappsstudio.shravanimela.Utils.Utils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.stfalcon.imageviewer.StfalconImageViewer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.internal.annotations.EverythingIsNonNull;

public class FindPlaces extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationClient;
    private Location mCurrentLocation;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;

    private DatabaseReference mDatabaseReference;
    private Context ctx;
    private String type;
    private List<PlacesModel> placesModels;
    private LatLngBounds.Builder builder;
    private boolean flagSelf;
    private double mUpdatedLang;
    private double mUpdatedLat;
    private List<String> images;
    private Dialog dialog;
    private String display;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_places);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        ctx=this;
        mUpdatedLang=0d;
        mUpdatedLat=0d;
        String project="Bhagalpur24";
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child(project);
        flagSelf=true;
        type=getIntent().getStringExtra("type");
        display=getIntent().getStringExtra("display");
        if (type!=null){
            TextView textView=findViewById(R.id.textView15);
            String t="Nearest "+display;
            textView.setText(t);

        }

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        createLocationRequest();
        createLocationCallback();


        getLocation();
        updateDialog("Loading location...");
        builder = new LatLngBounds.Builder();

    }
    private void loadRC(){
        RecyclerView recyclerView=findViewById(R.id.places_rc);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        placesModels=new ArrayList<>();
        images=new ArrayList<>();
        updateDialog("Downloading List...");

        mDatabaseReference.child("TagPlace").child(type).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@EverythingIsNonNull  DataSnapshot snapshot) {
                for (DataSnapshot snapshot1:snapshot.getChildren()){

                    PlacesModel placesModel=snapshot1.getValue(PlacesModel.class);
                    if (placesModel!=null && placesModel.getLat()!=null && placesModel.getLang()!=null){
                        if (placesModel.getPicUrl()!=null){
                            images.add(placesModel.getPicUrl());

                        }
                        double latD=placesModel.getLat();
                        double langD=placesModel.getLang();
                        String distance=null;
                        if (mCurrentLocation!=null) {
                            mUpdatedLat = mCurrentLocation.getLatitude();
                            mUpdatedLang = mCurrentLocation.getLongitude();
                            int dis = Math.round(Utils.GetDistance(latD, langD, mUpdatedLat, mUpdatedLang));
                            if (dis>1000){
                                float v = (float)dis / 1000;
                                distance=String.format("%.02f", v)+" Km";
                            }else {
                                distance=dis+"m";

                            }
                        }else {
                            toastMessage("no distance");
                        }

                        placesModel.setType(display);
                        placesModel.setDistance(distance);
                        placesModels.add(placesModel);
                    }

                }
                dialog.dismiss();
                PlacesAdapter adapter=new PlacesAdapter(placesModels,ctx);
                recyclerView.setAdapter(adapter);
                PlaceMarker();

            }

            @Override
            public void onCancelled(@EverythingIsNonNull DatabaseError error) {
                dialog.dismiss();
            }
        });
    }
    public void HighLight(double lat, double lang) {
        LatLng placeMarker1 = new LatLng(lat, lang);

        CameraUpdate location = CameraUpdateFactory.newLatLngZoom(placeMarker1, 16);
        mMap.animateCamera(location);
    }
    public void loadFullScreenImage(String url){

        int index=images.indexOf(url);
        new StfalconImageViewer.Builder<>(ctx, images, (imageView, image) -> Picasso.get().load(image).networkPolicy(NetworkPolicy.OFFLINE).into(imageView, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(Exception e) {
                Picasso.get().load(image).into(imageView);

            }
        })).withStartPosition(index).show();
    }
    public void startNavigation(double lat,double lang){
        String url="google.navigation:q="+lat+","+lang;//+"&mode=w";
        Uri gmmIntentUri = Uri.parse(url);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //Custom Theme
        MapStyleOptions style = MapStyleOptions.loadRawResourceStyle(ctx, R.raw.map2);
        mMap.setMapStyle(style);
        mMap.setPadding(100, 0, 100, 100);

    }
    private void PlaceMarker(){
        if (placesModels!=null){

            for (int i=0;i<placesModels.size();i++) {
                LatLng placeMarker1 = new LatLng(placesModels.get(i).getLat(), placesModels.get(i).getLang());
                MarkerOptions marker = new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)).position(placeMarker1).title((i+1)+". "+type);
                builder.include(marker.getPosition());
                mMap.addMarker(marker);
                CameraUpdate location = CameraUpdateFactory.newLatLngZoom(placeMarker1, 16);
                mMap.animateCamera(location);
            }
        }
    }
    protected void createLocationRequest() {
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(1000); // 5 seconds
        locationRequest.setFastestInterval(1000); // 1 seconds
    }
    private void createLocationCallback() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                mCurrentLocation = locationResult.getLastLocation();
                if (mCurrentLocation.isFromMockProvider()) {
                    toastMessage("Fake location \n A complain has been registered against you");
                    //DatabaseReference mFakeRef= mDatabaseReference.child("FakeAlert").child(userID).child(String.valueOf(mCurrentLocation.getTime()));
                    // mFakeRef.setValue(ServerValue.TIMESTAMP);
                    return;
                }

                //mUpdatedLat = mCurrentLocation.getLatitude();
                //mUpdatedLang = mCurrentLocation.getLongitude();
                if (builder!=null && flagSelf) {
                    //LatLng placeMarker1 = new LatLng(mUpdatedLat, mUpdatedLang);
                    //MarkerOptions marker = new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)).position(placeMarker1).title("In");
                    //builder.include(marker.getPosition());
                    //mMap.addMarker(marker);
                    flagSelf=false;
                    loadRC();
                }

            }
        };
    }
    private void toastMessage(String s) {
        Toast.makeText(this,s,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        new GpsUtils(this).turnGPSOn(new GpsUtils.onGpsListener() {
            @Override
            public void gpsStatus(boolean isGPSEnable) {
                // turn on GPS
                //toastMessage("gps is On");
            }
        });

    }
    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    AppConstants.LOCATION_REQUEST);

        } else {
            mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);

        }
    }
    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @EverythingIsNonNull String[] permissions, @EverythingIsNonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // If request is cancelled, the result arrays are empty.
        if (requestCode == 1000) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);

            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                loadRC();
            }
        }
    }
    private void loadDialog(String message){
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_simple);
        dialog.setCancelable(false);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(Objects.requireNonNull(dialog.getWindow()).getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        TextView dialogMessage=dialog.findViewById(R.id.dialog_text);

        ProgressBar progressBar=dialog.findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.VISIBLE);
        dialogMessage.setText(message);

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }
    private void updateDialog(String message){
        if (dialog!=null&& dialog.isShowing()){
            TextView dialogMessage=dialog.findViewById(R.id.dialog_text);
            dialogMessage.setText(message);
        }else {
            loadDialog(message);
        }
    }
}