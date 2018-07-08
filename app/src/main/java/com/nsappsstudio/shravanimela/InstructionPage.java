package com.nsappsstudio.shravanimela;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class InstructionPage extends AppCompatActivity {


    private ViewPager slides;
    private DatabaseReference mDatabaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instruction_page);

        slides = findViewById(R.id.instruction_view_pager);
        mDatabaseReference= FirebaseDatabase.getInstance().getReference();


        InstructionSliderAdapter sliderAdaptor= new InstructionSliderAdapter(this);
        slides.setAdapter(sliderAdaptor);

        /*Calendar calendar= Calendar.getInstance();
        String year=String.valueOf(calendar.get(Calendar.YEAR));
        String currentMonth =String.valueOf(calendar.get(Calendar.MONTH)+1);
        String dayOfMonth=String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        if (dayOfMonth.length()==1){
            dayOfMonth="0"+dayOfMonth;
        }
        String date=year+currentMonth+dayOfMonth;*/
        checkAppVersion();
    }
    public void goToMain(){

        Intent intent= new Intent(this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();

    }
    public void nextPage(){
        slides.setCurrentItem(slides.getCurrentItem()+1 );

    }
    private void checkAppVersion(){
        final int appVersion=BuildConfig.VERSION_CODE;



        DatabaseReference mAppVer=mDatabaseReference.child("GlobalParameter").child("AppVersion").child("SM");
        mAppVer.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Integer latestVersion=dataSnapshot.child("LV").getValue(Integer.class);
                Integer minSupporterVersion=dataSnapshot.child("MV").getValue(Integer.class);
                assert latestVersion!=null ;
                assert minSupporterVersion!=null;
                if (appVersion>=minSupporterVersion && appVersion<latestVersion){
                    // update notification
                    toastMessage("New Update is Available");

                }else if(appVersion!=latestVersion) {

                    //update Now
                    toastMessage("Please Update your app to the latest version");

                    final String appPackageName =getPackageName(); // getPackageName() from Context or Activity object
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                    } catch (android.content.ActivityNotFoundException anfe) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void toastMessage(String text) {
        Toast.makeText(this,text,Toast.LENGTH_SHORT).show();
    }
}
