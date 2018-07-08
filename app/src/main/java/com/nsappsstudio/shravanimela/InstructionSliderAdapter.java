package com.nsappsstudio.shravanimela;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class InstructionSliderAdapter extends PagerAdapter {
    Context context;
    LayoutInflater layoutInflater;

    public InstructionSliderAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull final ViewGroup container, int position) {
        layoutInflater=(LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view=layoutInflater.inflate(R.layout.instruction_slide,container,false);

        ConstraintLayout slide1 =view.findViewById(R.id.slide1);
        ConstraintLayout slide2 =view.findViewById(R.id.slide2);
        ConstraintLayout slide3 =view.findViewById(R.id.slide3);
        switch (position){
            case 0:
                slide1.setVisibility(View.VISIBLE);
                slide2.setVisibility(View.GONE);
                slide3.setVisibility(View.GONE);
                break;
            case 1:
                slide1.setVisibility(View.GONE);
                slide2.setVisibility(View.VISIBLE);
                slide3.setVisibility(View.GONE);
                break;
            case 2:
                slide1.setVisibility(View.GONE);
                slide2.setVisibility(View.GONE);
                slide3.setVisibility(View.VISIBLE);
                break;
        }
        Button nextPage1=view.findViewById(R.id.button3);
        nextPage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((InstructionPage)context).nextPage();
            }
        });
        Button nextPage2=view.findViewById(R.id.button4);
        nextPage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((InstructionPage)context).nextPage();
            }
        });
        Button skipPage1=view.findViewById(R.id.button8);
        skipPage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((InstructionPage)context).goToMain();
            }
        });
        Button skipPage2=view.findViewById(R.id.button5);
        skipPage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((InstructionPage)context).goToMain();
            }
        });

        Button finish=view.findViewById(R.id.button6);
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((InstructionPage)context).goToMain();

            }
        });
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ConstraintLayout)object);
    }
}
