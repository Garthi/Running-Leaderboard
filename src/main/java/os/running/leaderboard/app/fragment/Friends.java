package os.running.leaderboard.app.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import os.running.leaderboard.app.R;

/**
 * @author Martin "Garth" Zander <garth@new-crusader.de>
 */
public class Friends extends Fragment
{
    /**
     * this fragment representing
     */
    private static Friends self = null;

    /**
     * Returns a new instance of this fragment
     */
    public static Friends newInstance()
    {
        if (self == null) {
            self = new Friends();
        }
        
        return self;
    }

    public Friends()
    {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.friends, container, false);
    }
}
