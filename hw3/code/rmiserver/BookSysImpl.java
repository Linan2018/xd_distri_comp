import java.rmi.RemoteException;
import java.util.*;

public class BookSysImpl implements BookSys {
    public ArrayList<Book> list;

    public BookSysImpl(){
        list = new ArrayList<Book>();
    }

    @Override
    public boolean add(Book b) throws RemoteException {
		System.out.println("Calling add");
        list.add(b);
        return true;
    }
	
    public Book queryByID(int bookID) throws RemoteException {
		System.out.println("Calling queryByID");
        Book book = null;
        for(int i = 0; i < list.size(); i++) {
            book = list.get(i);
            if (book.getID() == bookID) {
                break;
            }
        }
        return book;
    }

    public BookList queryByName(String name) throws RemoteException {
		System.out.println("Calling queryByName");
        BookList bookList = new BookList();
        for(int i = 0; i < list.size(); i++) {
            Book book = list.get(i);
            if (name.equals(book.getName())) {
                bookList.add(new Book(book));
            }
        }
        return bookList;
    }

    public boolean delete(int bookID) throws RemoteException {
		System.out.println("Calling delete");
        for(int i = 0; i < list.size(); i++) {
            Book book = list.get(i);
            if (book.getID() == bookID) {
                list.remove(book);
                return true;
            }
        }
        return false;
    }

    
    public String show() throws RemoteException {
		System.out.println("Calling show");
        String s = "\n";
        for(int i = 0; i < list.size(); i++) {
            Book book = list.get(i);
            s += "ID: " + book.getID() + "\tName: " + book.getName() + "\n";
        }
        return s;
    }

    
}
