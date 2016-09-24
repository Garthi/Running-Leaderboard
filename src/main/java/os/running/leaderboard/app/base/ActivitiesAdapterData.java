package os.running.leaderboard.app.base;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import os.running.leaderboard.app.R;

/**
 * @author Martin "Garth" Zander <garth@new-crusader.de>
 */
public class ActivitiesAdapterData extends LiveSessionAdapterData
{
    private String sportType;
    private String distance;
    private String duration;
    private List<Integer> icons = new ArrayList<Integer>();
    private String notes;
    private String date;
    private String socialId;
    private String activityId;
    private String mapUrl;

    public List<Integer> getIcons()
    {
        return icons;
    }

    public void addIcon(String icon)
    {
        Integer iconImage = null;
        if (icon.equals("newsfeed-sunny")) {
            iconImage = R.drawable.ic_sunny;
        } else if (icon.equals("newsfeed-rainy")) {
            iconImage = R.drawable.ic_shower;
        } else if (icon.equals("newsfeed-cloudy")) {
            iconImage = R.drawable.ic_cloudy;
        }else if (icon.equals("newsfeed-night")) {
            iconImage = R.drawable.ic_night;
        }else if (icon.equals("newsfeed-snowy")) {    // TODO check this on snowy weather
            iconImage = R.drawable.ic_snow;
            
        } else if (icon.equals("newsfeed-offroad")) {
            iconImage = R.drawable.ic_offroad;
        } else if (icon.equals("newsfeed-trail")) {
            iconImage = R.drawable.ic_trail;
        } else if (icon.equals("newsfeed-road")) {
            iconImage = R.drawable.ic_road;
        } else if (icon.equals("newsfeed-mixed")) {
            iconImage = R.drawable.ic_mixed;
        } else if (icon.equals("newsfeed-sand")) {
            iconImage = R.drawable.ic_sand;
            
        } else if (icon.equals("newsfeed-awesome")) {
            iconImage = R.drawable.ic_awesome;
        } else if (icon.equals("newsfeed-good")) {
            iconImage = R.drawable.ic_good;
        } else if (icon.equals("newsfeed-so-so")) {
            iconImage = R.drawable.ic_so_so;
        } else if (icon.equals("newsfeed-sluggish")) {
            iconImage = R.drawable.ic_sluggish;
        } else if (icon.equals("newsfeed-injured")) {
            iconImage = R.drawable.ic_injured;
            
        } else if (icon.equals("newsfeed-tp_small_fat_loss")) {
            iconImage = R.drawable.ic_fat_loss;
        } else if (icon.equals("newsfeed-tp_small_beginner")) {
            iconImage = R.drawable.ic_beginner;
        } else if (icon.equals("newsfeed-tp_small_bikini-body-prep")) {
            iconImage = R.drawable.ic_bikini_body;
        } else if (icon.equals("newsfeed-tp_small_half-marathon")) {
            iconImage = R.drawable.ic_half_marathon;
        } else if (icon.equals("newsfeed-tp_small_marathon")) {
            iconImage = R.drawable.ic_marathon;
        } else if (icon.equals("newsfeed-tp_small_10k")) {
            iconImage = R.drawable.ic_10k;
        } else {
            Log.d("app", "new icon found: " + icon);
        }
        
        if (iconImage != null) {
            this.icons.add(iconImage);
        }
    }

    public String getSportType()
    {
        return sportType;
    }

    public void setSportType(String sportType)
    {
        this.sportType = sportType;
    }

    public String getDistance()
    {
        return distance;
    }

    public void setDistance(String distance)
    {
        this.distance = distance;
    }

    public String getDuration()
    {
        return duration;
    }

    public void setDuration(String duration)
    {
        this.duration = duration;
    }

    public String getNotes()
    {
        return notes;
    }

    public void setNotes(String notes)
    {
        this.notes = notes;
    }

    public String getDate()
    {
        return date;
    }

    public void setDate(String date)
    {
        this.date = date;
    }

    public String getSocialId()
    {
        return socialId;
    }

    public void setSocialId(String socialId)
    {
        this.socialId = socialId;
    }

    public String getActivityId()
    {
        return activityId;
    }

    public void setActivityId(String activityId)
    {
        this.activityId = activityId;
    }

    public String getMapUrl()
    {
        return mapUrl;
    }

    public void setMapUrl(String mapUrl)
    {
        this.mapUrl = mapUrl;
    }
}
