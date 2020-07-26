package com.example.smartsocket.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.smartsocket.Fragments.Tab1Fragment;
import com.example.smartsocket.Fragments.Tab2Fragment;
import com.example.smartsocket.Fragments.Tab3Fragment;

public class PagerAdapter extends FragmentPagerAdapter {
    private  int numOfTabs;
    public PagerAdapter(FragmentManager fm , int numOfTabs){
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.numOfTabs = numOfTabs;
    }
    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new Tab1Fragment();
            case 1:
                return new Tab2Fragment();
            case 2:
                return new Tab3Fragment();
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Meter";
            case 1:
                return "Status";
            case 2:
                return "Charts";
        }
        return null;
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
