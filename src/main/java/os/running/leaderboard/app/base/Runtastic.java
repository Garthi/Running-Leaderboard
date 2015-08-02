package os.running.leaderboard.app.base;

import android.content.Context;
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
    private Context context;
    
    private String eMail = null;
    private String password = null;

    final private String loginUrl = "https://www.runtastic.com/en/d/users/sign_in.json";
    
    public Runtastic(Context context)
    {
        this.context = context;
    }

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

            String token = getToken(response);
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
