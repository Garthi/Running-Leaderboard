package os.running.leaderboard.app.base;

import android.content.res.Resources;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import os.running.leaderboard.app.R;
import os.running.leaderboard.app.fragment.Activities;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Martin "Garth" Zander <garth@new-crusader.de>
 */
public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.ViewHolder>
{
    private List<FriendsAdapterData> dataSet;
    private Fragment fragment;
    
    public FriendsAdapter(Fragment fragment)
    {
        this.fragment = fragment;
        dataSet = new ArrayList<FriendsAdapterData>();
    }
    
    @Override
    public FriendsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        // create a new view
        LinearLayout view = (LinearLayout)LayoutInflater.from(parent.getContext()).inflate(R.layout.friends_item, parent, false);
        
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position)
    {
        try {
            FriendsAdapterData data = dataSet.get(position);

            viewHolder.layoutView.setTag(data);

            viewHolder.nameView.setText(data.getUserName());
     
            viewHolder.imageView.setBorderColor(Color.WHITE);
            Picasso.with(viewHolder.layoutView.getContext()).load(data.getUserAvatarUrl()).placeholder(R.drawable.default_profile).into(viewHolder.imageView);

            viewHolder.layoutView.setClickable(true);
            viewHolder.layoutView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    try {
                        Activities activities = new Activities();
                        FragmentTransaction fragmentTransaction = fragment.getActivity().getSupportFragmentManager().beginTransaction();
                        // TODO: add animation
                        // hide old fragment
                        fragmentTransaction.hide(fragment);
                        // add new fragment
                        fragmentTransaction.add(R.id.content, activities);
                        // add back stack
                        fragmentTransaction.addToBackStack(null);
                        // commit all changes
                        fragmentTransaction.commit();
                    } catch (Exception e) {
                        Log.e("app", "FriendsAdapter.onBindViewHolder.onClick: " + e.getMessage());
                    }
                }
            });
            
        } catch(Resources.NotFoundException e) {
            Log.e("app", "FriendsAdapter.onBindViewHolder" + e.getMessage());
        }
    }

    @Override
    public int getItemCount()
    {
        return dataSet.size();
    }
    
    public void add(FriendsAdapterData data)
    {
        dataSet.add(data);
        this.notifyItemInserted(dataSet.size());
    }
    
    public void reset()
    {
        dataSet.clear();
        this.notifyDataSetChanged();
    }
    
    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public LinearLayout layoutView;
        public TextView nameView;
        public de.hdodenhof.circleimageview.CircleImageView imageView;
        
        public ViewHolder(LinearLayout itemView)
        {
            super(itemView);

            layoutView = itemView;

            nameView = (TextView)itemView.findViewById(R.id.name);
            imageView = (de.hdodenhof.circleimageview.CircleImageView)itemView.findViewById(R.id.profile_image);
        }
    }
}
