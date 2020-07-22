/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package basic;

import java.time.LocalDate;
import java.time.ZoneId;

public class User {

    private String user_id, empname, dept, rank;
    private LocalDate last_login;

    //This creates a user based on existing data stored in database
    public User(String user_id, String empname, String dept, String rank, LocalDate last_login) {
        this.user_id = user_id;
        this.empname = empname;
        this.dept = dept;
        this.rank = rank;
        this.last_login = last_login;
    }

    //This creates a new user with a new Last Login
    public User(String user_id, String empname, String dept, String rank) {
        this.user_id = user_id;
        this.empname = empname;
        this.dept = dept;
        this.rank = rank;
        setFirstLogin();
    }

    //Null user 
    public User() {
        this.user_id = null;
        this.empname = null;
        this.dept = null;
        this.rank = null;
        this.last_login = null;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getEmpname() {
        return empname;
    }

    public String getDept() {
        return dept;
    }

    public String getRank() {
        return rank;
    }

    public LocalDate getLastLogin() {
        return last_login;
    }

    public void setLastLogin(LocalDate date) {
        this.last_login = date;
    }

    public final void setFirstLogin() {
        this.last_login = LocalDate.now(ZoneId.of("America/Port_of_Spain"));
    }

}
