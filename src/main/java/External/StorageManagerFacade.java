package External;

public class StorageManagerFacade {

    public static void setApplicationStorageDirectory(String directory) {
        StorageManager.getInstance().setApplicationStorageDirectory(directory);
    }

}