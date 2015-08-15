package os.running.leaderboard.app.fragment;

import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import org.json.JSONArray;
import org.json.JSONObject;
import os.running.leaderboard.app.Main;
import os.running.leaderboard.app.R;
import os.running.leaderboard.app.base.AbstractPagerPage;
import os.running.leaderboard.app.base.LiveSessionAdapter;
import os.running.leaderboard.app.base.LiveSessionAdapterData;
import os.running.leaderboard.app.base.Runtastic;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Martin "Garth" Zander <garth@new-crusader.de>
 */
public class LiveSessionsPage extends AbstractPagerPage
{
    public final int TYPE_FRIENDS = 0;
    public final int TYPE_WORLD = 1;
    private LiveSessionAdapter adapter = null;
    protected Map<Integer, LiveSessionAdapterData> data = new HashMap<Integer, LiveSessionAdapterData>();
    private boolean dataLoaded = false;
    private int pageType = TYPE_FRIENDS;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        if (this.mainView == null) {
            this.mainView = (LinearLayout) inflater.inflate(R.layout.live_sessions_page, container, false);

            RecyclerView listView = (RecyclerView)mainView.findViewById(R.id.listView);
            listView.setHasFixedSize(true);

            RecyclerView.LayoutManager manager = new LinearLayoutManager(this.getActivity());
            listView.setLayoutManager(manager);

            adapter = new LiveSessionAdapter();
            listView.setAdapter(adapter);

            SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout)mainView.findViewById(R.id.swipeRefreshLayout);
            swipeRefreshLayout.setColorSchemeResources(R.color.app_blue_dark, R.color.app_blue, R.color.app_blue_light);
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
            {
                @Override
                public void onRefresh()
                {
                    new createContent().execute(pageType);
                }
            });

            if (data != null && data.size() > 0) {
                for (LiveSessionAdapterData sessionData: data.values()) {
                    disableLoadingView();
                    adapter.add(sessionData);
                }
            } else if (dataLoaded) {
                createEmptyContent();
            }
        }
        
        return this.mainView;
    }
    
    private void disableLoadingView()
    {
        mainView.findViewById(R.id.loadingView).setVisibility(View.GONE);
    }
    
    private void createEmptyContent()
    {
        disableLoadingView();
        
        // show default empty view
        mainView.findViewById(R.id.emptyView).setVisibility(View.VISIBLE);
        
        // modified image color
        ImageView image = (ImageView)mainView.findViewById(R.id.emptyImage);
        ColorFilter filter = new LightingColorFilter(Color.parseColor("#6A6A6A"), Color.WHITE);
        image.setColorFilter(filter);
    }
    
    public void loadData(final int type)
    {
        pageType = type;
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
                Log.e("app", "LiveSessionsPage.createContent " + e.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);

            if (result == null) {
                Log.d("app", "content failed");

                if (adapter != null) {
                    createEmptyContent();
                }
                
                return;
            }

            // process response
            try {
                JSONArray sessions = result.getJSONArray("sessions");

                if (adapter != null) {
                    disableLoadingView();
                    adapter.reset();
                }
                
                for (int i = 0; i < sessions.length(); i++) {
                    JSONObject session = sessions.getJSONObject(i);

                    LiveSessionAdapterData sessionData = new LiveSessionAdapterData();

                    sessionData.setUserName(session.getString("user"));
                    sessionData.setUserAvatarUrl(session.getString("avatarUrl"));
                    sessionData.setUserUrl(session.getString("url"));
                    sessionData.setUserSessionUrl(session.getString("sessionUrl"));
                    sessionData.setText(session.getString("text"));

                    data.put(i, sessionData);
                    
                    if (adapter != null) {
                        adapter.add(sessionData);
                    }
                }

                if (adapter != null) {
                    SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout)mainView.findViewById(R.id.swipeRefreshLayout);
                    swipeRefreshLayout.setRefreshing(false);
                }
            } catch (Exception e) {
                Log.e("app", "LiveSessionsPage.createContent.onPostExecute: " + e.getMessage());
            }
            
            dataLoaded = true;
        }
    }
}
