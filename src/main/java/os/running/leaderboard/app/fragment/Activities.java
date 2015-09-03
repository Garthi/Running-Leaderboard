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
import os.running.leaderboard.app.base.ActivitiesAdapter;
import os.running.leaderboard.app.base.ActivitiesAdapterData;
import os.running.leaderboard.app.base.Runtastic;

/**
 * @author Martin "Garth" Zander <garth@new-crusader.de>
 */
public class Activities extends Fragment
{
    private LinearLayout mainView;
    private int userId;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        if (this.getArguments() != null) {
            userId = this.getArguments().getInt("userId");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        if (this.mainView == null) {
            this.mainView = (LinearLayout) inflater.inflate(R.layout.activities, container, false);

            RecyclerView listView = (RecyclerView)mainView.findViewById(R.id.listView);
            listView.setHasFixedSize(true);
            // TODO add load more listener

            RecyclerView.LayoutManager manager = new LinearLayoutManager(this.getActivity());
            listView.setLayoutManager(manager);

            ActivitiesAdapter adapter = new ActivitiesAdapter(this);
            listView.setAdapter(adapter);

            SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout)mainView.findViewById(R.id.swipeRefreshLayout);
            swipeRefreshLayout.setColorSchemeResources(R.color.app_blue_dark, R.color.app_blue, R.color.app_blue_light);
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
            {
                @Override
                public void onRefresh()
                {
                    try {
                        new createContent().execute(false);
                    } catch (Exception e) {
                        ((SwipeRefreshLayout) mainView.findViewById(R.id.swipeRefreshLayout)).setRefreshing(false);
                    }
                }
            });

            new createContent().execute();
        }
        
        //createEmptyContent();
        
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

    private class createContent extends AsyncTask<Boolean, Boolean, JSONObject>
    {
        private boolean reset = true;
        
        @Override
        protected JSONObject doInBackground(Boolean... type)
        {
            try {
                
                if (type.length > 0 && type[0]) {
                    reset = false;
                }
                
                Runtastic runtastic = new Runtastic(Main.activity);
                
                if (userId > 0) {
                    return runtastic.activities(userId);
                } else {
                    return runtastic.activities();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("app", "Activities.createContent " + e.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(JSONObject result)
        {
            super.onPostExecute(result);

            if (result == null) {
                Log.d("app", "content failed");
                createEmptyContent();

                return;
            }

            // process response
            try {
                JSONArray activities = result.getJSONArray("activities");

                ActivitiesAdapter adapter = (ActivitiesAdapter)((RecyclerView)mainView.findViewById(R.id.listView)).getAdapter();

                if (adapter == null) {
                    createEmptyContent();
                    return;
                }

                disableLoadingView();
                if (reset) {
                    adapter.reset();
                }

                for (int i = 0; i < activities.length(); i++) {
                    JSONObject activity = activities.getJSONObject(i);

                    ActivitiesAdapterData activityData = new ActivitiesAdapterData();

                    activityData.setUserName(activity.getString("user"));
                    activityData.setUserAvatarUrl(activity.getString("avatarUrl"));
                    activityData.setUserUrl(activity.getString("url"));

                    activityData.setDate(activity.getString("date"));
                    activityData.setSocialId(activity.getString("socialId"));
                    activityData.setActivityId(activity.getString("activityId"));

                    if (activity.has("sport_type")) {
                        activityData.setSportType(activity.getString("sport_type"));
                    }
                    if (activity.has("distance")) {
                        activityData.setDistance(activity.getString("distance"));
                    }
                    if (activity.has("duration")) {
                        activityData.setDuration(activity.getString("duration"));
                    }
                    if (activity.has("icons")) {
                        for (int iconIndex = 0; iconIndex < activity.getJSONArray("icons").length(); iconIndex++) {
                            activityData.addIcon(activity.getJSONArray("icons").getString(iconIndex));
                        }
                    }
                    if (activity.has("notes")) {
                        activityData.setNotes(activity.getString("notes"));
                    }
                    if (activity.has("mapUrl")) {
                        activityData.setMapUrl(activity.getString("mapUrl"));
                    }

                    adapter.add(activityData);
                }

                SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout)mainView.findViewById(R.id.swipeRefreshLayout);
                swipeRefreshLayout.setRefreshing(false);

            } catch (Exception e) {
                Log.e("app", "Activities.createContent.onPostExecute: " + e.getMessage());
            }
        }
    }
}
