import java.util.*;
import java.io.*;

public class BookList implements Serializable{
    public ArrayList<Book>  list;
    public BookList(){
        list = new ArrayList<Book>();
    }
    public void add(Book b){
        list.add(b);
    }
}