package os.running.leaderboard.app.base;

import android.util.Log;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.*;
import java.util.List;

/**
 * @author Martin "Garth" Zander <garth@new-crusader.de>
 */
public class Connection
{
    private String url;
    private String method;
    private List<Parameter> parameters;
    private String response;
    
    private Boolean httpsConnection = true;
    private int readTimeout = 10000;
    private int connectionTimeout = 15000;

    final public String METHOD_TYPE_GET = "GET";
    final public String METHOD_TYPE_POST = "POST";

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
            URL url = new URL(this.url);

            HttpURLConnection connection;
            
            if (httpsConnection) {
                connection = (HttpsURLConnection)url.openConnection();
            } else {
                connection = (HttpURLConnection)url.openConnection();
            }
            
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

            if (Log.isLoggable("app", Log.DEBUG)) {
                Log.d("app", "Response Code: " + connection.getResponseCode());
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
            Log.e("app", "connection url", e.fillInStackTrace());
        } catch (ProtocolException e) {
            Log.e("app", "connection protocol", e.fillInStackTrace());
        } catch (UnsupportedEncodingException e) {
            Log.e("app", "connection parameters", e.fillInStackTrace());
        } catch (IOException e) {
            Log.e("app", "connection", e.fillInStackTrace());
        }
        
        return false;
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
