package com.stosh.discountstorage.refactoring.utils;

import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.util.Locale;

/**
 * @author Ruslan Stosyk
 *         Date: November, 24, 2017
 *         Time: 4:39 PM
 */
public class FragmentUtil {

    public static void addFragment(FragmentManager fragmentManager,
                                   Fragment fragment,
                                   int layoutId,
                                   boolean addToBackStack,
                                   @Nullable Integer animFadeIn,
                                   @Nullable Integer animFadeOut) {
        String tag = fragment.getClass().getSimpleName();
        if (fragmentManager.findFragmentByTag(tag) == null) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            if (animFadeIn != null) transaction.setCustomAnimations(animFadeIn, animFadeOut);
            transaction.add(layoutId, fragment, tag);
            if (addToBackStack) transaction.addToBackStack(null);
            transaction.commit();
        }
    }


    public static void replaceFragment(FragmentManager fragmentManager,
                                       Fragment fragment,
                                       int layoutId,
                                       boolean addToBackStack,
                                       @Nullable Integer animFadeIn,
                                       @Nullable Integer animFadeOut,
                                       @Nullable Integer animFadePopIn,
                                       @Nullable Integer animFadePopOut) {
        String tag = fragment.getClass().getSimpleName();
        if (fragmentManager.findFragmentByTag(tag) == null) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            if (animFadePopIn != null)
                transaction.setCustomAnimations(animFadeIn, animFadeOut, animFadePopIn, animFadePopOut);
            else if (animFadeIn != null)
                transaction.setCustomAnimations(animFadeIn, animFadeOut);
            transaction.replace(layoutId, fragment, tag);
            if (addToBackStack) transaction.addToBackStack(null);
            transaction.commit();
        }
    }


    @Nullable
    public static Fragment getCurrentFragment(FragmentManager fragmentManager,
                                              @IdRes int layoutId) {
        return fragmentManager.findFragmentById(layoutId);
    }

    @Nullable
    public static Fragment getCurrentFragment(FragmentManager fragmentManager,
                                              int layoutId,
                                              int position) {
        return fragmentManager.findFragmentByTag(String.format(
                Locale.getDefault(),
                "android:switcher:%d:%d",
                layoutId, position));
    }

}