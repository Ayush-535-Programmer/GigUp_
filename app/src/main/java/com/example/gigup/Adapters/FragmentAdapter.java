package com.example.gigup.Adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.gigup.R;
import com.example.gigup.UserHistory;
import com.example.gigup.UserHome;
import com.example.gigup.UserProfile;
import com.example.gigup.UserUpload;

public class FragmentAdapter extends FragmentPagerAdapter {

    public FragmentAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    public FragmentAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: return new UserHome();
            case 1: return new UserUpload();
            case 2: return new UserHistory();
            case 3: return new UserProfile();
            default:return new UserHome();
        }
    }

    @Override
    public int getCount() {
        return 4;
    }



    public int getPageIcon(int position) {
        // Return the resource ID of the icon for the given position
        // For example:
        switch (position) {
            case 0:
                return R.drawable.home_icon;
            case 1:
                return R.drawable.upload_icon;
            case 2:
                return R.drawable.history_icon;
            case 3:
                return R.drawable.profile_icon;
            default:
                return 0;
        }
    }






}
