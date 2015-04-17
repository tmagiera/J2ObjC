package External;

public class EventBusFacade {
    
    public static String eventKeyLogin = "POCeventKeyLogin";
    public static String eventKeyBooksRefreshed = "POCEventBooksRefreshed";
    public static String eventKeyDownloadBook = "POCEventDownloadBook";
    public static String eventKeyUnzipBook = "POCEventUnzipBook";

    public interface EventBusCallback {
        public void handleEvent(BusEvent event);
    }
    
    public static void registerEventHandler(EventBusCallback callback) {
        EventBusManager.getInstance().registerCallback(callback);
    }
    
    public static void postEvent(BusEvent event) {
        EventBusManager.getInstance().postExternalEvent(event);
    }
}