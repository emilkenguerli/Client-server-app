/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package log;

/**
 *
 * @author Emil
 */

import java.sql.Connection;
import java.sql.*;
 
class Log {
 
    private String url = "jdbc:mysql://nightmare.cs.uct.ac.za:3306/kngemi002";
    private String user = "kngemi002";
    private String password = "eif4ooNu";
    private Connection conn = null;
    private Statement stmt = null;

    public Log() throws Exception{
        Class.forName("com.mysql.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Success");
        stmt = conn.createStatement();
    }


    public void addUserHistory(String id) throws Exception{
        String sql = "select * from chat_log where id = '" + id + "';";
        ResultSet rs = stmt.executeQuery(sql);
        boolean exist = false;
        while(rs.next()){
            String id2 = rs.getString("id");
            String message = rs.getString("message");
            String date = rs.getString("date_added");            
            System.out.println(id2 + ", " + message + ", " + date);
        }

    }
 
    public boolean groupMatchPair(String id, String password) throws Exception{
        String sql = "select * from groups where id = '" + id + "' and password = '" + password + "';";
        ResultSet rs = stmt.executeQuery(sql);
        boolean exist = false;
        while(rs.next()){
            exist = true;
        }
        return exist;

    }

    public boolean userMatchPair(String id, String password) throws Exception{
        String sql = "select * from users where id = '" + id + "' and password = '" + password + "';";
        ResultSet rs = stmt.executeQuery(sql);
        boolean exist = false;
        while(rs.next()){
            exist = true;
        }
        return exist;

    }

    public boolean doesUserExist(String id) throws Exception{
        String sql = "select * from users where id = '" + id + "';";
        ResultSet rs = stmt.executeQuery(sql);
        boolean exist = false;
        while(rs.next()){
            exist = true;
        }
        return exist;

    }

    public boolean doesGroupExist(String id) throws Exception{
        String sql = "select * from groups where id = '" + id + "';";
        ResultSet rs = stmt.executeQuery(sql);
        boolean exist = false;
        while(rs.next()){
            exist = true;
        }
        return exist;

    }

    public void insertIntoChat_History(String id, String message) throws Exception{
        //LocalTime time = new LocalTime(); //Need Joda Time package:https://github.com/JodaOrg/joda-time/releases
        //LocalDate date = new LocalDate();
        //String dateAdded = date.toString() + " " + time.toString();
        String dateAdded = "Monday";    //for testing            
        String sql = "insert chat_log values" + "('" + id + "', '" + message + "', '" + dateAdded + "')";
        System.out.println();
        stmt.executeUpdate(sql);
        System.out.println("Inserted message into chat_log");
    }

    public void insertIntoUsers(String id, String password) throws Exception{
        String sql = "insert users values" + "('" + id + "', '" + password + "')";
        System.out.println();
        stmt.executeUpdate(sql);
        System.out.println("Inserted record into users");
    }

    public void insertIntoGroups(String id, String password) throws Exception{
        String sql = "insert groups values" + "('" + id + "', '" + password + "')";
        System.out.println();
        stmt.executeUpdate(sql);
        System.out.println("Inserted record into groups");
    }

    public void printChat_Log() throws Exception{
        String sql = "select*from chat_log";
        ResultSet rs = stmt.executeQuery(sql);
        System.out.println("Data from chat_log table:");
        System.out.println();
        while(rs.next()){
            String id = rs.getString("id");
            String message = rs.getString("message");
            String date2 = rs.getString("date_added");            
            System.out.println(id + ", " + message + ", " + date2);

        }
    }

    public void printUsers() throws Exception{
        String sql = "select*from users";
        ResultSet rs = stmt.executeQuery(sql);
        System.out.println("Data from users table:");
        System.out.println();
        while(rs.next()){
            String id = rs.getString("id");
            String password = rs.getString("password");            
            System.out.println(id + ", " + password);

        }
    }

    public void printGroups() throws Exception{
        String sql = "select*from groups";
        ResultSet rs = stmt.executeQuery(sql);
        System.out.println("Data from groups table:");
        System.out.println();
        while(rs.next()){
            String id = rs.getString("id");
            String password = rs.getString("password");            
            System.out.println(id + ", " + password);

        }
    }

    public void deleteLog() throws Exception{
        System.out.println("Deleting data from chat_log table:");
        String sql = "delete from chat_log";
        stmt.executeUpdate(sql);

    }
 

}

