package os.running.leaderboard.app.fragment;

import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import org.json.JSONObject;
import os.running.leaderboard.app.R;
import os.running.leaderboard.app.base.AbstractPagerPage;
import os.running.leaderboard.app.base.LeaderBoardAdapter;
import os.running.leaderboard.app.base.LeaderBoardAdapterData;
import os.running.leaderboard.app.base.LeaderBoardPagerAdapter;

/**
 * @author Martin "Garth" Zander <garth@new-crusader.de>
 */
public class LeaderBoardPage extends AbstractPagerPage
{
    private JSONObject contentData = null;
    private Boolean emptyData = false;
    private String tabType;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        if (this.mainView == null) {
            this.mainView = (LinearLayout) inflater.inflate(R.layout.leader_board_page, container, false);

            RecyclerView listView = (RecyclerView)mainView.findViewById(R.id.listView);
            listView.setHasFixedSize(true);

            RecyclerView.LayoutManager manager = new LinearLayoutManager(this.getActivity());
            listView.setLayoutManager(manager);

            LeaderBoardAdapter adapter = new LeaderBoardAdapter(this);
            adapter.setTabType(this.tabType);
            listView.setAdapter(adapter);

            SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout)mainView.findViewById(R.id.swipeRefreshLayout);
            swipeRefreshLayout.setColorSchemeResources(R.color.app_blue_dark, R.color.app_blue, R.color.app_blue_light);
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
            {
                @Override
                public void onRefresh()
                {
                    try {
                        final ViewPager viewPager = (ViewPager) getActivity().findViewById(R.id.viewpager);
                        LeaderBoardPagerAdapter adapter = (LeaderBoardPagerAdapter) viewPager.getAdapter();
                        adapter.loadContent();
                    } catch (Exception e) {
                        ((SwipeRefreshLayout)mainView.findViewById(R.id.swipeRefreshLayout)).setRefreshing(false);
                    }
                }
            });
        }

        if (this.contentData != null) {
            createContent(this.contentData);
        } else if (emptyData) {
            createEmptyContent();
        }
        
        return this.mainView;
    }
    
    public void setTabType(String type)
    {
        this.tabType = type;
    }
    
    public void setContentData(JSONObject data)
    {
        this.contentData = data;
        
        if (this.mainView != null) {
            createContent(data);
        }
    }

    public void setEmptyContent()
    {
        emptyData = true;
        
        if (this.mainView != null) {
            createEmptyContent();
        }
    }
    
    private void createEmptyContent()
    {
        // hide Progress Bar
        mainView.findViewById(R.id.loadingView).setVisibility(View.GONE);

        // show default empty view
        mainView.findViewById(R.id.emptyView).setVisibility(View.VISIBLE);

        // modified image color
        ImageView image = (ImageView)mainView.findViewById(R.id.emptyImage);
        ColorFilter filter = new LightingColorFilter(Color.parseColor("#6A6A6A"), Color.WHITE);
        image.setColorFilter(filter);
    }
    
    private void createContent(JSONObject data)
    {
        try {
            // hide Progress Bar
            mainView.findViewById(R.id.loadingView).setVisibility(View.GONE);
            
            // load listView adapter
            RecyclerView listView = (RecyclerView)mainView.findViewById(R.id.listView);
            LeaderBoardAdapter adapter = (LeaderBoardAdapter)listView.getAdapter();

            // reset adapter data
            adapter.reset();

            // fill adapter
            for (int i = 0; i < data.getJSONArray("entries").length(); i++) {

                JSONObject member = data.getJSONArray("entries").getJSONObject(i);

                LeaderBoardAdapterData adapterData = new LeaderBoardAdapterData();
                
                adapterData.setNumber(member.getInt("entry_number"));
                adapterData.setUserName(member.getJSONObject("member_data").getString("name"));
                adapterData.setScore(member.getDouble("score"));
                adapterData.setUserId(member.getInt("member"));
                
                adapter.add(adapterData);
            }

            ((SwipeRefreshLayout)mainView.findViewById(R.id.swipeRefreshLayout)).setRefreshing(false);
            
        }  catch (Exception e) {
            Log.e("app", "LeaderBoardPage.createContent: " + e.getMessage());
        }
    }
}
