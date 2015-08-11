package os.running.leaderboard.app.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import os.running.leaderboard.app.R;
import os.running.leaderboard.app.base.AbstractPagerPage;

/**
 * @author Martin "Garth" Zander <garth@new-crusader.de>
 */
public class LiveSessionsPage extends AbstractPagerPage
{
    private LinearLayout mainView = null;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        if (this.mainView == null) {
            this.mainView = (LinearLayout) inflater.inflate(R.layout.leader_board_page, container, false);

            RecyclerView listView = (RecyclerView)mainView.findViewById(R.id.listView);
            listView.setHasFixedSize(true);

            RecyclerView.LayoutManager manager = new LinearLayoutManager(this.getActivity());
            listView.setLayoutManager(manager);

            //LeaderBoardAdapter adapter = new LeaderBoardAdapter();
            //listView.setAdapter(adapter);
        }
        
        return this.mainView;
    }
}
