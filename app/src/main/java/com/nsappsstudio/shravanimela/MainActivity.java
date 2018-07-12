package com.nsappsstudio.shravanimela;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Switch;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private DatabaseReference mDatabaseReference;
    private List<CrowdItemList> crowdItemLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent serviceIntent = new Intent(this, NotificationServices.class);
        startService(serviceIntent);
        
        mDatabaseReference= FirebaseDatabase.getInstance().getReference();

        mDrawerLayout=findViewById(R.id.drawer_layout);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        mDrawerLayout.closeDrawers();
                        Toast.makeText(MainActivity.this,String.valueOf(menuItem),Toast.LENGTH_SHORT).show();
                        int id= menuItem.getItemId();
                        loadFromMenu(id);


                        return true;
                    }
                });
        /*AppBarLayout.OnOffsetChangedListener mListener = new AppBarLayout.OnOffsetChangedListener() {
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
        appBar.addOnOffsetChangedListener(mListener);*/

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

        loadCrowdStatus();

    }

    private void loadFromMenu(int id) {
        switch (id){
            case R.id.nav_doctor:

                break;
            case R.id.nav_ambulance:

                break;
            case R.id.nav_mela_helpline:

                break;
            case R.id.nav_disaster_helpline:

                break;
            case R.id.nav_electricity_helpline:
                Intent intent= new Intent(this,Electric.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.nav_food_inspector:

                break;
            case R.id.nav_food_rate:

                break;
            case R.id.nav_puja_samagari:

                break;
            case R.id.nav_medicine:

                break;
            case R.id.nav_waterfall:

                break;
            case R.id.nav_drinking_water:

                break;

        }
    }

    private void loadCrowdStatus(){
        final RecyclerView recyclerView=findViewById(R.id.crowd_recyclerview);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        crowdItemLists = new ArrayList<>();
        DatabaseReference mCrowdRef= mDatabaseReference.child("CrowdStatus").child("Live");
        mCrowdRef.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                
                String place=dataSnapshot.getKey();
                String crowdLevel=dataSnapshot.child("level").getValue(String.class);
                String date = null;

                try {
                    Long timeStamp= dataSnapshot.child("ts").getValue(Long.class);

                    CharSequence ago =
                            DateUtils.getRelativeDateTimeString(MainActivity.this, timeStamp, DateUtils.MINUTE_IN_MILLIS, DateUtils.WEEK_IN_MILLIS,0);

                    //DateUtils.getRelativeTimeSpanString(timeStamp, now, DateUtils.MINUTE_IN_MILLIS);
                    date=String.valueOf(ago);

                }catch (NullPointerException e){
                    //do nothing
                }

                Toast.makeText(MainActivity.this, date, Toast.LENGTH_LONG).show();

                CrowdItemList crowdItemList=new CrowdItemList(date,place,crowdLevel);
                crowdItemLists.add(crowdItemList);
                RecyclerView.Adapter adapter = new CrowdAdapter(crowdItemLists, MainActivity.this);
                recyclerView.setAdapter(adapter);
                
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        

       


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
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

        Intent intent= new Intent(this,Table.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
    /*public void play_pauseMusic(View view){
        Intent svc=new Intent(this, MusicService.class);
        FloatingActionButton floatingActionButton=findViewById(R.id.floatingActionButton2);

        if (isMyServiceRunning(MusicService.class)){
            stopService(svc);
            floatingActionButton.setImageResource(R.drawable.ic_volume_up_black_24dp);

        }else {
            startService(svc);
            floatingActionButton.setImageResource(R.drawable.ic_volume_off_black_24dp);

        }

    }*/
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
