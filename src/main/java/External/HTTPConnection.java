package External;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;



class HTTPConnection implements Runnable {
    
    interface HTTPRequestCallback {
        void requestFailed(String message, String tag);
        void requestSuccess(String message, String tag);
    }
    
    public Thread thread;
    public HTTPRequestCallback httpRequestCallback;
    public String requestUrl = "";
    public String requestTag = "";
    
    public HTTPConnection(String url, HTTPRequestCallback callback, String tag) {
        requestUrl = url;
        requestTag = tag;
        httpRequestCallback = callback;
        thread = new Thread(this,"");
        thread.start();
    }
    
    
    public void run() {
        sendRequest(requestUrl);
    }
    
    
    void sendRequest(String url) {
        HttpURLConnection conn = null;
        
        try {
            System.out.println("SendingRequest:"+url);
            
            // Setup a URL connection
            URL address = new URL(url);
            conn = (HttpURLConnection) address.openConnection();
            conn.setUseCaches(false);
            conn.setDoOutput(true);
            conn.setConnectTimeout(3000);

            
            // Get the response
            int responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                System.out.println("Error - HTTP response code: " + responseCode);
                httpRequestCallback.requestFailed("Error - HTTP response code: " + responseCode, requestTag);
            } else {
                String response = readFromInputStream(conn.getInputStream());
                //System.out.println("Response:" + response);
                httpRequestCallback.requestSuccess(response,requestTag);
            }
        }  catch (IOException e) {
            Log.d("HTTPConn", e.toString());
            System.out.println("Failed to call the WebService");
        } finally {
            if (conn != null) conn.disconnect();
        }
    }
    
    
    
    private String readFromInputStream(InputStream stream) {
        
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            StringBuilder result = new StringBuilder();
            String line;
            while((line = reader.readLine()) != null) {
                result.append(line);
            }
            System.out.println(result.toString());
            return URLDecoder.decode(result.toString(), "UTF-8");
        } catch (IOException e) {
            System.out.println("Failed to read from input stream");
            return "";
        }
    }

}

