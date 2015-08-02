package os.running.leaderboard.app.base;

import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Martin "Garth" Zander <garth@new-crusader.de>
 */
public class Runtastic
{
    private String eMail = null;
    private String password = null;

    final private String loginUrl = "https://www.runtastic.com/en/d/users/sign_in.json";

    public Boolean login()
    {
        if (eMail == null || eMail.isEmpty()) {
            return false;
        }

        if (password == null || password.isEmpty()) {
            return false;
        }

        Connection api = new Connection();

        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter("user[email]", eMail));
        parameters.add(new Parameter("user[password]", password));
        parameters.add(new Parameter("authenticity_token", ""));

        api.setUrl(loginUrl);
        api.setMethod(api.METHOD_TYPE_POST);
        api.setParameters(parameters);

        if (!api.connect()) {
            return false;
        }

        try {
            JSONObject response = new JSONObject(api.getResponse());

            if (Log.isLoggable("app", Log.DEBUG)) {
                Log.d("app", "Response JSON: " + response.toString());
            }

            if (!response.getBoolean("success")) {
                return false;
            }

            String token = getToken(response);
            String userName = getUsername(response);
            String userId = getUserId(response);
            String avatar = getAvatar(response);

            Log.d("app", "token: " + token);
            Log.d("app", "username: " + userName);
            Log.d("app", "userId: " + userId);
            Log.d("app", "avatar: " + avatar);
            
            // TODO: save data to DB
            
            return true;

        } catch (JSONException e) {
            Log.e("app", "Runtastic.login", e.fillInStackTrace());
        }

        return false;
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

    /**
     * returns the runtastic user id
     * 
     * @param response api response
     * @return String
     * @throws JSONException
     */
    private String getUserId(JSONObject response) throws JSONException
    {
        return response.getJSONObject("current_user").getString("id");
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
