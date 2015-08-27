package os.running.leaderboard.app.fragment;

import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import os.running.leaderboard.app.base.FriendsAdapter;
import os.running.leaderboard.app.base.FriendsAdapterData;
import os.running.leaderboard.app.base.Runtastic;

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
        if (this.mainView == null) {
            this.mainView = (LinearLayout) inflater.inflate(R.layout.friends, container, false);

            RecyclerView listView = (RecyclerView)mainView.findViewById(R.id.listView);
            listView.setHasFixedSize(true);

            RecyclerView.LayoutManager manager = new LinearLayoutManager(this.getActivity());
            listView.setLayoutManager(manager);

            FriendsAdapter adapter = new FriendsAdapter(this);
            listView.setAdapter(adapter);

            SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout)mainView.findViewById(R.id.swipeRefreshLayout);
            swipeRefreshLayout.setColorSchemeResources(R.color.app_blue_dark, R.color.app_blue, R.color.app_blue_light);
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
            {
                @Override
                public void onRefresh()
                {
                    try {
                        new createContent().execute();
                    } catch (Exception e) {
                        ((SwipeRefreshLayout) mainView.findViewById(R.id.swipeRefreshLayout)).setRefreshing(false);
                    }
                }
            });

            new createContent().execute();
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
    
    private class createContent extends AsyncTask<Integer, String, String>
    {
        private JSONObject result = null;

        @Override
        protected String doInBackground(Integer... type)
        {
            try {
                Runtastic runtastic = new Runtastic(Main.activity);
                result = runtastic.friends();
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("app", "Friends.createContent " + e.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);

            if (result == null) {
                Log.d("app", "content failed");
                createEmptyContent();

                return;
            }

            // process response
            try {
                JSONArray sessions = result.getJSONArray("friends");

                FriendsAdapter adapter = (FriendsAdapter)((RecyclerView)mainView.findViewById(R.id.listView)).getAdapter();
                
                if (adapter == null) {
                    createEmptyContent();
                    return;
                }
                
                disableLoadingView();
                adapter.reset();

                for (int i = 0; i < sessions.length(); i++) {
                    JSONObject session = sessions.getJSONObject(i);

                    FriendsAdapterData friendsData = new FriendsAdapterData();

                    friendsData.setUserName(session.getString("user"));
                    friendsData.setUserAvatarUrl(session.getString("avatarUrl"));
                    friendsData.setUserUrl(session.getString("url"));

                    adapter.add(friendsData);
                }
                
                SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout)mainView.findViewById(R.id.swipeRefreshLayout);
                swipeRefreshLayout.setRefreshing(false);
                
            } catch (Exception e) {
                Log.e("app", "Friends.createContent.onPostExecute: " + e.getMessage());
            }
        }
    }
}
