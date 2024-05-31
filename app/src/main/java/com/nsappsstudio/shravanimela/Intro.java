package com.nsappsstudio.shravanimela;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nsappsstudio.shravanimela.Animation.Animations;

import java.util.Locale;

import okhttp3.internal.annotations.EverythingIsNonNull;

public class Intro extends AppCompatActivity {

    private DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPref = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String language= sharedPref.getString("lang",null);
        if (language!=null){
            setLanguage(language);
            //toastMessage("lang"+language);
        }else {
            Intent intent= new Intent(this, com.nsappsstudio.shravanimela.LanguageSelect.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_intro);
        String project="Muzaffarpur 2022";
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child(project);
        checkAppVersion();

    }
    private void setLanguage(String language){
        Locale locale=new Locale(language);
        Locale.setDefault(locale);
        Configuration configuration= new Configuration();
        configuration.locale=locale;
        getBaseContext().getResources().updateConfiguration(configuration,getBaseContext().getResources().getDisplayMetrics());

    }
    @Override
    protected void onStart() {
        super.onStart();
        ImageView damru=findViewById(R.id.damru);
        ImageView bg=findViewById(R.id.imageView);
        Animations.scaleWithAlpha(bg,0.3f,1f,0.5f,0.5f,1500);
        Animations.translateWithAlpha(damru,100,1200,2,0f,1f);

        //TextView title=findViewById(R.id.textView7);
        ImageView title=findViewById(R.id.imageView14);
        TextView title2=findViewById(R.id.t4);
        Animations.scaleWithAlpha(title,0.3f,1f,0.5f,0.5f,1500);
        Animations.scaleWithAlpha(title2,0.3f,1f,0.5f,0.5f,1500);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(Intro.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        },2000);

    }
    private void checkAppVersion(){
        int appVersion = BuildConfig.VERSION_CODE;

        SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        int minVersion = sharedPreferences.getInt("minVersion", 1);

        //toastMessage(minVersion +" | "+appVersion);
        if (appVersion<minVersion){
            Intent intentNew = new Intent(Intro.this, Update.class);
            intentNew.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intentNew);
            finish();
        }
        DatabaseReference mAppVer= mDatabaseReference.child("GlobalParameter").child("AppVersion");
        mAppVer.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@EverythingIsNonNull DataSnapshot dataSnapshot) {
                Integer latestVersion=dataSnapshot.child("App_LV").getValue(Integer.class);
                Integer minSupporterVersion=dataSnapshot.child("App_MV").getValue(Integer.class);
                //toastMessage(minSupporterVersion+"");
                if(minSupporterVersion!=null && latestVersion!=null) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("minVersion", minSupporterVersion);
                    editor.apply();

                    if (appVersion >= minSupporterVersion && appVersion < latestVersion) {

                        // update notification
                        Toast.makeText(getApplicationContext(),"Update Your App",Toast.LENGTH_SHORT).show();
                        Intent intentNew = new Intent(Intro.this, Update.class);
                        intentNew.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intentNew);
                    } else if (appVersion < minSupporterVersion) {

                        //update Now
                        Toast.makeText(getApplicationContext(),"Update Your App Now",Toast.LENGTH_SHORT).show();
                        Intent intentNew = new Intent(Intro.this, Update.class);
                        intentNew.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intentNew);
                        //finish();
                    }
                }
            }

            @Override
            public void onCancelled(@EverythingIsNonNull DatabaseError databaseError) {

            }
        });
    }
}