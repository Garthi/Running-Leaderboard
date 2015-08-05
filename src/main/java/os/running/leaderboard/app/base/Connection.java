package os.running.leaderboard.app.base;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.*;
import java.util.List;
import java.util.Map;

/**
 * @author Martin "Garth" Zander <garth@new-crusader.de>
 */
public class Connection
{
    private Context context;
    
    private String url;
    private String method;
    private List<Parameter> parameters;
    private String response;
    
    private String sessionCookieKey = "session";
    
    private Boolean httpsConnection = true;
    private int readTimeout = 10000;
    private int connectionTimeout = 15000;

    final public String METHOD_TYPE_GET = "GET";
    final public String METHOD_TYPE_POST = "POST";

    public Connection(Context context)
    {
        this.context = context;
    }
    
    public void setUrl(String url)
    {
        this.url = url;
    }

    public void setMethod(String method) throws IllegalArgumentException
    {
        if (method.equals(METHOD_TYPE_GET)) {
            this.method = METHOD_TYPE_GET;
        } else if (method.equals(METHOD_TYPE_POST)) {
            this.method = METHOD_TYPE_POST;
        } else {
            throw new IllegalArgumentException("not correct method");
        }
    }

    public void setParameters(List<Parameter> parameters)
    {
        this.parameters = parameters;
    }

    public String getResponse()
    {
        return response;
    }

    public Boolean connect()
    {
        try {

            URL url;
            if (this.method.equals(METHOD_TYPE_GET)) {
                String connectionParameters = convertParameters();
                url = new URL(this.url + "?" + connectionParameters);
            } else {
                url = new URL(this.url);
            }

            HttpURLConnection connection;
            
            if (httpsConnection) {
                connection = (HttpsURLConnection)url.openConnection();
            } else {
                connection = (HttpURLConnection)url.openConnection();
            }
            
            
            // set cookie session
            SharedPreferences SharedData = PreferenceManager.getDefaultSharedPreferences(context);
            if (SharedData.contains(sessionCookieKey)) {
                connection.addRequestProperty("Cookie", SharedData.getString(sessionCookieKey, ""));
                Log.d("app", "Cookie: " + SharedData.getString(sessionCookieKey, ""));
            }

            connection.addRequestProperty("Pragma", "no-cache");
            connection.addRequestProperty("Cache-Control", "no-cache");
            connection.addRequestProperty("Accept", "application/json, text/javascript, */*; q=0.01");
            connection.addRequestProperty("X-App-Version", "3.0");
            connection.addRequestProperty("X-App-Key", "com.runtastic.web");
            
            Map<String, List<String>> headers = connection.getRequestProperties();
            Log.d("app", "headers" + headers.toString());

            
            connection.setReadTimeout(this.readTimeout);
            connection.setConnectTimeout(this.connectionTimeout);
            connection.setRequestMethod(this.method);
            
            if (this.method.equals(METHOD_TYPE_POST)) {

                String connectionParameters = convertParameters();
                
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.setFixedLengthStreamingMode(connectionParameters.length());

                OutputStream os = connection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(connectionParameters);
                writer.flush();
                writer.close();
                os.close();
            }

            //if (Log.isLoggable("app", Log.DEBUG)) {
                Log.d("app", "Response Code: " + connection.getResponseCode());
            //}

            // get cookies
            Map<String, List<String>> headerFields = connection.getHeaderFields();
            List<String> cookiesHeader = headerFields.get("Set-Cookie");
            for (String cookie: cookiesHeader) {
                if (cookie.startsWith(sessionCookieKey)) {
                    //if (Log.isLoggable("app", Log.DEBUG)) {
                        Log.d("app", "Session Cookie: " + cookie);
                    //}
                    SharedPreferences.Editor editor = SharedData.edit();
                    editor.putString(sessionCookieKey, cookie);
                    editor.apply();
                } else {
                    Log.d("app", "Cookie: " + cookie);
                }
            }

            InputStream in = new BufferedInputStream(connection.getInputStream());
            byte[] contents = new byte[1024];

            int bytesRead;
            response = "";
            while ((bytesRead = in.read(contents)) != -1) {
                response += new String(contents, 0, bytesRead);
            }

            if (Log.isLoggable("app", Log.DEBUG)) {
                Log.d("app", "Response: " + response);
            }

            connection.disconnect();

            return true;
        } catch (MalformedURLException e) {
            Log.e("app", "connection url" + e.getLocalizedMessage());
        } catch (ProtocolException e) {
            Log.e("app", "connection protocol" + e.getLocalizedMessage());
        } catch (UnsupportedEncodingException e) {
            Log.e("app", "connection parameters" + e.getLocalizedMessage());
        } catch (IOException e) {
            Log.e("app", "connection" + e.getLocalizedMessage());
        }
        
        return false;
    }

    public void setSessionCookieKey(String sessionCookieKey) {
        this.sessionCookieKey = sessionCookieKey;
    }

    private String convertParameters() throws UnsupportedEncodingException
    {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (Parameter parameter : parameters)
        {
            if (first) {
                first = false;
            } else {
                result.append("&");
            }

            result.append(URLEncoder.encode(parameter.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(parameter.getValue(), "UTF-8"));
        }
        
        return result.toString();
    }
}
