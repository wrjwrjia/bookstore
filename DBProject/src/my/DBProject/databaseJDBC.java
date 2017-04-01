/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.DBProject;

import java.sql.*;
import java.util.*;

/**
 *
 * @author jia
 */
public class databaseJDBC {
    private static final String username = "root";
    private static final String password = "Wrj2015!";
    private static final String dataBaseName = "bookstore";
    
    private static Connection connection = null;
    
    //connect to the database
    public static void getConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection  = DriverManager.getConnection("jdbc:mysql://localhost:3306/"+dataBaseName+"?useUnicode=true&characterEncoding=utf8&useSSL=true",username,password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //close the connection
    public static void closeConnection(){
        try {
            connection.close();
        } catch (Exception e) {
            e.fillInStackTrace();
        }
    }

    //execute insert, update, delete operation
    public static boolean upOperation(String sql,Object...objects){
        PreparedStatement statement = null;
        try {
            statement = (PreparedStatement) connection.prepareStatement(sql);
            for (int i = 0; i < objects.length; i++) {
                statement.setObject(i+1, objects[i]);
            }
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    //execute select operation, and return results in list
    public static List<Map<String,Object>> queryForList(String sql, Object...objects){
        List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            statement = connection.prepareStatement(sql);
            for (int i = 0; i < objects.length; i++) {
                statement.setObject(i+1, objects[i]);
            }

            rs = statement.executeQuery();
            while(rs.next()){
                ResultSetMetaData resultSetMetaData = rs.getMetaData();
                int count = resultSetMetaData.getColumnCount(); //get row size
                Map<String,Object> map = new HashMap<String, Object>();
                for (int i = 0; i < count; i++) {
                    map.put(resultSetMetaData.getColumnName(i+1), rs.getObject(resultSetMetaData.getColumnName(i+1)));
                }
                result.add(map);
            };
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}
