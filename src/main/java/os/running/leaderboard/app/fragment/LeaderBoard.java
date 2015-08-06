package os.running.leaderboard.app.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import os.running.leaderboard.app.R;
import os.running.leaderboard.app.base.LeaderBoardPagerAdapter;

/**
 * @author Martin "Garth" Zander <garth@new-crusader.de>
 */
public class LeaderBoard extends Fragment
{
    private LinearLayout mainView;

    public LeaderBoard()
    {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        if (this.mainView == null) {
            this.mainView = (LinearLayout) inflater.inflate(R.layout.leader_board, container, false);

            ViewPager viewPager = (ViewPager) mainView.findViewById(R.id.viewpager);
            viewPager.setAdapter(new LeaderBoardPagerAdapter(getActivity().getSupportFragmentManager(), getActivity()));

            TabLayout tabLayout = (TabLayout) mainView.findViewById(R.id.tabLayout);
            tabLayout.removeAllTabs();
            tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
            tabLayout.setVisibility(TabLayout.VISIBLE);
            tabLayout.setupWithViewPager(viewPager);
        }
        
        return this.mainView;
    }
}
