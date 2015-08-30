package os.running.leaderboard.app.base;

/**
 * @author Martin "Garth" Zander <garth@new-crusader.de>
 */
public class ActivitiesAdapterData extends LiveSessionAdapterData
{
    private String sportType;
    private String distance;
    private String duration;
    private String icon;

    public String getIcon()
    {
        return icon;
    }

    public void setIcon(String icon)
    {
        this.icon = icon;
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
}
