package os.running.leaderboard.app.base;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import os.running.leaderboard.app.R;
import os.running.leaderboard.app.fragment.Activities;

/**
 * @author Martin "Garth" Zander <garth@new-crusader.de>
 */
public class LeaderBoardAdapter extends RecyclerView.Adapter<LeaderBoardAdapter.ViewHolder>
{
    private List<LeaderBoardAdapterData> dataSet;
    private Fragment fragment;
    private String tabType;
    
    public LeaderBoardAdapter(Fragment fragment)
    {
        this.fragment = fragment;
        dataSet = new ArrayList<LeaderBoardAdapterData>();
    }
    
    @Override
    public LeaderBoardAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        // create a new view
        LinearLayout view = (LinearLayout)LayoutInflater.from(parent.getContext()).inflate(R.layout.leader_board_item, parent, false);
        
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position)
    {
        try {
            final LeaderBoardAdapterData data = dataSet.get(position);

            viewHolder.layoutView.setTag(data);

            viewHolder.placeView.setText(String.valueOf(data.getNumber()));
            viewHolder.nameView.setText(data.getUserName());
            
            double score = data.getScore();
            score = score / 10 / 100;

            NumberFormat format = NumberFormat.getInstance();
            format.setRoundingMode(RoundingMode.HALF_UP);
            format.setMinimumIntegerDigits(1);
            format.setMinimumFractionDigits(2);
            format.setMaximumFractionDigits(2);

            viewHolder.scoreView.setText(format.format(score) + " km");
            
            if (data.getNumber() == 1) {
                viewHolder.imageView.setBorderColor(
                        viewHolder.placeView.getContext().getResources().getColor(R.color.app_gold)
                );
                GradientDrawable bgShape = (GradientDrawable)viewHolder.placeView.getBackground();
                bgShape.setColor(viewHolder.placeView.getContext().getResources().getColor(R.color.app_gold));
            } else if (data.getNumber() == 2){
                viewHolder.imageView.setBorderColor(
                        viewHolder.placeView.getContext().getResources().getColor(R.color.app_silver)
                );
                GradientDrawable bgShape = (GradientDrawable)viewHolder.placeView.getBackground();
                bgShape.setColor(viewHolder.placeView.getContext().getResources().getColor(R.color.app_silver));
            } else if (data.getNumber() == 3) {
                viewHolder.imageView.setBorderColor(
                        viewHolder.placeView.getContext().getResources().getColor(R.color.app_bronze)
                );
                GradientDrawable bgShape = (GradientDrawable)viewHolder.placeView.getBackground();
                bgShape.setColor(viewHolder.placeView.getContext().getResources().getColor(R.color.app_bronze));
            } else {
                viewHolder.imageView.setBorderColor(Color.WHITE);
                GradientDrawable bgShape = (GradientDrawable)viewHolder.placeView.getBackground();
                bgShape.setColor(viewHolder.placeView.getContext().getResources().getColor(R.color.app_blue));
            }
            
            viewHolder.layoutView.setClickable(true);
            viewHolder.layoutView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    try {
                        Activities activities = new Activities();
                        
                        // add extra information
                        Bundle extras = new Bundle();
                        extras.putInt("userId", data.getUserId());
                        // TODO add extras with period
                        activities.setArguments(extras);
                        
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
            Log.e("app", "onBindViewHolder" + e.getMessage());
        }
    }

    @Override
    public int getItemCount()
    {
        return dataSet.size();
    }
    
    public void setTabType(String type)
    {
        this.tabType = type;
    }
    
    public void add(LeaderBoardAdapterData data)
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
        public TextView placeView;
        public TextView nameView;
        public TextView scoreView;
        public de.hdodenhof.circleimageview.CircleImageView imageView;
        
        public ViewHolder(LinearLayout itemView)
        {
            super(itemView);

            layoutView = itemView;

            placeView = (TextView)itemView.findViewById(R.id.placeView);
            nameView = (TextView)itemView.findViewById(R.id.name);
            scoreView = (TextView)itemView.findViewById(R.id.score);
            imageView = (de.hdodenhof.circleimageview.CircleImageView)itemView.findViewById(R.id.profile_image);
        }
    }
}
