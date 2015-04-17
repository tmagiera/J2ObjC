package External;

import java.io.File;

public class StorageManager {
 
    public String storageDirectory;
    
    
    //singleton
    private static StorageManager instance = null;
    protected StorageManager() { }
    
    public static StorageManager getInstance() {
        if(instance == null) {
            instance = new StorageManager();
        }
        return instance;
    }
    
    public void setApplicationStorageDirectory(String path) {
        storageDirectory = path;
    }
    
    public String getApplicationStorageDirectory() {
        if(storageDirectory == null) {
            System.out.println("ERROR - NO APPLICATION DIRECTORY SET");
            return "";
        }
        return storageDirectory;
    }
    
    public String pathForBook(Book book) {
        return storageDirectory + "/" + book.code;
    }
    
    public String zipPathForBook(Book book) {
        return pathForBook(book) + "/content.zip";
    }
    
    public String contentPathForBook(Book book) {
        return pathForBook(book) + "/content";
    }
    
    public String thumbPathForBook(Book book) {
        return pathForBook(book);
    }
    
    public String tempDataPathForBook(Book book) {
        return pathForBook(book) + "/temp";
    }
    
    public void deleteFile(String path) {
        try {
            File file = new File(path);
    
            if(file.delete()) {
                System.out.println(file.getName() + " is deleted!");
            }
            else {
                System.out.println("Delete operation is failed.");
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    public boolean fileExistsAtPath(String path) {
        File f = new File(path);
        return f.exists() && !f.isDirectory();
    }
    
    public boolean directoryExistsAtPath(String path) {
        File f = new File(path);
        return f.exists() && f.isDirectory();
    }
    
    public void checkCreateDirectoryForBook(Book book) {
        String path = pathForBook(book);
        if(!directoryExistsAtPath(path)){
            File f = new File(path);
            f.mkdirs();
        }
    }
}