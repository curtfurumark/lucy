package se.curtrune.lucy.web;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import static se.curtrune.lucy.util.Logger.log;


public class HTTPBasic {

    public static int TIMEOUT = 10000;
    private static final String VERSION = "0.7";
    public  static  boolean VERBOSE = false;

    public static String getVersion(){
        return VERSION;
    }

    /**
     * @param getUrl, connect to this url
     * @return response as a string
     */
    public static String get(String getUrl) {
        log("HTTPBasic.get(String) ", getUrl );
        StringBuilder result = new StringBuilder();
        try {
            URL url = new URL(getUrl);
            if (VERBOSE)log("host: " , url.getHost());
            if( VERBOSE) log("port: " + url.getPort());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.setConnectTimeout(TIMEOUT);
            connection.setDoOutput(false);

            //if (VERBOSE )log(connection);
            InputStream inputStream = connection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.ISO_8859_1));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                result.append(line);
            }
            bufferedReader.close();
            inputStream.close();
            connection.disconnect();
        } catch (MalformedURLException | UnsupportedEncodingException | ProtocolException e) {
            e.printStackTrace();
            result = new StringBuilder(e.toString());
        } catch (IOException e) {
            e.printStackTrace();
            result = new StringBuilder(e.toString());
            if( VERBOSE) log("res (mysqld.post(): " + result);
        }
        return result.toString();
    }
}


