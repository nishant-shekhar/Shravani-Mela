package com.nsappsstudio.shravanimela;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

public class InstructionPage extends AppCompatActivity {


    private ViewPager slides;
    private DatabaseReference mDatabaseReference;
    private LinearLayout mDotsLayout;
    private Button next;
    private Button skip;
    private Button doNotShowBtn;
    private int currentPage;
    private TextView[] mDot;
    private CountDownTimer countDownTimer;
    private ConstraintLayout slide1;
    private String doNotShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPref = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String language= sharedPref.getString("lang",null);
        if (language!=null){
            setLanguage(language);
            //toastMessage("lang"+language);
        }else {
            Intent intent= new Intent(this,LanguageSelect.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }

        setContentView(R.layout.activity_instruction_page);
        doNotShow= sharedPref.getString("showInstruction",null);
        slide1=findViewById(R.id.splash_Screen);
        if (doNotShow==null){
            slide1.setVisibility(View.GONE);
        }else {
            if (doNotShow.equals("y")){
                slide1.setVisibility(View.VISIBLE);

                countDownTimer=new CountDownTimer(1200, 500) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {
                        Intent intent= new Intent(InstructionPage.this,MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }
                }.start();

            }
        }

        slides = findViewById(R.id.instruction_view_pager);
        mDatabaseReference= FirebaseDatabase.getInstance().getReference();
        mDotsLayout=findViewById(R.id.dots_layout);
        doNotShowBtn=findViewById(R.id.dont_show);
        doNotShowBtn.setVisibility(View.GONE);

        next=findViewById(R.id.next_btn);
        skip=findViewById(R.id.skip_btn);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextPage();
            }
        });
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMain();
            }
        });

        InstructionSliderAdapter sliderAdaptor= new InstructionSliderAdapter(this);
        slides.setAdapter(sliderAdaptor);
        currentPage=0;
        addDots(0);
        slides.addOnPageChangeListener(pageChangeListener);


        checkAppVersion();
    }
    private void addDots(int position){

        mDot = new TextView[3];
        mDotsLayout.removeAllViews();
        for(int i = 0; i< mDot.length; i++){

            mDot[i]=new TextView(this);
            mDot[i].setText(Html.fromHtml("\u2022"));
            mDot[i].setTextSize(35);
            mDot[i].setTextColor(getResources().getColor(R.color.light_orange));

            mDotsLayout.addView(mDot[i]);
        }
        if (mDot.length>0){
            mDot[position].setTextColor(getResources().getColor(R.color.background));
        }

    }
    ViewPager.OnPageChangeListener pageChangeListener=new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addDots(position);
            currentPage=position;
            if(currentPage== mDot.length-1){
                if(doNotShow==null){
                    doNotShowBtn.setVisibility(View.VISIBLE);
                }
                skip.setVisibility(View.GONE);
                next.setText(getResources().getText(R.string.finish));
                next.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                            goToMain();
                    }
                });
            }else {
                doNotShowBtn.setVisibility(View.GONE);

                skip.setVisibility(View.VISIBLE);
                next.setText(getResources().getText(R.string.next));
                next.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                            nextPage();
                    }
                });
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
    private void setLanguage(String language){
        Locale locale=new Locale(language);
        Locale.setDefault(locale);
        Configuration configuration= new Configuration();
        configuration.locale=locale;
        getBaseContext().getResources().updateConfiguration(configuration,getBaseContext().getResources().getDisplayMetrics());

        /*SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("lang", language);
        editor.apply();*/
        //toastMessage("lang: "+language+String.valueOf(reStart));

    }
    public void goToMain(){

        Intent intent= new Intent(this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();

    }
    public void goToMainAndDoNotShow(View view){
        SharedPreferences sharedPref = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("showInstruction", "y");
        editor.apply();

        Intent intent= new Intent(this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();

    }
    public void goToMainSkip(View view){
        countDownTimer.cancel();
        Intent intent= new Intent(this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
    public void readInstruction(View view){
        countDownTimer.cancel();
        slide1.setVisibility(View.GONE);

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
