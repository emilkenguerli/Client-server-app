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
// import org.joda.time.*; //Need Joda Time package:https://github.com/JodaOrg/joda-time/releases
 
class Log {
 
    private static final String url = "jdbc:mysql://nightmare.cs.uct.ac.za:3306/kngemi002";
 
    private static final String user = "kngemi002";
 
    private static final String password = "eif4ooNu"; // "aM6meib5"; //wohpheal";

    private static Connection conn = null;
    private static Statement stmt = null;

    public static void addUserHistory(String id) throws Exception{
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
 
    public static boolean groupMatchPair(String id, String password) throws Exception{
        String sql = "select * from groups where id = '" + id + "' and password = '" + password + "';";
        ResultSet rs = stmt.executeQuery(sql);
        boolean exist = false;
        while(rs.next()){
            exist = true;
        }
        return exist;

    }

    public static boolean userMatchPair(String id, String password) throws Exception{
        String sql = "select * from users where id = '" + id + "' and password = '" + password + "';";
        ResultSet rs = stmt.executeQuery(sql);
        boolean exist = false;
        while(rs.next()){
            exist = true;
        }
        return exist;

    }

    public static boolean doesUserExist(String id) throws Exception{
        String sql = "select * from users where id = '" + id + "';";
        ResultSet rs = stmt.executeQuery(sql);
        boolean exist = false;
        while(rs.next()){
            exist = true;
        }
        return exist;

    }

    public static boolean doesGroupExist(String id) throws Exception{
        String sql = "select * from groups where id = '" + id + "';";
        ResultSet rs = stmt.executeQuery(sql);
        boolean exist = false;
        while(rs.next()){
            exist = true;
        }
        return exist;

    }

    public static void insertIntoChat_History(String id, String message) throws Exception{
        //LocalTime time = new LocalTime(); //Need Joda Time package:https://github.com/JodaOrg/joda-time/releases
        //LocalDate date = new LocalDate();
        //String dateAdded = date.toString() + " " + time.toString();
        String dateAdded = "Monday";    //for testing            
        String sql = "insert chat_log values" + "('" + id + "', '" + message + "', '" + dateAdded + "')";
        System.out.println();
        stmt.executeUpdate(sql);
        System.out.println("Inserted message into chat_log");
    }

    public static void insertIntoUsers(String id, String password) throws Exception{
        String sql = "insert users values" + "('" + id + "', '" + password + "')";
        System.out.println();
        stmt.executeUpdate(sql);
        System.out.println("Inserted record into users");
    }

    public static void insertIntoGroups(String id, String password) throws Exception{
        String sql = "insert groups values" + "('" + id + "', '" + password + "')";
        System.out.println();
        stmt.executeUpdate(sql);
        System.out.println("Inserted record into groups");
    }

    public static void printChat_Log() throws Exception{
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

    public static void printUsers() throws Exception{
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

    public static void printGroups() throws Exception{
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

    public static void deleteLog() throws Exception{
        System.out.println("Deleting data from chat_log table:");
        String sql = "delete from chat_log";
        stmt.executeUpdate(sql);

    }
 
    public static void main(String args[]) throws Exception{
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Success");
        stmt = conn.createStatement();

        //insertIntoChat_History("10", "bka");
        //insertIntoUsers("10", "Bla");
             //insertIntoGroups("50", "Red Army");
        //printChat_Log();
        //printUsers();
        //printGroups();
        //boolean exist = doesUserExist("15");
        //boolean exist2 = doesGroupExist("50");
        //boolean match = groupMatchPair("50", "Red Army");
        //boolean match2 = userMatchPair("10", "Bla");
        //addUserHistory("10");

 
        } catch (Exception e) {
            e.printStackTrace();
        }

    
    }

}
