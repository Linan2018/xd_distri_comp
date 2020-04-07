

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class MyRMIClient {
    public static void main(String args[]) {

        try {
            String name = "BookSys";
            String serverIP = "127.0.0.1";  
            int serverPort = 1099;
			Registry registry = LocateRegistry.getRegistry(serverIP, serverPort);
			BookSys bookSys = (BookSys) registry.lookup(name);
			
            // test add
			System.out.println("test add");
			Book book1 = new Book(1, "abc");
			Book book2 = new Book(25, "defgh");
			Book book3 = new Book(30, "defgh");
			if (bookSys.add(book1) && bookSys.add(book2) && bookSys.add(book3)){
				System.out.println("All books:" + bookSys.show());
			}
            
			//test queryByID
			System.out.println("test queryByID");
			Book book4 = bookSys.queryByID(25);
			if (book4 != null){
				System.out.println("queryByID - ID: " + book4.getID() + "\tName: " + book4.getName() + "\n");
			}
			
			//test queryByName
			System.out.println("test queryByName");
			BookList books = bookSys.queryByName("defgh");
			if (books != null){
				for(int i = 0; i < books.list.size(); i++) {
					Book book = books.list.get(i);
					System.out.println("queryByName - ID: " + book.getID() + "\tName: " + book.getName());
				}
				System.out.println("");
			}
			
			//test delete
			System.out.println("test delete");
			if (bookSys.delete(30)){
				System.out.println("All books:" + bookSys.show());
			}
			
        } catch (Exception e) {
            System.err.println("??? exception:");
            e.printStackTrace();
        }
    }
}