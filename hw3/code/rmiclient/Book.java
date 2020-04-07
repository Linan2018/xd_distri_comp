import java.io.*;

public class Book implements Serializable{
    private int ID;
    private String name;
    public Book(int ID, String name){
        this.ID = ID;
        this.name = name;
    }
    public Book(Book book){
        this.ID = book.ID;
        this.name = book.name;
    }
    public int getID(){
        return ID;
    }
    public String getName(){
        return name;
    }
}