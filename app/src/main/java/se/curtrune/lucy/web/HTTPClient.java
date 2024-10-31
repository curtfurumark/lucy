package se.curtrune.lucy.web;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import static se.curtrune.lucy.util.Logger.log;


public class HTTPClient {

    public static int TIMEOUT = 10000;
    public static final String SELECT_URL = "http://curtfurumark.se/projects/select.php";
    public static final String INSERT_URL = "http://curtfurumark.se/projects/insert.php";
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
        log("HTTPClient.get(String) ", getUrl );
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

    public static String send(HTTPRequest request) throws IOException {
        log("DBOneBasic.send(HTTPRequest) method: ", request.getHttpMethod().toString() );
        if (VERBOSE) log(request);
        StringBuilder result = new StringBuilder();
        URL url = new URL(request.getUrl());
        if (VERBOSE) log(url);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        if (VERBOSE) log("...connection open");
        connection.setRequestMethod(request.getHttpMethod().toString());
        connection.setDoInput(true);
        connection.setDoOutput(true);
        OutputStream outputStream = null;
        outputStream = connection.getOutputStream();
        if (VERBOSE) log("...got outputStream()");
        assert outputStream != null;
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
        if (VERBOSE) log("...before");
        if (VERBOSE) log("...will send request", request.toPostString());
        bufferedWriter.write(request.toPostString(false));
        bufferedWriter.flush();
        if (VERBOSE) log("...request sent");
        bufferedWriter.close();
        outputStream.close();
        if( VERBOSE) log("...will get inputStream");
        InputStream inputStream = connection.getInputStream();
        if( VERBOSE) log("...got inputStream");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.ISO_8859_1));
        if(VERBOSE) log("...got buffered reader");
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            if (VERBOSE) log("...read line: " + line);
            result.append(line);
        }
        bufferedReader.close();
        inputStream.close();
        connection.disconnect();
        return result.toString();
    }
}


