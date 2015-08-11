package os.running.leaderboard.app.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import org.json.JSONObject;
import os.running.leaderboard.app.Main;
import os.running.leaderboard.app.R;
import os.running.leaderboard.app.base.AbstractPagerPage;
import os.running.leaderboard.app.base.Runtastic;

/**
 * @author Martin "Garth" Zander <garth@new-crusader.de>
 */
public class LiveSessionsPage extends AbstractPagerPage
{
    public final int TYPE_FRIENDS = 0;
    public final int TYPE_WORLD = 1;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        if (this.mainView == null) {
            this.mainView = (LinearLayout) inflater.inflate(R.layout.live_sessions_page, container, false);

            RecyclerView listView = (RecyclerView)mainView.findViewById(R.id.listView);
            listView.setHasFixedSize(true);

            RecyclerView.LayoutManager manager = new LinearLayoutManager(this.getActivity());
            listView.setLayoutManager(manager);

            //LeaderBoardAdapter adapter = new LeaderBoardAdapter();
            //listView.setAdapter(adapter);
        }
        
        return this.mainView;
    }
    
    public void loadData(final int type)
    {
        new createContent().execute(type);
    }
    
    private class createContent extends AsyncTask<Integer, String, String>
    {
        private JSONObject result = null;

        @Override
        protected String doInBackground(Integer... type)
        {
            try {
                Runtastic runtastic = new Runtastic(Main.activity);
                if (type[0] == TYPE_WORLD) {
                    result = runtastic.liveSessions();
                } else {
                    result = runtastic.friendsLiveSessions();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("app", "LiveSessionsPage.createContent" + e.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);

            if (result == null) {
                // TODO add error message
                Log.d("app", "content failed");
                return;
            }

            // TODO: process response
        }
    }
}
