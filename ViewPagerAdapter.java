package com.example.suitsappication;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;


public class ViewPagerAdapter extends FragmentStateAdapter {
    public ViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle); }
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) { case 0:
            return new HomeFragment(); case 1:
            return new PurchasedFragment(); case 2:
            return new ProfileFragment(); default:
            return new HomeFragment(); }
    }
    @Override
    public int getItemCount() {
        return 3; // Number of Fragments //
    }}