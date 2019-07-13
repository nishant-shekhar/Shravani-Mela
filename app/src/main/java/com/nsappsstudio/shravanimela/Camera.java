package com.nsappsstudio.shravanimela;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class Camera extends AppCompatActivity implements Camera1.OnFragmentInteractionListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        final androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);

        final TabLayout tabLayout = findViewById(R.id.tablayout);
        tabLayout.addTab(tabLayout.newTab().setText("Camera 1"));
        tabLayout.addTab(tabLayout.newTab().setText("Camera 2"));
        tabLayout.addTab(tabLayout.newTab().setText("Camera 3"));
        tabLayout.addTab(tabLayout.newTab().setText("Camera 4"));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        final Button buttonSetPortrait = findViewById(R.id.exit_full_screen);
        final Button buttonSetLandscape = findViewById(R.id.full_screen);

        buttonSetPortrait.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                toolbar.setVisibility(View.VISIBLE);
                tabLayout.setVisibility(View.VISIBLE);
                buttonSetPortrait.setVisibility(View.GONE);
                buttonSetLandscape.setVisibility(View.VISIBLE);
            }});

        buttonSetLandscape.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                toolbar.setVisibility(View.GONE);
                tabLayout.setVisibility(View.GONE);
                buttonSetPortrait.setVisibility(View.VISIBLE);
                buttonSetLandscape.setVisibility(View.GONE);
            }});

        switch (getResources().getConfiguration().orientation) {
            case Configuration.ORIENTATION_PORTRAIT:
                toolbar.setVisibility(View.VISIBLE);
                tabLayout.setVisibility(View.VISIBLE);
                buttonSetPortrait.setVisibility(View.GONE);
                buttonSetLandscape.setVisibility(View.VISIBLE);
                break;
            case Configuration.ORIENTATION_LANDSCAPE:
                toolbar.setVisibility(View.GONE);
                tabLayout.setVisibility(View.GONE);
                buttonSetPortrait.setVisibility(View.VISIBLE);
                buttonSetLandscape.setVisibility(View.GONE);
                break;
        }


    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    protected void onStop() {
        super.onStop();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_USER);

    }
}
