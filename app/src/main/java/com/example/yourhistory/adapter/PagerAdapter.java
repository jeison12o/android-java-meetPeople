package com.example.yourhistory.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import com.example.yourhistory.Fragment.FragmentHistoryUser;
import com.example.yourhistory.Fragment.FragmentPhotosUser;

public class PagerAdapter extends FragmentPagerAdapter {

    int numberTabs;

    public PagerAdapter(FragmentManager fm, int numberTabs) {
        super(fm);
        this.numberTabs=numberTabs;
    }


    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position){
            case 0:
                fragment = new FragmentPhotosUser();
            break;
            case 1:
                fragment = new FragmentHistoryUser();
            break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return numberTabs;
    }
}
