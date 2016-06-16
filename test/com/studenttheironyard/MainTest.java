package com.studenttheironyard;

import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by hoseasandstrom on 6/15/16.
 */
public class MainTest {
    public Connection startConnection() throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:h2:mem:test");
        Main.createTables(conn);
        return conn;
    }
    @Test
    public void testUsers() throws SQLException {
        Connection conn = startConnection();
        User testUser = new User(1, "Alice", "100 King St", "alice@gmail.com" );
        Main.insertUser(conn, testUser);
        ArrayList<User> userArrayList = Main.selectUsers(conn);
        conn.close();
        assertTrue(userArrayList.size() == 1);
    }

    public void testUpdateUser() throws SQLException {
        Connection conn = startConnection();
        User testUser = new User(1, "Alice", "100 King St", "alice@gmail.com" );
        Main.insertUser(conn, testUser);
        Main.updateUser(conn, testUser);
        ArrayList<User> testArray = Main.selectUsers(conn);
        testUser = testArray.get(0);
        conn.close();
        assertTrue(testUser.address.equals("100 King St"));
    }

    public void testDeleteUser() throws SQLException {
        Connection conn = startConnection();
        User testUser = new User(1, "Alice", "100 King St", "alice@gmail.com" );
        Main.insertUser(conn, testUser);
        Main.deleteUser(conn, 1);
        ArrayList<User> testArray = Main.selectUsers(conn);
        conn.close();
        assertTrue(testArray.size() == 1);

    }


}