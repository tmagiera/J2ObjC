package External;

import java.util.HashMap;

import External.ServerRequestManager.ServerRequestMessage;


class LoginManager implements ServerRequestMessage {
    
    private static final String USER_NAME = "lrzemek@ydp.eu";
    private static final String USER_PASSWORD = "1234";
    
    private static boolean loggingIn = false;
    private static boolean loggedIn = false;
    private static String loggedInUser = "";
    
    public static String sessionId = "";
    
//singleton
    private static LoginManager instance = null;
    protected LoginManager() {
        
    }
    
    public static LoginManager getInstance() {
        if(instance == null) {
            instance = new LoginManager();
        }
        return instance;
    }
    

    public void login() {
        if(!loggingIn && !loggedIn) {
            loggingIn = true;
            ServerRequestManager serverRequestManager = ServerRequestManager.getInstance();
            serverRequestManager.loginUser(USER_NAME,USER_PASSWORD,this);
        } else if(loggingIn) {
            LoginEvent le = new LoginEvent("Login process in progress",false);
            EventBusSingleton.post(le);
        } else if(loggedIn) {
            LoginEvent le = new LoginEvent("User already logged in",false);
            EventBusSingleton.post(le);
        }
    }
    
    public void requestFailedMessage(String message, String method) {
        System.out.println("LOGIN FAILED" + message);
        loggingIn = false;
        LoginEvent le = new LoginEvent("because fuck you ,that's why",false);
        EventBusSingleton.post(le);
    }
    
    public void requestSuccessMessage(String message, String method) {
        
        ResponseParser parser = new ResponseParser();
        parser.parseLoginRequest(message);
        HashMap responseMap = parser.responseMap;
        String errorCode = (String)responseMap.get("errorCode");
        if(errorCode != null && errorCode == "0") {
            sessionId = (String)responseMap.get("session");
            loginAccepted();
        }
        else {
            sessionId = "";
            loginRejected();
        }
    }
    
    private void loginAccepted() {
        loggedIn = true;
        loggingIn = false;
        LoginEvent le = new LoginEvent("",true);
        EventBusSingleton.post(le);
    }
    
    private void loginRejected() {
        loggedIn = false;
        loggingIn = false;
        
        LoginEvent le = new LoginEvent("login rejected by server", false);
        EventBusSingleton.post(le);
    }  
}