package os.running.leaderboard.app.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import org.json.JSONObject;
import os.running.leaderboard.app.R;
import os.running.leaderboard.app.base.LeaderBoardAdapter;
import os.running.leaderboard.app.base.LeaderBoardAdapterData;

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

            RecyclerView listView = (RecyclerView)mainView.findViewById(R.id.listView);
            listView.setHasFixedSize(true);

            RecyclerView.LayoutManager manager = new LinearLayoutManager(this.getActivity());
            listView.setLayoutManager(manager);

            LeaderBoardAdapter adapter = new LeaderBoardAdapter();
            listView.setAdapter(adapter);
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
                
                adapter.add(adapterData);
            }
            
        }  catch (Exception e) {
            Log.e("app", "LeaderBoardPage.createContent (" + mPage + "): " + e.getMessage());
        }
    }
}
