package External;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import External.DownloadEvent.DownloadEventType;

class HTTPDownload implements Runnable {

    
    interface HTTPDownloadCallback {
        void downloadFailed(String message, String tag);
        void downloadSuccess(String message, String tag);
    }
    
    public Thread thread;
    public HTTPDownloadCallback httpDownloadCallback;
    public String requestUrl = "";
    public String requestTag = "";
    public String downloadPath = "";
    public int downloadSize = 0;
    
    public HTTPDownload(String url, HTTPDownloadCallback callback, String tag, String path, int size) {
        requestUrl = url;
        requestTag = tag;
        downloadPath = path;
        downloadSize = size;
        httpDownloadCallback = callback;
        thread = new Thread(this,"");
        thread.start();
    }
    
    public void run() {
        try {
            downloadFromUrl(new URL(requestUrl), downloadPath);
        } catch (IOException e) {
            System.out.println("Download file IOException: "+e);
        }
    }
    
    void downloadFromUrl(URL url, String localFilename) throws IOException {
        InputStream is = null;
        FileOutputStream fos = null;
        
        eventDownloadStart();
        
        try {
            URLConnection urlConn = url.openConnection();
            
            is = urlConn.getInputStream();
            fos = new FileOutputStream(localFilename);
            
            byte[] buffer = new byte[4096];
            int len;
            int totalLen = 0;
            
            while ((len = is.read(buffer)) > 0) {
                fos.write(buffer, 0, len);
                totalLen += len;
                eventDownloadProgress(totalLen);
            }
            
            eventDownloadFinish();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } finally {
                if (fos != null) {
                    fos.close();
                }
            }
        }
    }
    

    void eventDownloadStart() {
        DownloadEvent de = new DownloadEvent(DownloadEventType.downloadEventTypeStarted, requestTag, 0);
        EventBusSingleton.post(de);
    }
    
    void eventDownloadFinish() {
        httpDownloadCallback.downloadSuccess("",requestTag);
        DownloadEvent de = new DownloadEvent(DownloadEventType.downloadEventTypeFinished, requestTag, 0);
        EventBusSingleton.post(de);
    }
    
    void eventDownloadProgress(int currentSize) {
//        //
//        try { Thread.sleep(1000); } catch(InterruptedException e) {}
//        //
        float downloadProgress = currentSize;
        downloadProgress /= downloadSize;
        DownloadEvent de = new DownloadEvent(DownloadEventType.downloadEventTypeProgress, requestTag, downloadProgress);
        EventBusSingleton.post(de);
    }
    
    void eventDownloadFail() {
        httpDownloadCallback.downloadFailed("Oh no",requestTag);
        DownloadEvent de = new DownloadEvent(DownloadEventType.downloadEventTypeFailed, requestTag, 0);
        EventBusSingleton.post(de);
    }
}