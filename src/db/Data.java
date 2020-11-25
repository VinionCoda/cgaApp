/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * Each function returns the DriverManager object for database stored on
 * separate database server types.
 *
 *
 * @author Kevin Mahon
 */
public class Data {

    private static final Logger LOGR = LogManager.getLogger(Data.class);

    /**
     * ********************* Connections *************************
     *///Access SQLExpress Server  
    public static Connection openCon() {
        try {
            String connectionUrl = "jdbc:sqlserver://192.168.0.30:1433; database=finance;user=sa;password={P@ssword123};";
            return DriverManager.getConnection(connectionUrl);
        } // Handle any errors that may have occurred.
        catch (SQLException e) {
            LOGR.fatal("Connection to program database failed");
            return null;
        }
    }
    
    //Access SQLExpress Server    
    public static Connection openUsers() {
        try {
            String connectionUrl = "jdbc:sqlserver://192.168.0.30:1433; database=global_users;user=sa;password={P@ssword123};";
            return DriverManager.getConnection(connectionUrl);
        } // Handle any errors that may have occurred.
        catch (SQLException e) {
             LOGR.fatal("Connection to user database failed");
            return null;
        }
    }    

    //Access Macola database from old 2003sql server
    public static Connection openMacola() {
        try {
            String connectionUrl = "jdbc:jtds:sqlserver://192.168.0.1/data;namedPipe=true";
            return DriverManager.getConnection(connectionUrl);
        } // Handle any errors that may have occurred.
        catch (SQLException e) {
             LOGR.fatal("Connection to macola database failed");
            return null;
        }
    }
    
    //Access SQLExpress Server
    public static Connection openMysql() {
        try {
            String connectionUrl = "jdbc:mysql://localhost/test?user=admin&password=P@ssw0rd123";
            return DriverManager.getConnection(connectionUrl);
        } // Handle any errors that may have occurred.
        catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }    
    



}

