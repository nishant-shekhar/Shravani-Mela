package com.nsappsstudio.shravanimela;

import android.os.CountDownTimer;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


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
    private void growUpAnim(View view){

        Animation grow = AnimationUtils.loadAnimation(this, R.anim.grow);
        view.setVisibility(View.VISIBLE);
        view.setEnabled(true);
        view.startAnimation(grow);
    }
}
