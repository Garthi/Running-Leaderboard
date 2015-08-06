package os.running.leaderboard.app.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.json.JSONObject;
import os.running.leaderboard.app.R;

/**
 * @author Martin "Garth" Zander <garth@new-crusader.de>
 */
public class LeaderBoardPage extends Fragment
{
    public static final String ARG_PAGE = "ARG_PAGE";
    
    private int mPage;
    private JSONObject contentData = null;
    
    private LinearLayout mainView = null;
    
    public static LeaderBoardPage newInstance(int page)
    {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        
        LeaderBoardPage fragment = new LeaderBoardPage();
        fragment.setArguments(args);
        
        return fragment;
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        if (this.mainView == null) {
            this.mainView = (LinearLayout) inflater.inflate(R.layout.leader_board_page, container, false);
        }
        
        if (this.contentData != null) {
            createContent(this.contentData);
        }
        
        return this.mainView;
    }
    
    public void setContentData(JSONObject data)
    {
        this.contentData = data;
        
        if (this.mainView != null) {
            createContent(data);
        }
    }
    
    private void createContent(JSONObject data)
    {
        try {
            this.mainView.removeAllViews();

            TextView memberView = new TextView(getActivity());
            memberView.setText(data.getString("id") + ": ");
            memberView.setTypeface(Typeface.DEFAULT_BOLD, Typeface.BOLD_ITALIC);

            this.mainView.addView(memberView);

            for (int i = 0; i < data.getJSONArray("entries").length(); i++) {

                JSONObject member = data.getJSONArray("entries").getJSONObject(i);

                memberView = new TextView(getActivity());

                memberView.setText(member.getString("entry_number") + ". " + member.getJSONObject("member_data").getString("name") + " " + member.getString("score"));

                this.mainView.addView(memberView);
            }
        }  catch (Exception e) {
            Log.e("app", "LeaderBoardPage.createContent (" + mPage + "): " + e.getMessage());
        }
    }
}
