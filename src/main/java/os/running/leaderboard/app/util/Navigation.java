package os.running.leaderboard.app.util;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import os.running.leaderboard.app.R;

/**
 * navigation utility class
 */
public class Navigation
{
    private static Navigation instance;

    public static int MODE_ADD = 0;
    public static int MODE_REPLACE = 1;

    private Fragment fromFragment;
    private AppCompatActivity fromActivity;
    private String toFragment;

    private int mode = MODE_ADD;

    private Navigation()
    {}

    private static Navigation getInstance()
    {
        if (instance == null) {
            instance = new Navigation();
        }

        instance.reset();

        return instance;
    }

    private void reset()
    {
        fromFragment = null;
        fromActivity = null;
        toFragment = null;
    }

    public static Navigation from(Fragment fragment)
    {
        Navigation navigation = getInstance();

        navigation.fromFragment = fragment;

        return navigation;
    }

    public static Navigation from(AppCompatActivity activity)
    {
        Navigation navigation = getInstance();

        navigation.fromActivity = activity;

        return navigation;
    }

    public static Navigation from()
    {
        return getInstance();
    }

    public Boolean to(String fragment)
    {
        toFragment = fragment;

        return execute();
    }

    public Boolean replace(String fragment)
    {
        toFragment = fragment;
        mode = MODE_REPLACE;

        return execute();
    }

    private Boolean execute()
    {
        Fragment newFragment;

        try {
            // create the new fragment
            newFragment = (Fragment)Class.forName(toFragment).newInstance();
        } catch (Exception e) {
            // TODO add logging
            return false;
        }

        FragmentTransaction fragmentTransaction;

        // start navigation transaction
        if (fromFragment != null) {
            fragmentTransaction = fromFragment.getActivity().getSupportFragmentManager().beginTransaction();
        } else if (fromActivity != null) {
            fragmentTransaction = fromActivity.getSupportFragmentManager().beginTransaction();
        } else {
            return false;
        }

        // hide the old fragment
        if (fromFragment != null && mode == MODE_ADD) {
            fragmentTransaction.hide(fromFragment);
        }

        // add / replace the new fragment
        if (mode == MODE_REPLACE) {
            fragmentTransaction.replace(R.id.content, newFragment);
        } else if (mode == MODE_ADD) {
            fragmentTransaction.add(R.id.content, newFragment);
        } else {
            return false;
        }

        fragmentTransaction.addToBackStack(null);

        fragmentTransaction.commit();

        return true;
    }
}
