package com.example.mr.khan2;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by MR on 11/28/2017.
 */

class SectionsPagerAdapter extends FragmentPagerAdapter {
    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
                ChatsFragment chatsFragment = new ChatsFragment();
                return  chatsFragment;
            case 1:
                FriendsFragment friendsFragment = new FriendsFragment();
                return  friendsFragment;


            default:
                return null;



        }


    }

    @Override
    public int getCount() {
        return 2;
    }

    public CharSequence getPageTitle(int position){

        switch (position){
            case 0:
                return "Chats";
            case 1:
                return "Contacts";


            default:
                return null;

        }

    }
}
