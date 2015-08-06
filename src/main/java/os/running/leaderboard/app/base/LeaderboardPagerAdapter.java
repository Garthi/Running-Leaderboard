package os.running.leaderboard.app.base;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONObject;
import os.running.leaderboard.app.R;
import os.running.leaderboard.app.fragment.LeaderBoardPage;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Martin "Garth" Zander <garth@new-crusader.de>
 */
public class LeaderBoardPagerAdapter extends FragmentPagerAdapter
{
    final int PAGE_COUNT = 4;
    
    private Integer tabTitles[] = new Integer[] {
            R.string.leader_board_this_week,
            R.string.leader_board_last_week,
            R.string.leader_board_this_month,
            R.string.leader_board_last_month
    };
    private Context context;
    
    private Map<Integer, LeaderBoardPage> fragment = new HashMap<Integer, LeaderBoardPage>();
    
    private Map<Integer, JSONObject> data = new HashMap<Integer, JSONObject>();

    public LeaderBoardPagerAdapter(FragmentManager fm, Context context)
    {
        super(fm);
        this.context = context;
        
        new createContent().execute();
    }

    @Override
    public int getCount()
    {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position)
    {
        LeaderBoardPage fragment = LeaderBoardPage.newInstance(position + 1);
        
        this.fragment.put(position, fragment);

        if (data != null && data.size() >= position) {
            fragment.setContentData(data.get(position));
        }
        
        return fragment;
    }

    @Override
    public CharSequence getPageTitle(int position)
    {
        // Generate title based on item position
        return context.getString(tabTitles[position]);
    }

    private void createContent(JSONObject result)
    {
        if (result == null) {
            return;
        }

        try {
            JSONArray data = result.getJSONArray("data");

            for (int i = 0; i < data.length(); i++) {

                JSONObject entry = data.getJSONObject(i);

                this.data.put(i, entry);
                
                if (this.fragment != null && this.fragment.size() >= i) {
                    LeaderBoardPage fragment = this.fragment.get(i);
                    if (fragment != null) {
                        fragment.setContentData(entry);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("app", "LeaderBoardPagerAdapter.createContent(2): " + e.getMessage());
        }
    }

    private class createContent extends AsyncTask<String, String, String>
    {
        private JSONObject result = null;

        @Override
        protected String doInBackground(String... strings)
        {
            try {
                Runtastic runtastic = new Runtastic(context);
                result = runtastic.leaderboard();
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("app", "LeaderBoardPagerAdapter.createContent" + e.getMessage());
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

            createContent(result);
        }
    }
}
