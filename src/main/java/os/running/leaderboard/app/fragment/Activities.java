package os.running.leaderboard.app.fragment;

import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import os.running.leaderboard.app.R;

/**
 * @author Martin "Garth" Zander <garth@new-crusader.de>
 */
public class Activities extends Fragment
{
    private LinearLayout mainView;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        if (this.mainView == null) {
            this.mainView = (LinearLayout) inflater.inflate(R.layout.activities, container, false);

            RecyclerView listView = (RecyclerView)mainView.findViewById(R.id.listView);
            listView.setHasFixedSize(true);

            RecyclerView.LayoutManager manager = new LinearLayoutManager(this.getActivity());
            listView.setLayoutManager(manager);

            SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout)mainView.findViewById(R.id.swipeRefreshLayout);
            swipeRefreshLayout.setColorSchemeResources(R.color.app_blue_dark, R.color.app_blue, R.color.app_blue_light);
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
            {
                @Override
                public void onRefresh()
                {
                    try {
                        // TODO refresh content
                    } catch (Exception e) {
                        ((SwipeRefreshLayout) mainView.findViewById(R.id.swipeRefreshLayout)).setRefreshing(false);
                    }
                }
            });

            // TODO load content
        }
        
        createEmptyContent();
        
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
}
