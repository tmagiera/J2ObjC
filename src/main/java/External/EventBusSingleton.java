package External;

import com.google.common.eventbus.EventBus;

public class EventBusSingleton {
    
    private EventBus eventbus;
    
    //singleton
    private static EventBusSingleton instance = null;
    protected EventBusSingleton() {
        
    }
    
    public static EventBusSingleton getInstance() {
        if(instance == null) {
            instance = new EventBusSingleton();
            instance.eventbus = new EventBus();
        }
        return instance;
    }
    
    public static void register(Object object) {
        EventBusSingleton.getInstance().eventbus.register(object);
    }
    
    public static void post(Object object) {
        EventBusSingleton.getInstance().eventbus.post(object);
    }
}