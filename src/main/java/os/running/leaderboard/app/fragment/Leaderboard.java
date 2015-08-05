package os.running.leaderboard.app.fragment;

import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONObject;
import os.running.leaderboard.app.R;
import os.running.leaderboard.app.base.Runtastic;

/**
 * @author Martin "Garth" Zander <garth@new-crusader.de>
 */
public class Leaderboard extends Fragment
{
    private LinearLayout mainView;

    public Leaderboard()
    {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        this.mainView = (LinearLayout)inflater.inflate(R.layout.leaderboard, container, false);
        
        new createContent().execute();
        
        return this.mainView;
    }

    @Override
    public void onResume()
    {
        super.onResume();

        new createContent().execute();
    }

    private void createContent(JSONObject result)
    {
        if (result == null) {
            return;
        }
        
        try {
            this.mainView.removeAllViews();
            TextView memberView;
            
            JSONArray data = result.getJSONArray("data");
            
            for(int ii = 0; ii < data.length(); ii++) {

                JSONObject entry = data.getJSONObject(ii);
                
                memberView = new TextView(getActivity());
                memberView.setText(entry.getString("id") + ": ");
                memberView.setTypeface(Typeface.DEFAULT_BOLD, Typeface.BOLD_ITALIC);

                this.mainView.addView(memberView);
                
                for (int i = 0; i < entry.getJSONArray("entries").length(); i++) {

                    JSONObject member = entry.getJSONArray("entries").getJSONObject(i);

                    memberView = new TextView(getActivity());

                    memberView.setText(member.getString("entry_number") + ". " + member.getJSONObject("member_data").getString("name") + " " + member.getString("score"));

                    this.mainView.addView(memberView);
                }
            }
            
        } catch (Exception e) {
            // 
        }
    }
    
    private class createContent extends AsyncTask<String, String, String>
    {
        private JSONObject result = null;
        
        @Override
        protected String doInBackground(String... strings)
        {
            try {
                Runtastic runtastic = new Runtastic(getActivity());
                result = runtastic.leaderboard();
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("app", "Leaderboard.createContent" + e.getMessage());
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
