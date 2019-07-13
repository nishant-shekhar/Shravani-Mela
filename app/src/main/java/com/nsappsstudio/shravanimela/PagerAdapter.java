package com.nsappsstudio.shravanimela;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class PagerAdapter extends FragmentStatePagerAdapter {



    int mTabCount;

    public PagerAdapter(FragmentManager fm, int mTabCount) {
        super(fm);
        this.mTabCount = mTabCount;
    }
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                Camera1 camera1= new Camera1();
                Bundle bundle = new Bundle();
                bundle.putInt("camera", 1);
                camera1.setArguments(bundle);
                return camera1;
            case 1:
                Camera1 camera2= new Camera1();
                Bundle bundle2 = new Bundle();
                bundle2.putInt("camera", 2);
                camera2.setArguments(bundle2);
                return camera2;
            case 2:
                Camera1 camera3= new Camera1();
                Bundle bundle3 = new Bundle();
                bundle3.putInt("camera", 3);
                camera3.setArguments(bundle3);
                return camera3;
            case 3:
                Camera1 camera4= new Camera1();
                Bundle bundle4 = new Bundle();
                bundle4.putInt("camera", 4);
                camera4.setArguments(bundle4);
                return camera4;
            default:
                return null;

        }
    }

    @Override
    public int getCount() {
        return mTabCount;
    }
}
