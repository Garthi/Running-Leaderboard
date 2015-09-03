package os.running.leaderboard.app.base;

import android.util.Log;
import os.running.leaderboard.app.R;

import java.util.ArrayList;
import java.util.List;

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
        } else if (icon.equals("newsfeed-trail")) {
        } else if (icon.equals("newsfeed-road")) {
            // TODO add road land icon
        } else if (icon.equals("newsfeed-mixed")) {
            // TODO add mixed land icon
            // TODO add sand land icon
            
        } else if (icon.equals("newsfeed-awesome")) {
            // TODO add blue icon face :-D
        } else if (icon.equals("newsfeed-good")) {
            // TODO add green icon face :-)
        } else if (icon.equals("newsfeed-so-so")) {
            // TODO add green icon face :-|
        } else if (icon.equals("newsfeed-sluggish")) {
            // TODO add yellow icon face :-(
        } else if (icon.equals("newsfeed-injured")) {
            // TODO add red icon face :-(
            
        } else if (icon.equals("newsfeed-tp_small_fat_loss")) {
            // TODO add fat loss training icon
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
