package External;

public class LoginEvent extends BusEvent {
    
    public boolean loginSuccess;
    
    public LoginEvent() {
        eventName = EventBusFacade.eventKeyLogin;
    }

    public LoginEvent(String message, boolean success) {
        eventName = EventBusFacade.eventKeyLogin;
        eventMessage = message;
        loginSuccess = success;
    }
}