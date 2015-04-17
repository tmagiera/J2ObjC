package External;

import java.util.HashMap;
import java.util.Map;

import External.HTTPDownload.HTTPDownloadCallback;



class DownloadManager implements HTTPDownloadCallback  {
    
    
    interface DownloadCallback {
        void downloadFailed(String message, String tag);
        void downloadSuccess(String message, String tag);
    }
    
    private static Map callbackTable;
    
    //singleton
    private static DownloadManager instance = null;
    protected DownloadManager() {
        
    }
    
    public static DownloadManager getInstance() {
        if(instance == null) {
            instance = new DownloadManager();
            callbackTable = new HashMap();
        }
        return instance;
    }
    
    
    //body
    public void downloadThumbnailForBook(Book book, DownloadCallback callback) {
        String downloadUrl = book.url + book.thumb;
        if(!callbackTable.containsKey(downloadUrl)) {
            downloadFile(downloadUrl,
                         StorageManager.getInstance().thumbPathForBook(book),
                         callback,
                         downloadUrl,
                         book.downloadSize);
        }
    }
    
    public void downloadFile(String url, String path, DownloadCallback callback, String tag, int size) {
        registerCallback(tag, callback);
        new HTTPDownload(url, this, tag, path, size);
    }
    
    
    //callback hashmap
    private void registerCallback(String callbackKey, DownloadCallback callback) {
        callbackTable.put(callbackKey, callback);
    }
    
    private void unregisterCallback(String callbackKey) {
        callbackTable.remove(callbackKey);
    }
    
    private DownloadCallback getCallback(String callbackKey) {
        return (DownloadCallback)callbackTable.get(callbackKey);
    }
    
    //callbacks
    public void downloadFailed(String message, String tag) {
        DownloadCallback callback = getCallback(tag);
        callback.downloadFailed(message,tag);
        unregisterCallback(tag);
        System.out.println("downloadFailed"+message);
    }
    
    public void downloadSuccess(String message, String tag) {
        DownloadCallback callback = getCallback(tag);
        callback.downloadSuccess(message,tag);
        unregisterCallback(tag);
        System.out.println("downloadSuccess"+message);
    }
}