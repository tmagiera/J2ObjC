package External;

import java.util.HashMap;

class DatabaseManager {

    
//TEMP

//
    
//TEMP solution, until I get some sqlite framework transpiled
private HashMap<String, Book> tempDB;
//TEMP
    
//singleton
private static DatabaseManager instance = null;
    protected DatabaseManager() {
        
    }
    
    public static DatabaseManager getInstance() {
        if(instance == null) {
            instance = new DatabaseManager();
            //TEMP
            instance.tempDB = new HashMap(100);
            //
        }
        return instance;
    }
    
    public void addUpdatBookToDatabase(Book book) {
        //TEMP
        if(tempDB.containsKey(book.code)) {
            Book existingBook = (Book)tempDB.get(book.code);
            existingBook.copyEntriesFromBook(book);
            tempDB.put(book.code,existingBook);
        } else {
            tempDB.put(book.code,book);
        }
        //
    }
    
    public Book[] getBooksFromDatabase() {
        //TEMP
        return (Book[]) tempDB.values().toArray(new Book[0]);
        //
    }
    
    public Book getBookByTag(String tag) {
        //TEMP
        if(tempDB.containsKey(tag)) {
            return (Book)tempDB.get(tag);
        }
        return null;
        //
    }
}