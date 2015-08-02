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
public class Leaderboard extends Fragment
{
    /**
     * this fragment representing
     */
    private static Leaderboard self = null;

    /**
     * Returns a new instance of this fragment
     */
    public static Leaderboard newInstance()
    {
        if (self == null) {
            self = new Leaderboard();
        }
        
        return self;
    }

    public Leaderboard()
    {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.leaderboard, container, false);
    }
}
