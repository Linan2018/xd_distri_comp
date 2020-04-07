# 第3次作业设计报告

## 1. 服务端程序

### 1.1 远程对象

要注册到*RMI Registry*的远程对象是`BookSys`类（skeleton），定义为

```java
public interface BookSys extends Remote{
    // 添加一个书籍对象
    boolean add(Book b) throws RemoteException;
    // 查询指定ID的书籍对象
    Book queryByID(int bookID) throws RemoteException;
    // 按书名查询书籍对象列表
    BookList queryByName(String name) throws RemoteException;
    // 删除指定ID的书籍对象
    boolean delete(int bookID) throws RemoteException;
    // 展示全部书籍信息
    String show() throws RemoteException;
}
```

然后将远程对象服务的名称`BookSys`与套接字`127.0.0.1:1099`绑定

### 1.2 其他对象的设计

`Book`对象可序列化，能够作为RMI的传递对象，其定义为

```java
public class Book implements Serializable{
    private int ID;
    private String name;
    public Book(int ID, String name);
    // 复制构造函数
    public Book(Book book);
    // 获取ID
    public int getID();
    // 获取名称
    public String getName();
}
```

`BookList`对象可序列化，能够作为RMI的传递对象，主要包含一个能够存放`Book`对象的`ArrayList`，其定义为

```java
public class BookList implements Serializable{
    public ArrayList<Book>  list;
    public BookList();
    public void add(Book b);
}
```

## 2. 客户端程序

客户端也创建一个新的`bookSys`对象（stub），但其实现不在客户端，客户端可以通过这个代理远程调用，服务端skeleton暴露出的方法。在客户端看来，好像在调用本地方法一样。

同样地，在客户端也要实现可序列化的对象`Book`和`BookList`，通过RMI传递。

## 3. 实验运行
运行`run_this.bat`
将打开2个终端，其中1个运行服务器程序，1个运行客户端程序，客户端程序进行多次远程调用。

1. 加入3个书籍对象

   ```java
   Book book1 = new Book(1, "abc");
   Book book2 = new Book(25, "defgh");
   Book book3 = new Book(30, "defgh");
   ```

2. 展示全部书籍

3. 查询ID为`25`的书籍对象

4. 查询名称为`defgh`的书籍对象

5. 删除ID为`30`的书籍对象

6. 展示全部书籍

结果（客户端）：

```
test add
All books:
ID: 1   Name: abc
ID: 25  Name: defgh
ID: 30  Name: defgh

test queryByID
queryByID - ID: 25      Name: defgh

test queryByName
queryByName - ID: 25    Name: defgh
queryByName - ID: 30    Name: defgh

test delete
All books:
ID: 1   Name: abc
ID: 25  Name: defgh

```

