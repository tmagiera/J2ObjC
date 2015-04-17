package External;

public class BooksRefreshedEvent extends BusEvent {

    public BooksRefreshedEvent() {
        eventName = EventBusFacade.eventKeyBooksRefreshed;
    }
}