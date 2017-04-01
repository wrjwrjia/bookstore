/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.DBProject;

import java.util.*;

/**
 *
 * @author jia
 */
public class Service {
    public List<Map<String, Object>> getBook(int select, String input){
        String sql = null;
        List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
        //input is ISBN
        if(select == 1)
        {
            sql = "select b.ISBN, ba.author, b.name, c.major, bs.location, cl.addedDate" +
                    " from Book b, BookAuthor ba, Category c, Bookstore bs, Classification cl" +
                    " where b.partOf = bs.id" +
                    " and c.partOf = bs.id" +
                    " and ba.book = b.ISBN" +
                    " and cl.belongs = b.ISBN" +
                    " and cl.isBelongedTo = c.id" +
                    " and b.ISBN = ?";
        }
        //input is name
        else if(select == 2) {
            sql = "select b.ISBN, ba.author, b.name, c.major, bs.location, cl.addedDate" +
                " from Book b, BookAuthor ba, Category c, Bookstore bs, Classification cl" +
                " where b.partOf = bs.id" +
                " and c.partOf = bs.id" +
                " and ba.book = b.ISBN" +
                " and cl.belongs = b.ISBN" +
                " and cl.isBelongedTo = c.id" +
                " and b.name = ?";
        }
        //input is author
        else if(select == 3) {
            sql = "select b.ISBN, ba.author, b.name, c.major, bs.location, cl.addedDate" +
                " from Book b, BookAuthor ba, Category c, Bookstore bs, Classification cl" +
                " where b.partOf = bs.id" +
                " and c.partOf = bs.id" +
                " and ba.book = b.ISBN" +
                " and cl.belongs = b.ISBN" +
                " and cl.isBelongedTo = c.id" +
                " and ba.author = ?";
        }
        //input is addedDate
        else if(select == 4) {
            sql = "select b.ISBN, ba.author, b.name, c.major, bs.location, cl.addedDate" +
                " from Book b, BookAuthor ba, Category c, Bookstore bs, Classification cl" +
                " where b.partOf = bs.id" +
                " and c.partOf = bs.id" +
                " and ba.book = b.ISBN" +
                " and cl.belongs = b.ISBN" +
                " and cl.isBelongedTo = c.id" +
                " and cl.addedDate = ?";
        }
        //input is major
        else{
            sql = "select b.ISBN, ba.author, b.name, c.major, bs.location, cl.addedDate" +
                " from Book b, BookAuthor ba, Category c, Bookstore bs, Classification cl" +
                " where b.partOf = bs.id" +
                " and c.partOf = bs.id" +
                " and ba.book = b.ISBN" +
                " and cl.belongs = b.ISBN" +
                " and cl.isBelongedTo = c.id" +
                " and c.major = ?";
        }
        list = databaseJDBC.queryForList(sql, input);
        
        return list;
    }
    
    private static boolean isEmpty(String str) {
        return null == str || str.equals("") || str.matches("\\s*");
    }
    
    public boolean insertBook (Object...objects) {
        String sql1 = "insert into bookstore(ISBN, location) values (?, ?)";
        
        String sql2 = "insert into book(ISBN, name, partOf) select ISBN, ?, bs.id from bookstore bs" +
                    " where bs.ISBN = ?";
        
        String sql3 = "insert into bookauthor(book, author) values (?, ?)";
        
        String sql4 = "insert into category(major, location, partOf)" +
                    " select ?, ?, bs.id from bookstore bs" +
                    " where bs.ISBN = ?";
        
        String sql5 = "insert into classification(belongs, isBelongedTo, addedDate)" +
                    " select ?, c.id, ? from category c, bookstore bs" +
                    " where bs.id = c.partOf and bs.ISBN = ?";
        
        //if sql execution fail, return false
        if(!databaseJDBC.upOperation(sql1, objects[0], objects[5])) 
            return false;
        if(!databaseJDBC.upOperation(sql2, objects[1], objects[0]))
            return false;
        if(!databaseJDBC.upOperation(sql3, objects[0], objects[2]))
            return false;
        if(!databaseJDBC.upOperation(sql4, objects[4], objects[5], objects[0]))
            return false;
        if(!databaseJDBC.upOperation(sql5, objects[0], objects[3], objects[0]))
            return false;
        
        //multi authors
        if(!isEmpty(objects[6].toString())) {
            if(!databaseJDBC.upOperation(sql3, objects[0], objects[6]))
            return false;
        }
        
        return true;
    }
    
    public boolean updateBook (List<String> old, Object...objects) {
        String sql1 = "update Book set ISBN = ? where ISBN = ?";
        
        String sql2 = "update BookStore set ISBN = ? where ISBN = ?";
        
        String sql3 = "update Book set name = ? where name = ?";
        
        String sql4 = "update BookAuthor set author = ? where exists" +
                    " (select * from Book b where author = ? and b.ISBN = ? and b.ISBN = book)";

        if(!databaseJDBC.upOperation(sql1, objects[0], old.get(0))) 
            return false;
        if(!databaseJDBC.upOperation(sql2, objects[0], old.get(0)))
            return false;
        if(!databaseJDBC.upOperation(sql3, objects[1], old.get(1)))
            return false;
        if(!databaseJDBC.upOperation(sql4, objects[2], old.get(2), old.get(0)))
            return false;
        
        return true;
    }
    
    public boolean deleteBook (int select, Object object) {
        if(select == 1)
        {
            String sql1 = "delete from Book where ISBN = ?";

            String sql2 = "delete from Bookstore where ISBN = ?";
            
            if(!databaseJDBC.upOperation(sql1, object)) 
                return false;
            if(!databaseJDBC.upOperation(sql2, object))
                return false;
        }
        else
        {
            String sql1 = null;
            if (select == 2) {
                sql1 = "select ISBN from Book where name = ?";
            }
            else {
                sql1 = "select distinct book from BookAuthor where author = ?";
            }
            
            List<Map<String, Object>> results = new ArrayList<Map<String, Object>>();
            results = databaseJDBC.queryForList(sql1, object);
            
            String sql2 = "delete from Book where ISBN = ?";
            String sql3 = "delete from Bookstore where ISBN = ?";
            
            for (Map<String, Object> m : results) {  
                for (String k : m.keySet()) { 
                    if(!databaseJDBC.upOperation(sql2, m.get(k))) 
                        return false;
                    if(!databaseJDBC.upOperation(sql3, m.get(k)))
                        return false;
                }
            }
        }
        
        return true;
    }
}
