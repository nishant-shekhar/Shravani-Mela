package com.nsappsstudio.shravanimela;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class SlideshowAdapter extends PagerAdapter {
    Context context;
    LayoutInflater layoutInflater;

    public SlideshowAdapter(Context context) {
        this.context = context;
    }
    private int[] imageList={
            R.drawable.baba,R.drawable.baba1,R.drawable.baba2,R.drawable.baba3,R.drawable.baba4,R.drawable.baba5,R.drawable.baba6
    };
    public String[] text={
            "my name is nishant","i am 26 year old","it's nice to meet you"
    };
    @Override
    public int getCount() {
        return imageList.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater=(LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view=layoutInflater.inflate(R.layout.slides,container,false);

        ImageView imageView= view.findViewById(R.id.slide_image);

        imageView.setImageResource(imageList[position]);

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout)object);
    }
}
