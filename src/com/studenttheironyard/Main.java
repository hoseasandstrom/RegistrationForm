package com.studenttheironyard;

import jodd.json.JsonParser;
import jodd.json.JsonSerializer;
import org.h2.tools.Server;
import spark.Spark;

import java.sql.*;
import java.util.ArrayList;

public class Main {

    public static void createTables(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.execute("CREATE TABLE IF NOT EXISTS messages (id IDENTITY, username VARCHAR, address VARCHAR, email VARCHAR)");
    }

    public static void insertUser(Connection conn, User reg) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO registrations VALUES(NULL, ?, ?, ?)");
        stmt.setString(1, reg.username);
        stmt.setString(2, reg.address);
        stmt.setString(3, reg.email);
        stmt.execute();
    }

    public static ArrayList<User> selectUsers(Connection conn) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM registrations");
        ResultSet results = stmt.executeQuery();
        ArrayList<User> regs = new ArrayList<>();
        while (results.next()) {
            Integer id = results.getInt("id");
            String username = results.getString("username");
            String address = results.getString("address");
            String email = results.getString("email");
            User reg = new User(id, username, address, email);
            regs.add(reg);
        }
        return regs;
    }

    public static void editUser(Connection conn) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("UPDATE ")
    }

    public static void deleteUser(Connection conn) {
        PreparedStatement stmt = conn.prepareStatement("DELETE ")
    }

    public static void main(String[] args) throws SQLException {
        Server.createWebServer().start();
        Connection conn = DriverManager.getConnection("jdbc:h2:./main");
        createTables(conn);

        Spark.externalStaticFileLocation("public"); //very important!!!!
        Spark.init();

        Spark.get(
                "/get-user",
                (request, response) -> {
                    ArrayList<User> regs = selectUsers(conn);
                    JsonSerializer s = new JsonSerializer();
                    return s.serialize(regs);
                }
        );
        Spark.post(
                "/add-user",
                (request, response) -> {
                    String body = request.body();
                    JsonParser p = new JsonParser();
                    User reg = p.parse(body, User.class);
                    insertUser(conn, reg);
                    return "";
                }
        );
        Spark.put(
                "/edit-user",
                (request, response) -> {
                    //update message in database
                    return "";
                }
        );
        Spark.delete(
                "/delete-user",
                (request, response) -> {
                    //delete message in database
                    return "";
                }

        );
    }
}
