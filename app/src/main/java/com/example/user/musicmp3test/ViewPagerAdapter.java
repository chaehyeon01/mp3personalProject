package com.example.user.musicmp3test;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

public class ViewPagerAdapter extends FragmentPagerAdapter {


    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: return MusicActivty.newInstance() ;
            case 1: return SubMusicActivity.newInstance() ;
            default: return null;

        }

    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        switch (position){
            case 0: return "MUSIC LIST";
            case 1: return "My MUSIC";
            default: return null;


        }

    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        if (object.getClass().equals(SubMusicActivity.class)) {
            return POSITION_NONE;
        } else {
            return super.getItemPosition(object);
        }

    }
}
