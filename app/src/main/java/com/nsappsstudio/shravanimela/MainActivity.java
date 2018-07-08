package com.nsappsstudio.shravanimela;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent serviceIntent = new Intent(this, NotificationServices.class);
        startService(serviceIntent);

        AppBarLayout.OnOffsetChangedListener mListener = new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                CollapsingToolbarLayout collapsingToolbar=findViewById(R.id.collapsing_toolbar);
                android.support.v7.widget.Toolbar hello=findViewById(R.id.toolbar);
                if (collapsingToolbar.getHeight() + verticalOffset < 2 * ViewCompat.getMinimumHeight(collapsingToolbar)) {
                    hello.animate().alpha(1).setDuration(600);
                } else {
                    hello.animate().alpha(0).setDuration(600);
                }
            }
        };

        AppBarLayout appBar=findViewById(R.id.appBar);
        appBar.addOnOffsetChangedListener(mListener);

        final ViewPager slides = findViewById(R.id.slideshow_pager);

        SlideshowAdapter sliderAdaptor= new SlideshowAdapter(this);
        slides.setAdapter(sliderAdaptor);

        new CountDownTimer(5000000, 5000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (slides.getCurrentItem()<6){
                    slides.setCurrentItem(slides.getCurrentItem()+1 );
                } else {
                    slides.setCurrentItem(0);
                }
            }

            @Override
            public void onFinish() {

            }
        }.start();


    }
    public void GoToInstruction(View view){

        Intent intent= new Intent(this,InstructionPage.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
    public void GoToCamera(View view){

        Intent intent= new Intent(this,Camera.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
    public void GoToSOS(View view){

        Intent intent= new Intent(this,SOS.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
    public void GoToPlaces(View view){

        Intent intent= new Intent(this,PLaces.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("type","Toilet.json");
        startActivity(intent);
    }
    public void GoToJharna(View view){

        Intent intent= new Intent(this,PLaces.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("type","Jharna.json");
        startActivity(intent);
    }
    public void GoToPolice(View view){

        Intent intent= new Intent(this,PLaces.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("type", "Police Station.json");
        startActivity(intent);
    }
    public void GoToCtrlRoom(View view){

        Intent intent= new Intent(this,PLaces.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("type","Control Room.json");
        startActivity(intent);
    }
    public void GoToStayPlaces(View view){

        Intent intent= new Intent(this,PLaces.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("type","Stay Place.json");
        startActivity(intent);
    }
    public void GoToNotification(View view){

        Intent intent= new Intent(this,Notification.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
    public void play_pauseMusic(View view){
        Intent svc=new Intent(this, MusicService.class);
        FloatingActionButton floatingActionButton=findViewById(R.id.floatingActionButton2);

        if (isMyServiceRunning(MusicService.class)){
            stopService(svc);
            floatingActionButton.setImageResource(R.drawable.ic_volume_up_black_24dp);

        }else {
            startService(svc);
            floatingActionButton.setImageResource(R.drawable.ic_volume_off_black_24dp);

        }

    }
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
    private void growUpAnim(View view){

        Animation grow = AnimationUtils.loadAnimation(this, R.anim.grow);
        view.setVisibility(View.VISIBLE);
        view.setEnabled(true);
        view.startAnimation(grow);
    }
}
