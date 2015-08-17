package os.running.leaderboard.app.base;

import android.content.res.Resources;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import os.running.leaderboard.app.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Martin "Garth" Zander <garth@new-crusader.de>
 */
public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.ViewHolder>
{
    private List<FriendsAdapterData> dataSet;
    
    public FriendsAdapter()
    {
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
    public void onBindViewHolder(ViewHolder viewHolder, int position)
    {
        try {
            FriendsAdapterData data = dataSet.get(position);

            viewHolder.layoutView.setTag(data);

            viewHolder.nameView.setText(data.getUserName());
     
            viewHolder.imageView.setBorderColor(Color.WHITE);
            Picasso.with(viewHolder.layoutView.getContext()).load(data.getUserAvatarUrl()).placeholder(R.drawable.default_profile).into(viewHolder.imageView);

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
