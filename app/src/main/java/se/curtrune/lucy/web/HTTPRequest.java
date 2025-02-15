package se.curtrune.lucy.web;


import static se.curtrune.lucy.util.Logger.log;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import se.curtrune.lucy.util.Converter;


public class HTTPRequest {
    private Map<String, String> postPairs = new HashMap<>();
    private Map<String, String> headers = new HashMap<>();
    private String url;
    public static  boolean VERBOSE = false;

    private HttpMethod httpMethod = HttpMethod.POST;


    public HTTPRequest(String url) {
        this.url = url;
    }


    public void add(String key, LocalDate target_date) {
        if(VERBOSE) log(key, target_date);
        postPairs.put(key, target_date != null? target_date.toString(): "");
    }
    public void add(String key, LocalDateTime date_time) {
        postPairs.put(key, Converter.format(date_time));
    }

    public void addHeader(String key, String value){
        headers.put(key, value);
    }

    /**
     * if value is null, it is translated into an empty string ""
     * @param key
     * @param value
     */
    public void add(String key, String value) {
        if (value == null){
            value = "";
        }
        postPairs.put(key, value);
    }
    public void add(String name, float value){
        postPairs.put(name, String.valueOf(value));
    }
    public void add(String name, int value){
        postPairs.put(name, String.valueOf(value));
    }
    public void add(String key, long value){postPairs.put(key, String.valueOf(value));}


    public HttpMethod getHttpMethod(){
        return httpMethod;
    }
    public Map<String,String> getHeaders(){
        return headers;
    }
    /**
     * dont know what this should be called, its the params, used in GET and POST
     * @return the params currently set for this HTTPRequest
     */
    public  Map<String, String> getParams(){
        return postPairs;
    }
    public String getUrl() {
        return url;
    }

    public void setMethod(HttpMethod method){
        this.httpMethod = method;
    }

    public String toPostString() throws UnsupportedEncodingException {
        if(VERBOSE) log(  "HTTPRequest.toPostString()");
        return urlEncode(postPairs);
    }
    public String toPostString(boolean url_encode) throws UnsupportedEncodingException {
        if( VERBOSE ) log(  "HTTPPost.toPostString()");
        if( url_encode) {
            return urlEncode(postPairs);
        }
        return notUrlEncode(postPairs);
    }

    public static String urlEncode(Map<String, String> map) throws UnsupportedEncodingException {
        if(VERBOSE)log("HTTPRequest.urlEncode()");
        int i = 0;
        String postValues = "";
        for (Map.Entry<String, String> pair : map.entrySet()) {
            String key = URLEncoder.encode(pair.getKey(), "UTF-8");
            String value = URLEncoder.encode(pair.getValue(), "UTF-8");
            postValues += key + "=" + value;
            if (map.size() > ++i) {
                postValues += "&";
            }
        }
        if(VERBOSE)log("HTTPPost.urlEncode()...postString: " + postValues);
        return postValues;
    }
    public static String notUrlEncode(Map<String, String> map) throws UnsupportedEncodingException {
        int i = 0;
        String postValues = "";
        for (Map.Entry<String, String> pair : map.entrySet()) {
            //System.out.println(pair.getKey() + " " + pair.getValue());
            String key = pair.getKey();
            String value = pair.getValue();
            postValues += key + "=" + value;
            if (map.size() > ++i) {
                postValues += "&";
            }
        }
        if(VERBOSE )log("HTTPPost.urlEncode()...postString: " + postValues);
        return postValues;
    }



    public void setUrl(String url) {
        this.url = url;
    }
    @Override
    public String toString() {
        return "HTTPPost{" +
                "postPairs=" + postPairs +
                ", url='" + url + '\'' +
                '}';
    }

}
