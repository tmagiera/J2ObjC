package External;

import java.util.HashMap;
import java.util.Map;

import External.HTTPConnection.HTTPRequestCallback;


class ServerRequestManager implements HTTPRequestCallback  {
    
    private static final String LOGIN_USER = "loginUser";
    private static final String GET_BOOKS = "getBooks";
    private static final String GET_BOOKS_BY_ID = "getBooksById";
    
    interface ServerRequestMessage {
        void requestFailedMessage(String message, String method);
        void requestSuccessMessage(String message, String method);
    }
    
    private static Map callbackTable;
    
//singleton
    private static ServerRequestManager instance = null;
    protected ServerRequestManager() {
        
    }
    
    public static ServerRequestManager getInstance() {
        if(instance == null) {
            instance = new ServerRequestManager();
            callbackTable = new HashMap();
        }
        return instance;
    }
    
    
//requests
    public void loginUser(String user, String password, ServerRequestMessage callback) {
        registerCallback(LOGIN_USER,callback);
        
        if(!ConnectionManager.getInstance().isConnectionUp()) {
            System.out.println("No connection, oh no!");
            return;
        }

        String loginRequest = RequestURLCreator.loginRequestURL(user,password);
        new HTTPConnection(loginRequest,this, LOGIN_USER);
    }
    
    public void getBookList(ServerRequestMessage callback) {
        registerCallback(GET_BOOKS,callback);
        
        if(!ConnectionManager.getInstance().isConnectionUp()) {
            System.out.println("No connection, oh no!");
            return;
        }
        
        String getBooksRequest = RequestURLCreator.getBooksRequestURL();
        new HTTPConnection(getBooksRequest,this, GET_BOOKS);
    }
    
    public void getBooksById(ServerRequestMessage callback, Book[] bookList) {
        registerCallback(GET_BOOKS_BY_ID,callback);
        
        if(!ConnectionManager.getInstance().isConnectionUp()) {
            System.out.println("No connection, oh no!");
            return;
        }
        
        String getBooksRequest = RequestURLCreator.getBooksByIdRequestURL(bookList);
        new HTTPConnection(getBooksRequest,this, GET_BOOKS_BY_ID);
    }
    
//callbacks
    private void registerCallback(String callbackKey, ServerRequestMessage callback) {
        //if(!callbackTable.containsValue(callback)) {
            callbackTable.put(callbackKey, callback);
        //}
    }
    
    private void unregisterCallback(String callbackKey) {
        //if(callbackTable.containsKey(callbackKey)) {
            callbackTable.remove(callbackKey);
        //}
    }
    
    private ServerRequestMessage getCallback(String callbackKey) {
        return (ServerRequestMessage)callbackTable.get(callbackKey);
    }
    
    
//callbacks
    public void requestFailed(String message, String tag) {
        ServerRequestMessage callback = getCallback(tag);
        callback.requestFailedMessage(message,tag);
        unregisterCallback(tag);
        System.out.println("requestFailed"+message);
    }
    
    public void requestSuccess(String message, String tag) {
        ServerRequestMessage callback = getCallback(tag);
        callback.requestSuccessMessage(message,tag);
        unregisterCallback(tag);
        System.out.println("requestSuccess"+message);
    }
    

    
}