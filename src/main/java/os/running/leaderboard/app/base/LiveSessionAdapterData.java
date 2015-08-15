package os.running.leaderboard.app.base;

/**
 * @author Martin "Garth" Zander <garth@new-crusader.de>
 */
public class LiveSessionAdapterData
{
    private String userName;
    private String userAvatarUrl;
    private String userUrl;
    private String text;
    private String userSessionUrl;

    public String getUserSessionUrl()
    {
        return userSessionUrl;
    }

    public void setUserSessionUrl(String userSessionUrl)
    {
        this.userSessionUrl = userSessionUrl;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getUserAvatarUrl()
    {
        return userAvatarUrl;
    }

    public void setUserAvatarUrl(String userAvatarUrl)
    {
        this.userAvatarUrl = userAvatarUrl;
    }

    public String getUserUrl()
    {
        return userUrl;
    }

    public void setUserUrl(String userUrl)
    {
        this.userUrl = userUrl;
    }

    public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        this.text = text;
    }
}
