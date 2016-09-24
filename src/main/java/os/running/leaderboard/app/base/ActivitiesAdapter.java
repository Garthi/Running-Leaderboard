package os.running.leaderboard.app.base;

import android.content.res.Resources;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import os.running.leaderboard.app.R;

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
            if (data.getDistance() != null) {
                viewHolder.distanceView.setVisibility(View.VISIBLE);
                viewHolder.distanceView.setText(data.getDistance());
            } else {
                viewHolder.distanceView.setVisibility(View.GONE);
            }
            if (data.getDuration() != null) {
                viewHolder.durationView.setVisibility(View.VISIBLE);
                viewHolder.durationView.setText(data.getDuration());
            } else {
                viewHolder.durationView.setVisibility(View.GONE);
            }
     
            viewHolder.imageView.setBorderColor(Color.WHITE);
            Picasso.with(viewHolder.layoutView.getContext()).load(data.getUserAvatarUrl()).placeholder(R.drawable.default_profile).into(viewHolder.imageView);

            if (data.getNotes() == null || data.getNotes().equals("")) {
                viewHolder.notesView.setVisibility(View.GONE);
            } else {
                viewHolder.notesView.setVisibility(View.VISIBLE);
                viewHolder.notesView.setText(data.getNotes());
            }

            try {
                viewHolder.dateView.setVisibility(View.VISIBLE);
                // sample 2015-08-22T08:20:52Z
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'");
                Date newDate = format.parse(data.getDate());

                format = new SimpleDateFormat("dd MMM yyyy HH:mm", Locale.GERMANY);
                String date = format.format(newDate);

                viewHolder.dateView.setText(date);
            } catch (Exception e) {
                viewHolder.dateView.setVisibility(View.GONE);
            }
            
            if (data.getMapUrl() != null && !data.getMapUrl().equals("")) {
                Picasso.with(viewHolder.layoutView.getContext()).load(data.getMapUrl()).into(viewHolder.mapView);
            } else {
                viewHolder.mapView.setImageResource(0);
            }
            
            /*viewHolder.layoutView.setClickable(false);  // TODO add open detail view
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
            });*/

            // reset icons
            viewHolder.iconView.removeAllViews();
            
            // set the current icons
            List<Integer> icons = data.getIcons();
            if (!icons.isEmpty()) {
                for (Integer icon: icons) {
                    ImageView iconImageView = (ImageView)LayoutInflater.from(viewHolder.layoutView.getContext()).inflate(
                            R.layout.activities_item_icon, viewHolder.layoutView, false
                    );
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
    
    public ActivitiesAdapterData get(int position)
    {
        return dataSet.get(position);
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
