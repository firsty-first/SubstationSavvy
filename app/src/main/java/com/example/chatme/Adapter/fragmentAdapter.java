package com.example.chatme.Adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.chatme.calllFragment;
import com.example.chatme.chatFragment;
import com.example.chatme.storyFragment;

public class fragmentAdapter extends FragmentPagerAdapter {

    public fragmentAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    //public fragmentAdapter(@NonNull FragmentManager fm, int behavior) {
      //  super(fm, behavior);
   // }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0: return new chatFragment();
            case 1: return new storyFragment();
            case 2: return new calllFragment();
            default:return new chatFragment();


        }

    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title=null;
        if(position==0)
            title="chats";
        else if(position==1)
            title="status";
        else if (position==2) {
            title = "calls";
        }
        return title;
    }
}
