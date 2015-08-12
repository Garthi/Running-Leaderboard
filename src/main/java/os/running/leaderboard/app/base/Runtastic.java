package os.running.leaderboard.app.base;

import android.content.Context;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * @author Martin "Garth" Zander <garth@new-crusader.de>
 */
public class Runtastic
{
    private Context context;
    
    private String eMail = null;
    private String password = null;

    final private String loginUrl = "https://www.runtastic.com/en/d/users/sign_in.json";
    final private String leaderboardUrl = "https://hubs.runtastic.com/leaderboard/v2/applications/com_runtastic_core/users/%1$d/friends_leaderboards.json";
    final private String liveSessionUrl = "https://www.runtastic.com/en/users/%1$s/live_sessions";
    final private String friendsLiveSessionUrl = "https://www.runtastic.com/en/users/%1$s/friends_live_sessions";
    
    public Runtastic(Context context)
    {
        this.context = context;
    }

    public JSONObject leaderboard()
    {
        Database DB = Database.getInstance(context);

        String token = DB.getAccountData("token");

        if (token == null || token.isEmpty()) {
            return null;
        }

        int userId = 0;
        
        userId = Integer.parseInt(DB.getAccountData("userId"));
        if (userId <= 0) {
            return null;
        }

        Calendar calendar = new GregorianCalendar();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentWeek = calendar.get(Calendar.WEEK_OF_YEAR);
        int currentMonth = calendar.get(Calendar.MONTH) + 1;
        int latestWeek = calendar.get(Calendar.WEEK_OF_YEAR) - 1;
        int latestMonth = calendar.get(Calendar.MONTH);
        
        Connection api = new Connection(context);

        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter("around", "2"));
        parameters.add(new Parameter("bottom", "10"));
        parameters.add(new Parameter("include", "user"));
        parameters.add(new Parameter("top", "1"));
        parameters.add(new Parameter(
                "ids",
                "distance:time_frame:week:" + currentYear + "_" + currentWeek +
                ",distance:time_frame:week:" + currentYear + "_" + latestWeek +
                ",distance:time_frame:month:" + currentYear + "_" + currentMonth +
                ",distance:time_frame:month:" + currentYear + "_" + latestMonth)
        );

        api.setSessionCookieKey("_runtastic_session");
        api.setUrl(String.format(leaderboardUrl, userId));
        api.setMethod(api.METHOD_TYPE_GET);
        api.setParameters(parameters);

        if (!api.connect()) {
            return null;
        }
        
        try {
            JSONObject response = new JSONObject(api.getResponse());

            //if (Log.isLoggable("app", Log.DEBUG)) {
                Log.d("app", "Response JSON: " + response.toString());
            //}
            
            return response;
        } catch (Exception e) {
            Log.e("app", "Runtastic.leaderboard", e.fillInStackTrace());
        }
        
        return null;
    }
    
    public JSONObject liveSessions()
    {
        Database DB = Database.getInstance(context);
        
        String userName = DB.getAccountData("userName");
        if (userName == null || userName.equals("")) {
            return null;
        }
        
        Connection api = new Connection(context);

        api.setSessionCookieKey("_runtastic_session");
        api.setUrl(String.format(liveSessionUrl, userName));
        api.setMethod(api.METHOD_TYPE_GET);

        if (!api.connect()) {
            return null;
        }

        try {
            Document doc = Jsoup.parse(api.getResponse());
            
            Elements sessions = doc.getElementsByClass("run_session");
            
            if (sessions.isEmpty()) {
                return null;
            }
            
            JSONObject response = new JSONObject();
            JSONArray responseSession = new JSONArray();
            
            for (Element session: sessions) {
                
                JSONObject entry = new JSONObject();

                Element user = session.getElementsByClass("avatar-group").get(0).getElementsByTag("a").get(0);
                
                entry.put("user", user.attr("title"));
                entry.put("url", user.attr("href"));
                entry.put("avatarUrl", session.getElementsByClass("avatar").get(0).attr("src"));
                entry.put("text", session.getElementsByClass("content").text());

                for (Element link: session.getElementsByTag("a")) {
                    if (link.attr("href").contains("/sport-sessions/")) {
                        entry.put("sessionUrl", link.attr("href"));
                    }
                }

                responseSession.put(entry);
            }

            response.put("sessions", responseSession);
            
            return response;

        } catch (Exception e) {
            Log.e("app", "Runtastic.liveSessions", e.fillInStackTrace());
        }
        
        return new JSONObject();
    }
    
    public JSONObject friendsLiveSessions()
    {
        Database DB = Database.getInstance(context);

        String userName = DB.getAccountData("userName");
        if (userName == null || userName.equals("")) {
            return null;
        }

        Connection api = new Connection(context);

        api.setSessionCookieKey("_runtastic_session");
        api.setUrl(String.format(friendsLiveSessionUrl, userName));
        api.setMethod(api.METHOD_TYPE_GET);

        if (!api.connect()) {
            return null;
        }

        try {
            Document doc = Jsoup.parse(api.getResponse());

            Elements sessions = doc.getElementsByClass("run_session");

            if (sessions.isEmpty()) {
                return null;
            }

            JSONObject response = new JSONObject();
            JSONArray responseSession = new JSONArray();

            for (Element session: sessions) {

                JSONObject entry = new JSONObject();

                Element user = session.getElementsByClass("avatar-group").get(0).getElementsByTag("a").get(0);

                entry.put("user", user.attr("title"));
                entry.put("url", user.attr("href"));
                entry.put("avatarUrl", session.getElementsByClass("avatar").get(0).attr("src"));
                entry.put("text", session.getElementsByClass("content").text());

                for (Element link: session.getElementsByTag("a")) {
                    if (link.attr("href").contains("/sport-sessions/")) {
                        entry.put("sessionUrl", link.attr("href"));
                    }
                }

                responseSession.put(entry);
            }

            response.put("sessions", responseSession);

            return response;

        } catch (Exception e) {
            Log.e("app", "Runtastic.friendsLiveSessions", e.fillInStackTrace());
        }
        
        return new JSONObject();
    }
    
    public Boolean login()
    {
        return login(false);
    }
    
    public Boolean login(Boolean autoLogin)
    {
        String token = "";
        
        if (autoLogin) {
            Database DB = Database.getInstance(context);
            
            token = DB.getAccountData("token");
            
            if (token == null || token.isEmpty()) {
                return false;
            }
            
        } else {
            if (eMail == null || eMail.isEmpty()) {
                return false;
            }
            if (password == null || password.isEmpty()) {
                return false;
            }
        }

        Connection api = new Connection(context);

        List<Parameter> parameters = new ArrayList<Parameter>();
        if (autoLogin) {
            parameters.add(new Parameter("user[email]", ""));
            parameters.add(new Parameter("user[password]", ""));
            parameters.add(new Parameter("authenticity_token", token));
        } else {
            parameters.add(new Parameter("user[email]", eMail));
            parameters.add(new Parameter("user[password]", password));
            parameters.add(new Parameter("authenticity_token", ""));
        }

        api.setSessionCookieKey("_runtastic_session");
        api.setUrl(loginUrl);
        api.setMethod(api.METHOD_TYPE_POST);
        api.setParameters(parameters);

        if (!api.connect()) {
            logout(false);
            return false;
        }

        try {
            JSONObject response = new JSONObject(api.getResponse());

            if (Log.isLoggable("app", Log.DEBUG)) {
                Log.d("app", "Response JSON: " + response.toString());
            }

            if (!response.getBoolean("success")) {
                logout(false);
                return false;
            }

            token = getToken(response);
            String userName = getUsername(response);
            String userId = response.getJSONObject("current_user").getString("id");
            String avatar = getAvatar(response);
            String firstName = response.getJSONObject("current_user").getString("first_name");
            String lastName = response.getJSONObject("current_user").getString("last_name");

            // save data to DB
            Database DB = Database.getInstance(context);
            
            DB.addAccounData("userName", userName);
            DB.addAccounData("userId", userId);
            DB.addAccounData("token", token);
            DB.addAccounData("avatarUrl", avatar);
            DB.addAccounData("firstName", firstName);
            DB.addAccounData("lastName", lastName);
            
            return true;

        } catch (JSONException e) {
            Log.e("app", "Runtastic.login", e.fillInStackTrace());
        }

        logout(false);
        return false;
    }
    
    public Boolean hasLogin()
    {
        Database DB = Database.getInstance(context);

        String token = DB.getAccountData("token");
        
        if (token != null && !token.isEmpty()) {
            return true;
        }
        
        return false;
    }

    public void logout()
    {
        logout(true);
    }
    
    public void logout(Boolean withApi)
    {
        if (withApi) {
            // TODO logout api call
        }

        Database DB = Database.getInstance(context);
        
        // DB.deleteAccountData("token");
    }

    private String getToken(JSONObject response) throws JSONException
    {
        String data = response.getString("update");

        Document doc = Jsoup.parse(data);
        Elements elements = doc.body().getElementsByTag("input");
        
        for (Element element: elements) {
            if (element.attr("name").equals("authenticity_token")) {
                return element.attr("value");
            }
        }
        
        return "";
    }

    private String getUsername(JSONObject response) throws JSONException
    {
        String data = response.getString("update");
        
        Document doc = Jsoup.parse(data);
        Elements elements = doc.body().getElementsByTag("a");

        for (Element element: elements) {
            if (element.attr("href").startsWith("/en/users/") && element.attr("href").endsWith("/dashboard")) {
                return element.attr("href").replace("/en/users/", "").replace("/dashboard", "");
            }
        }
        
        return "";
    }
    
    private String getAvatar(JSONObject response) throws JSONException
    {
        String data = response.getString("update");

        Document doc = Jsoup.parse(data);
        Elements elements = doc.body().getElementsByTag("img");

        for (Element element: elements) {
            if (element.attr("class").equals("avatar")) {
                return element.attr("src");
            }
        }
        
        return "";
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
