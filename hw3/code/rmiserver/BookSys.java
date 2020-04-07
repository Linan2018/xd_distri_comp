import java.rmi.Remote;
import java.rmi.RemoteException;

public interface BookSys extends Remote{
    boolean add(Book b) throws RemoteException;
    Book queryByID(int bookID) throws RemoteException;
    BookList queryByName(String name) throws RemoteException;
    boolean delete(int bookID) throws RemoteException;
    String show() throws RemoteException;
}