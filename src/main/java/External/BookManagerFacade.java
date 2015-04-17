package External;



public class BookManagerFacade {

    public static void refreshBookList() {
        BookManager.getInstance().refreshBookList();
    }
    
    public static Book[] getBookList() {
        return BookManager.getInstance().getBookList();
    }
    
    public static void downloadBookData(Book book) {
        BookManager.getInstance().downloadBookData(book);
    }
    
    public static void downloadBookThumbnail(Book book) {
        
    }
    
    public static void saveStateForBook(Book book) {
        BookManager.getInstance().saveStateForBook(book);
    }

}