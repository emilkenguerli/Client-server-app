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
import java.sql.*;
import javax.sql.*;
import javax.naming.*;
import org.joda.time.*; //Need Joda Time package

public class Log{
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/chat_history"; //nightmare

    static final String USER = "root";
    static final String PASS = "Emilious98";

    public static void main(String[] args) throws Exception{
        Connection conn = null;
        Statement stmt = null;

        try{
            Class.forName(JDBC_DRIVER).newInstance();
            System.out.println("Driver loaded");
        
            System.out.println("Connecting to Database");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();
            String sql;

            //When send is clicked

            String id = clientSocket.getInetAddress().toString(); //Need to be put in correct class
            String message = textfield.getText();                 //Need to connect to gui textfield
            LocalTime time = new LocalTime();               //Need Joda Time package
            LocalDate date = new LocalDate();
            String dateAdded = date.toString() + " " + time.toString();
            sql = "insert into chat_log values" + "(" + id +  ", " + message + ", " + dateAdded + ")";
            System.out.println();
            stmt.executeUpdate(sql);
            System.out.println("Inserted message into chat_log:");

            //To delete logs

            System.out.println("Deleting data from chat_log table:");
            sql = "delete from chat_log";
            stmt.executeUpdate(sql);
            
            //To print out everything

            sql = "select*from chat_log";
            ResultSet rs = stmt.executeQuery(sql);
            System.out.println("Data from chat_log table:");
            System.out.println();
            while(rs.next()){
                id = rs.getInt("id") + "";
                message = rs.getString("message");
                String date = rs.getString("date_added");            
                System.out.println(id + ", " + message + ", " + date);

            }





        }
        catch(ClassNotFoundException e){
            throw new IllegalStateException("Cannot find the driver in the classpath", e);
        }

        catch(SQLException se){
            se.printStackTrace();
        
        }
        finally{
            try{
                if(conn != null) conn.close();
            }
            catch(SQLException se){
                se.printStackTrace();
            }
        }



    }
}