package External;

public class UnzipEvent extends BusEvent {
    
    public enum UnzipEventType {
        unzipEventTypeStarted,
        unipEventTypeFinished,
        unzipEventTypeFailed
    }
    
    public UnzipEventType eventType;
    public String objectKey;
    
    public UnzipEvent() {
        eventName = EventBusFacade.eventKeyUnzipBook;
    }
    
    public UnzipEvent(UnzipEventType type, String key) {
        eventName = EventBusFacade.eventKeyUnzipBook;
        eventType = type;
        objectKey = key;
    }
}