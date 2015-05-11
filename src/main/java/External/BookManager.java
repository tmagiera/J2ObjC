package External;

import java.util.HashMap;

import External.DownloadManager.DownloadCallback;
import External.ServerRequestManager.ServerRequestMessage;
import External.UnZip.UnzipCallback;

class BookManager implements ServerRequestMessage, DownloadCallback, UnzipCallback {
    
    
    //singleton
    private static BookManager instance = null;
    protected BookManager() { }
    
    public static BookManager getInstance() {
        if(instance == null) {
            instance = new BookManager();
        }
        return instance;
    }
    
    //body
    public void refreshBookList() {
        ServerRequestManager.getInstance().getBookList(this);
    }
    
    public Book[] getBookList() {
        return DatabaseManager.getInstance().getBooksFromDatabase();
    }
    
    private void getBookListById() {
        Book[] booklist = DatabaseManager.getInstance().getBooksFromDatabase();
        ServerRequestManager.getInstance().getBooksById(this,booklist);
    }

    public void downloadBookData(Book book) {
        StorageManager.getInstance().checkCreateDirectoryForBook(book);
        String[] urls = book.chunks;
        if(urls.length > 0) {
            for(int i = 0; i < urls.length; i++) {
                String chunkUrl = urls[i];
                DownloadManager.getInstance().downloadFile(chunkUrl,
                                                           StorageManager.getInstance().zipPathForBook(book),
                                                           this,
                                                           book.code,
                                                           book.downloadSize);
            }
        }
    }
    

    
    //server request callbacks
    public void requestFailedMessage(String message, String method) {
        System.out.println("BOOK UPDATE FAILED[" + method + "] " + message);
    }
    
    public void requestSuccessMessage(String message, String method) {
        ResponseParser parser = new ResponseParser();
        HashMap responseMap = null;

        if(method.equals("getBooks")) {
            parser.parseGetBooksRequest(message);
            responseMap = parser.responseMap;
            synchronizeGetBooksResponseToDatabase(responseMap);
            getBookListById();
        }
        else if(method.equals("getBooksById")) {
            parser.parseGetBooksByIdRequest(message);
            responseMap = parser.responseMap;
            synchronizeGetBooksByIdResponseToDatabase(responseMap);
        }
    }
    
    //download callbacks
   public void downloadFailed(String message, String tag) {
       System.out.println("aqq");
    }
    
    public void downloadSuccess(String message, String tag) {
        Book book = DatabaseManager.getInstance().getBookByTag(tag);
        book.isDownloaded = true;
        DatabaseManager.getInstance().addUpdatBookToDatabase(book);
        unzipFile(book);
    }
    
    //database access
    private void synchronizeGetBooksResponseToDatabase(HashMap response) {
        HashMap[] books = (HashMap[])response.get("books");
        for(int i = 0; i < books.length; i++) {
            HashMap bookMap = books[i];
            if(bookMap != null) {
                Book book = new Book();
                book.code = (String)bookMap.get("code");
                book.cmsServer = (String)bookMap.get("cms");
                DatabaseManager.getInstance().addUpdatBookToDatabase(book);
            }
        }
    }
    
    private void synchronizeGetBooksByIdResponseToDatabase(HashMap response) {
        HashMap[] books = (HashMap[])response.get("books");
        for(int i = 0; i < books.length; i++) {
            HashMap bookMap = books[i];
            if(bookMap != null) {
                Book book = new Book();
                book.code = (String)bookMap.get("code");
                book.chunks = (String[])bookMap.get("urlChunks");
                book.title = (String)bookMap.get("title");
                book.author = (String)bookMap.get("author");
                book.url = (String)bookMap.get("url");
                book.thumb = (String)bookMap.get("thumbnail");
                book.type = (String)bookMap.get("bookType");
                book.downloadSize = Integer.parseInt((String)bookMap.get("chunksTotalSize"));
                DatabaseManager.getInstance().addUpdatBookToDatabase(book);
            }
        }
        postBookRefreshed();
    }
    
    private void postBookRefreshed() {
        BooksRefreshedEvent refreshEvent = new BooksRefreshedEvent();
        EventBusSingleton.post(refreshEvent);
    }
    
    //unzip
    public void unzipFile(Book book) {
        String bookPath = StorageManager.getInstance().pathForBook(book);
        String bookZipPath = StorageManager.getInstance().zipPathForBook(book);
        new UnZip(bookZipPath, bookPath, this, book.code);
    }
    
    public void UnzipSuccess(String path, String tag) {
        StorageManager.getInstance().deleteFile(path);
        System.out.println("Unzip success: " + tag);
    }
    
    public void UnzipFail(String message, String tag){
        System.out.println("Unzip fail: " + tag + " " + message);

    }
    
    
    //state
    public void saveStateForBook(Book book) {
        DatabaseManager.getInstance().addUpdatBookToDatabase(book);
    }
}