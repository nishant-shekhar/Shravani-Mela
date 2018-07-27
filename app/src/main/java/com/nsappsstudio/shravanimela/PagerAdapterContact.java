package com.nsappsstudio.shravanimela;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class PagerAdapterContact extends FragmentStatePagerAdapter {



    int mTabCount;

    public PagerAdapterContact(FragmentManager fm, int mTabCount) {
        super(fm);
        this.mTabCount = mTabCount;
    }
    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
                ContactFrag contactFrag= new ContactFrag();
                Bundle bundle = new Bundle();
                bundle.putString("filename", "Bhagalpur Contact.json");
                contactFrag.setArguments(bundle);
                return contactFrag;
            case 1:
                ContactFrag contactFrag2= new ContactFrag();
                Bundle bundle2 = new Bundle();
                bundle2.putString("filename", "Munger Contact.json");
                contactFrag2.setArguments(bundle2);
                return contactFrag2;
            case 2:
                ContactFrag contactFrag3= new ContactFrag();
                Bundle bundle3 = new Bundle();
                bundle3.putString("filename", "Banka Contact.json");
                contactFrag3.setArguments(bundle3);
                return contactFrag3;

            default:
                return null;

        }
    }

    @Override
    public int getCount() {
        return mTabCount;
    }
}
