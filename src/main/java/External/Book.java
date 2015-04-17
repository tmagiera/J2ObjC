package External;

public class Book {
    public String code;
    public String cmsServer;
    public String[] chunks;
    public String title;
    public String author;
    public String url;
    public String thumb;
    public String thumbPath;
    public String path;
    public String type;
    public boolean isDownloaded;
    public int downloadSize;
    public String bookState;
    
    public void copyEntriesFromBook(Book book) {
        if(book.code != null) code = book.code;
        if(book.cmsServer != null) cmsServer = book.cmsServer;
        if(book.chunks != null) chunks = book.chunks;
        if(book.title != null) title = book.title;
        if(book.author != null) author = book.author;
        if(book.url != null) url = book.url;
        if(book.thumb != null) thumb = book.thumb;
        if(book.thumbPath != null) thumbPath = book.thumbPath;
        if(book.path != null) path = book.path;
        if(book.type != null) type = book.type;
        if(book.downloadSize != 0) downloadSize = book.downloadSize;
        if(book.bookState != null) bookState = book.bookState;
    }
}