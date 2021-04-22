package com.example.hospitalwaka.Sliders;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.hospitalwaka.Sliders.FragmentA;
import com.example.hospitalwaka.Sliders.FragmentB;
import com.example.hospitalwaka.Sliders.FragmentC;

public class viewPagerAdapter extends FragmentPagerAdapter {
    public viewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:  return new FragmentA();
            case 1:  return new FragmentB();
            case 2:  return new FragmentC();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
