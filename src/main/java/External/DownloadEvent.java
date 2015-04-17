package External;

public class DownloadEvent extends BusEvent {
    
    public enum DownloadEventType {
        downloadEventTypeStarted,
        downloadEventTypeFinished,
        downloadEventTypeProgress,
        downloadEventTypeFailed
    }
    
    public DownloadEventType eventType;
    public String objectKey;
    public float progressValue;
    
    public DownloadEvent() {
        eventName = EventBusFacade.eventKeyDownloadBook;
    }
    
    public DownloadEvent(DownloadEventType type, String key, float value) {
        eventName = EventBusFacade.eventKeyDownloadBook;
        eventType = type;
        objectKey = key;
        progressValue = value;
    }
}