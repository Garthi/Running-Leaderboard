package os.running.leaderboard.app.base;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import os.running.leaderboard.app.R;
import os.running.leaderboard.app.fragment.LiveSessionsPage;

/**
 * @author Martin "Garth" Zander <garth@new-crusader.de>
 */
public class LiveSessionsPagerAdapter extends AbstractPagerAdapter
{
    public LiveSessionsPagerAdapter(FragmentManager fm, Context context)
    {
        super(fm, context);
        
        PAGE_COUNT = 2;
        tabTitles = new Integer[] {
            R.string.live_sessions_friends,
            R.string.live_sessions_world
        };
    }
    
    @Override
    public Fragment getItem(int position)
    {
        LiveSessionsPage fragment = new LiveSessionsPage();
        
        this.fragment.put(position, fragment);

        return fragment;
    }
}
