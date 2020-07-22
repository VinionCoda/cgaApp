/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

import static db.Data.openCon;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import basic.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author takeb
 */
public class Login extends Data {
    
    private static final Logger LOGR = LogManager.getLogger(Login.class);

    /**
     * ********************* Users *************************
     */
    //Function returns boolean and user object 
    public static final Object[] getUser(String username, String password) {
        Object[] arr = new Object[2];
        arr[0] = false;
        String SQL = "SELECT * FROM dbo.inv_user WHERE user_id = ? and user_pass = ?";
        try (Connection con = openUsers(); PreparedStatement ps = con.prepareStatement(SQL);) {
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    arr[0] = true;
                    arr[1] = new User(rs.getString("user_id"), rs.getString("user_name"), rs.getString("dept"),
                            rs.getString("dept_lvl"), rs.getDate("last_visit").toLocalDate());
                }
                LOGR.info(rs.getString("user_name") + " successfully logged in");
                rs.close();
                ps.close();
                con.close();
            } else {
                LOGR.info("User login was unsuccessfully");
            }
        } catch (SQLException e) {
            LOGR.fatal("Attempt to query database failed /n" + e.getSQLState());
            return arr;
        }
        return arr;
    }

    public static final Boolean setLastVisit(User user) {
        String SQL = "UPDATE dbo.user SET 'last_visit' = ?  WHERE user_id = ?";
        try (Connection con = openCon(); PreparedStatement ps = con.prepareStatement(SQL);) {
            ps.setString(1, user.getEmpname());
            ps.setDate(2, java.sql.Date.valueOf(LocalDate.now()));
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
             LOGR.fatal("Attempt to query database failed /n" + e.getSQLState());
            return false;
        }
    }
}
