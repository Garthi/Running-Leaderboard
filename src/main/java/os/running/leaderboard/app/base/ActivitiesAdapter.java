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
import android.widget.ImageView;
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
public class ActivitiesAdapter extends RecyclerView.Adapter<ActivitiesAdapter.ViewHolder>
{
    private List<ActivitiesAdapterData> dataSet;
    private Fragment fragment;
    
    public ActivitiesAdapter(Fragment fragment)
    {
        this.fragment = fragment;
        dataSet = new ArrayList<ActivitiesAdapterData>();
    }
    
    @Override
    public ActivitiesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        // create a new view
        LinearLayout view = (LinearLayout)LayoutInflater.from(parent.getContext()).inflate(R.layout.activities_item, parent, false);
        
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position)
    {
        try {
            ActivitiesAdapterData data = dataSet.get(position);

            viewHolder.layoutView.setTag(data);

            viewHolder.nameView.setText(data.getUserName());
            viewHolder.typeView.setText(data.getSportType());
            viewHolder.distanceView.setText(data.getDistance());
            viewHolder.durationView.setText(data.getDuration());
     
            viewHolder.imageView.setBorderColor(Color.WHITE);
            Picasso.with(viewHolder.layoutView.getContext()).load(data.getUserAvatarUrl()).placeholder(R.drawable.default_profile).into(viewHolder.imageView);

            if (data.getNotes() == null || data.getNotes().equals("")) {
                viewHolder.notesView.setVisibility(View.GONE);
            } else {
                viewHolder.notesView.setText(data.getNotes());
            }
            
            viewHolder.dateView.setText(data.getDate());
            
            viewHolder.mapView.setVisibility(View.GONE);
            
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
                        Log.e("app", "ActivitiesAdapter.onBindViewHolder.onClick: " + e.getMessage());
                    }
                }
            });

            // reset icons
            viewHolder.iconView.removeAllViews();
            
            // set the current icons
            List<Integer> icons = data.getIcons();
            if (!icons.isEmpty()) {
                for (Integer icon: icons) {
                    ImageView iconImageView = new ImageView(viewHolder.layoutView.getContext());
                    iconImageView.setImageResource(icon);
                    viewHolder.iconView.addView(iconImageView);
                }
            }
            
        } catch(Resources.NotFoundException e) {
            Log.e("app", "ActivitiesAdapter.onBindViewHolder" + e.getMessage());
        }
    }

    @Override
    public int getItemCount()
    {
        return dataSet.size();
    }
    
    public void add(ActivitiesAdapterData data)
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
        public LinearLayout iconView;
        public TextView nameView;
        public TextView typeView;
        public TextView distanceView;
        public TextView durationView;
        public TextView notesView;
        public TextView dateView;
        public ImageView mapView;
        public de.hdodenhof.circleimageview.CircleImageView imageView;
        
        public ViewHolder(LinearLayout itemView)
        {
            super(itemView);

            layoutView = itemView;

            nameView = (TextView)itemView.findViewById(R.id.name);
            typeView = (TextView)itemView.findViewById(R.id.type);
            distanceView = (TextView)itemView.findViewById(R.id.distance);
            durationView = (TextView)itemView.findViewById(R.id.duration);
            notesView = (TextView)itemView.findViewById(R.id.notes);
            dateView = (TextView)itemView.findViewById(R.id.date);
            imageView = (de.hdodenhof.circleimageview.CircleImageView)itemView.findViewById(R.id.profile_image);
            mapView = (ImageView)itemView.findViewById(R.id.map);
            iconView = (LinearLayout)itemView.findViewById(R.id.icons);
        }
    }
}
