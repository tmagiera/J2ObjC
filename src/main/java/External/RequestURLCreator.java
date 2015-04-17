package External;

class RequestURLCreator {

    private static final String SERVER_URL = "http://demo-bookshelf-assodb.ydp.eu/ctrl.php/api";

    
    public static String loginRequestURL(String user, String password) {
        return SERVER_URL + "/verifyUser?login="+user+"&password="+password;
    }

    public static String getBooksRequestURL() {
        return SERVER_URL + "/getEbooksForUser?sessionId="+LoginManager.sessionId;
    }
    
    public static String getBooksByIdRequestURL(Book[] books) {
        if(books.length > 0) {
            String ebookIds = "";
            String cmsUrl = "";
            for(int i = 0; i < books.length; i++) {
                Book book = books[i];
                if(i == 0) {
                    ebookIds = book.code;
                    cmsUrl = book.cmsServer;
                } else {
                    ebookIds += ",";
                    ebookIds += book.code;
                }
            }
            
            return cmsUrl + "/getEbooks?ebookKey="+ebookIds;
        }
        return "";
    }
}