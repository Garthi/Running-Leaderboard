package os.running.leaderboard.app.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import os.running.leaderboard.app.R;

/**
 * @author Martin "Garth" Zander <garth@new-crusader.de>
 */
public class Friends extends Fragment
{
    private LinearLayout mainView;
    
    public Friends()
    {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        this.mainView = (LinearLayout)inflater.inflate(R.layout.friends, container, false);

        return this.mainView;
    }
}
