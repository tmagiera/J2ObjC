package External;

import com.google.common.eventbus.Subscribe;

import External.EventBusFacade.EventBusCallback;

class EventBusManager {

    
    private EventBusCallback eventBusCallback;
    
    //singleton
    private static EventBusManager instance = null;
    protected EventBusManager() {
        
    }
    
    public static EventBusManager getInstance() {
        if(instance == null) {
            instance = new EventBusManager();
            EventBusSingleton.register(instance);
        }
        return instance;
    }
    
    public void registerCallback(EventBusCallback callback) {
        eventBusCallback = callback;
    }
    
    @Subscribe
    public void handleBusEvent(BusEvent event) {
        eventBusCallback.handleEvent(event);
    }
    
//    @Subscribe
//    public void handleLoginBusEvent(LoginEvent event) {
//        eventBusCallback.handleEvent(event);
//    }


    public void postExternalEvent(BusEvent event) {
        EventBusSingleton.post(event);
    } 
}