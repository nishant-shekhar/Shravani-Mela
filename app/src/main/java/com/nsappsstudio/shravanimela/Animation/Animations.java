package com.nsappsstudio.shravanimela.Animation;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.Transformation;
import android.view.animation.TranslateAnimation;

import com.nsappsstudio.shravanimela.R;


public class Animations {


    public static void expand(final View v) {
        v.measure(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);

        final int targetedHeight = v.getMeasuredHeight();
        final int initialHeight=v.getHeight();
        //v.getLayoutParams().height = 0;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime==1){
                    v.getLayoutParams().height=LayoutParams.WRAP_CONTENT;
                }else {
                    v.getLayoutParams().height=(int) ((targetedHeight-initialHeight) * interpolatedTime)+ initialHeight;

                }
                /*v.getLayoutParams().height = interpolatedTime == 1
                        ? LayoutParams.WRAP_CONTENT
                        : (int) (targtetHeight * interpolatedTime);*/
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        a.setDuration((int) (targetedHeight*3 / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public static void changeHeight(final View v, final int initialHeight, final int targetHeight){

        v.setVisibility(View.VISIBLE);
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height=(int) ((targetHeight-initialHeight) * interpolatedTime)+ initialHeight;
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        a.setDuration(700);
        v.startAnimation(a);
    }
    public static void changeHeightWithDuration(final View v, final int initialHeight, final int targetHeight,int duration){

        v.setVisibility(View.VISIBLE);
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height=(int) ((targetHeight-initialHeight) * interpolatedTime)+ initialHeight;
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        a.setDuration(duration);
        a.setInterpolator(new AccelerateDecelerateInterpolator());
        v.startAnimation(a);
    }
    public static void reduceHeight(final View v, final int initialHeight, final int targetHeight){


        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {

                v.getLayoutParams().height = initialHeight - (int)((initialHeight-targetHeight) * interpolatedTime);
                v.requestLayout();

            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        a.setDuration(500);
        v.startAnimation(a);
    }
    public static void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if(interpolatedTime == 1){
                    v.setVisibility(View.GONE);
                }else{
                    v.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        a.setDuration((int)(initialHeight*3 / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }
    public static void collapseWidthWithDuration(final View v,int duration) {
        final int initialWidth = v.getMeasuredWidth();

        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if(interpolatedTime == 1){
                    v.setVisibility(View.GONE);
                }else{
                    v.getLayoutParams().width = initialWidth - (int)(initialWidth * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        a.setDuration(duration);
        v.startAnimation(a);
    }
    public static void changeWidthWithDuration(final View v, final int initialWidth, final int targetWidth,int duration){

        v.setVisibility(View.VISIBLE);
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().width=(int) ((targetWidth-initialWidth) * interpolatedTime)+ initialWidth;
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        a.setDuration(duration);
        a.setInterpolator(new AccelerateDecelerateInterpolator());
        v.startAnimation(a);
    }
    public static void translate(final View v, long delay, final long duration, final int direction){

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                v.measure(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                Animation animation;
                switch (direction){
                    case 0:
                        //from Left
                        animation = new TranslateAnimation(-v.getMeasuredWidth(), 0,0, 0);
                        break;
                    case 1:
                        //from Right
                        animation = new TranslateAnimation(v.getMeasuredWidth(), 0,0, 0);
                        break;
                    case 2:
                        //from down
                        animation = new TranslateAnimation(0, 0,v.getHeight(), 0);
                        break;
                    default:
                        //from up
                        animation = new TranslateAnimation(0, 0,-v.getHeight(), 0);
                        break;

                }
                animation.setDuration(duration);
                animation.setInterpolator(new DecelerateInterpolator());
                animation.setFillAfter(true);
                v.startAnimation(animation);
                v.setVisibility(View.VISIBLE);
            }
        }, delay);

    }
    public static void translateWithAlpha(final View v, long delay, final long duration, final int direction, final float initialAlpha, final float finalAlpha){

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                v.measure(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                AnimationSet animationSet=new AnimationSet(false);
                Animation translateAnimation;
                switch (direction){
                    case 0:
                        //from Left
                        translateAnimation = new TranslateAnimation(-v.getMeasuredWidth(), 0,0, 0);
                        break;
                    case 1:
                        //from Right
                        translateAnimation = new TranslateAnimation(v.getMeasuredWidth(), 0,0, 0);
                        break;
                    case 2:
                        //from down
                        translateAnimation = new TranslateAnimation(0, 0,v.getHeight(), 0);
                        break;
                    default:
                        //from up
                        translateAnimation = new TranslateAnimation(0, 0,-v.getHeight(), 0);
                        break;

                }
                translateAnimation.setDuration(duration);
                translateAnimation.setInterpolator(new DecelerateInterpolator());
                translateAnimation.setFillAfter(true);
                animationSet.addAnimation(translateAnimation);

                Animation alphaAnim=new AlphaAnimation(initialAlpha,finalAlpha);
                alphaAnim.setDuration(duration);
                alphaAnim.setInterpolator(new AccelerateInterpolator());
                alphaAnim.setFillAfter(true);
                animationSet.addAnimation(alphaAnim);

                v.startAnimation(animationSet);
                v.setVisibility(View.VISIBLE);
            }
        }, delay);

    }
    public static void goneTranslateWithAlpha(final View v, long delay, final long duration, final int direction, final float initialAlpha, final float finalAlpha){

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                v.measure(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                AnimationSet animationSet=new AnimationSet(false);
                Animation translateAnimation;
                switch (direction){
                    case 0:
                        //from Left
                        translateAnimation = new TranslateAnimation(0,-v.getMeasuredWidth(), 0, 0);
                        break;
                    case 1:
                        //from Right
                        translateAnimation = new TranslateAnimation(0,v.getMeasuredWidth(), 0, 0);
                        break;
                    case 2:
                        //from down
                        translateAnimation = new TranslateAnimation(0, 0, 0,v.getHeight());
                        break;
                    default:
                        //from up
                        translateAnimation = new TranslateAnimation(0, 0, 0,-v.getHeight());
                        break;

                }
                translateAnimation.setDuration(duration);
                translateAnimation.setInterpolator(new DecelerateInterpolator());
                translateAnimation.setFillAfter(true);
                animationSet.addAnimation(translateAnimation);

                Animation alphaAnim=new AlphaAnimation(initialAlpha,finalAlpha);
                alphaAnim.setDuration(duration);
                alphaAnim.setInterpolator(new AccelerateInterpolator());
                alphaAnim.setFillAfter(true);
                animationSet.addAnimation(alphaAnim);

                v.startAnimation(animationSet);
                v.setVisibility(View.VISIBLE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        v.setVisibility(View.GONE);
                    }
                },duration);
            }
        }, delay);

    }
    public static void alphaAnim(final View v, long delay, final long duration, final float initialAlpha, final float finalAlpha){

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Animation alphaAnim=new AlphaAnimation(initialAlpha,finalAlpha);
                alphaAnim.setDuration(duration);
                alphaAnim.setInterpolator(new AccelerateInterpolator());
                alphaAnim.setFillAfter(true);

                v.startAnimation(alphaAnim);
                //v.setVisibility(View.VISIBLE);
            }
        }, delay);

    }
    public static void scale(View v, float startScale, float endScale,float pivotX,float pivotY,int duration) {
        Animation anim = new ScaleAnimation(
                startScale, endScale, // Start and end values for the X axis scaling
                startScale, endScale, // Start and end values for the Y axis scaling
                Animation.RELATIVE_TO_SELF, pivotX, // Pivot point of X scaling
                Animation.RELATIVE_TO_SELF, pivotY); // Pivot point of Y scaling
        anim.setFillAfter(true); // Needed to keep the result of the animation
        anim.setDuration(duration);
        anim.setInterpolator(new DecelerateInterpolator());
        v.startAnimation(anim);
    }

    public static void scaleWithAlpha(View v, float startScale, float endScale,float pivotX,float pivotY,int duration) {
        AnimationSet animationSet=new AnimationSet(false);
        Animation anim = new ScaleAnimation(
                startScale, endScale, // Start and end values for the X axis scaling
                startScale, endScale, // Start and end values for the Y axis scaling
                Animation.RELATIVE_TO_SELF, pivotX, // Pivot point of X scaling
                Animation.RELATIVE_TO_SELF, pivotY); // Pivot point of Y scaling
        anim.setFillAfter(true); // Needed to keep the result of the animation
        anim.setDuration(duration);
        anim.setInterpolator(new DecelerateInterpolator());

        Animation alphaAnim=new AlphaAnimation(0f,1f);
        alphaAnim.setDuration(duration/2);
        alphaAnim.setInterpolator(new AccelerateInterpolator());
        alphaAnim.setFillAfter(true);
        animationSet.addAnimation(anim);
        animationSet.addAnimation(alphaAnim);

        v.startAnimation(animationSet);
        v.setVisibility(View.VISIBLE);
    }
    public static void shake(View v,  Context context){
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.shake);
        v.startAnimation(animation);
    }
    public static void wobble(View v,  Context context){
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.wobble);
        v.startAnimation(animation);
    }
    public static void squeeze(View v,  Context context){
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.squeeze);
        v.startAnimation(animation);
    }
    public static void squeezeLittle(View v,  Context context){
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.squeeze_little);
        v.startAnimation(animation);
    }
    public static void release(View v,  Context context){
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.release);
        v.startAnimation(animation);
    }
}
