package os.running.leaderboard.app.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import os.running.leaderboard.app.Main;
import os.running.leaderboard.app.R;

/**
 * @author Martin "Garth" Zander <garth@new-crusader.de>
 * @package gradle.android
 */
public class Leaderboard extends Fragment
{
    /**
     * The fragment argument representing the section number for this
     * fragment.
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

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);

        //((Main)activity).onSectionAttached(getArguments().getInt(ARG_SECTION_NUMBER));
    }
}
