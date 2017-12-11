package com.wanandroid.utils;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

/**
 * Created by ${jay} on ${2016/8/17
 */
public class ActivityUtils {

    private static final String TAG = "ActivityUtils";

    public static void addFragmentToActivity(@NonNull FragmentManager fragmentManager,
                                             @NonNull Fragment fragment, int frameId) {
        addFragmentToActivity(fragmentManager, fragment, frameId, true);
    }

    public static void addFragmentToActivity(@NonNull FragmentManager fragmentManager,
                                             @NonNull Fragment fragment, int frameId, boolean addToBackStack) {
        if (fragment.isAdded()) {
            Log.w(TAG, "addFragmentToActivity: fragment is added:" + fragment.getClass().getName());
            return;
        }
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(frameId, fragment);
        if (addToBackStack) {
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commit();
    }
}
