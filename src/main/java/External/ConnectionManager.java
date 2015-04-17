package External;

class ConnectionManager {

//singleton
    private static ConnectionManager instance = null;
    protected ConnectionManager() {
        
    }
    
    public static ConnectionManager getInstance() {
        if(instance == null) {
            instance = new ConnectionManager();
        }
        return instance;
    }

    
//body
    public boolean isConnectionUp() {
        //now we propably won't neet that in the PoC anyway ...
        return true;
    }
}