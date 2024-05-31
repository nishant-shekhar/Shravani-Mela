package com.nsappsstudio.shravanimela;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import okhttp3.internal.annotations.EverythingIsNonNull;

public class Update extends AppCompatActivity {
    private DatabaseReference mDatabaseReference;
    private int appVersion;
    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        String project="Muzaffarpur 2022";

        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child(project);
        appVersion = BuildConfig.VERSION_CODE;
        sharedPref = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        TextView cV = findViewById(R.id.textView15);
        String cvText = "Your App Version: " + appVersion;
        cV.setText(cvText);

        DatabaseReference mUpdateMessage = mDatabaseReference.child("GlobalParameter").child("AppVersion");
        final TextView messageView = findViewById(R.id.update_message);

        mUpdateMessage.child("message").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@EverythingIsNonNull DataSnapshot dataSnapshot) {
                String msg = dataSnapshot.getValue(String.class);
                messageView.setText(msg);
            }

            @Override
            public void onCancelled(@EverythingIsNonNull DatabaseError databaseError) {

            }
        });
        checkAppVersion();

    }

    private void checkAppVersion() {

        DatabaseReference mAppVer = mDatabaseReference.child("GlobalParameter").child("AppVersion");
        mAppVer.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@EverythingIsNonNull DataSnapshot dataSnapshot) {
                Integer latestVersion = dataSnapshot.child("App_LV").getValue(Integer.class);
                Integer minSupporterVersion = dataSnapshot.child("App_MV").getValue(Integer.class);

                TextView mV = findViewById(R.id.textView16);
                String mvText = "App Minimum Supported Version: " + minSupporterVersion;
                mV.setText(mvText);

                TextView lV = findViewById(R.id.textView17);
                String lvText = "App Latest Version: " + latestVersion;
                lV.setText(lvText);

                if (minSupporterVersion != null && latestVersion != null) {
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putInt("minVersion", minSupporterVersion);
                    editor.apply();

                    if (appVersion >= latestVersion) {//|| appVersion>=minSupporterVersion) {
                        // update notification
                        Intent intentNew = new Intent(Update.this, com.nsappsstudio.shravanimela.MainActivity.class);
                        intentNew.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intentNew);
                        finish();

                    }
                }
            }

            @Override
            public void onCancelled(@EverythingIsNonNull DatabaseError databaseError) {

            }
        });


    }


    public void updateApp(View view) {
        final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException error) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }
}