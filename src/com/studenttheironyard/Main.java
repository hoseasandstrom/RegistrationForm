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
        stmt.execute("CREATE TABLE IF NOT EXISTS registrations (id IDENTITY, username VARCHAR, address VARCHAR, email VARCHAR)");
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
            int id = results.getInt("id");
            String username = results.getString("username");
            String address = results.getString("address");
            String email = results.getString("email");
            User reg = new User(id, username, address, email);
            regs.add(reg);
        }
        return regs;
    }

    public static void updateUser(Connection conn, User reg) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("UPDATE registrations SET username=?, address=?, email=? WHERE id=?");
        stmt.setString(1, reg.username);
        stmt.setString(2, reg.address);
        stmt.setString(3, reg.email);
        stmt.setInt(4, reg.id);
        stmt.execute();
    }

    public static void deleteUser(Connection conn, int id) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM registrations WHERE id = ?");
        stmt.setInt(1, id);
        stmt.execute();
    }

    public static void main(String[] args) throws SQLException {
        Server.createWebServer().start();
        Connection conn = DriverManager.getConnection("jdbc:h2:./main");
        createTables(conn);

        Spark.externalStaticFileLocation("public"); //very important!!!!
        Spark.init();

        Spark.get(
                "/user",
                (request, response) -> {
                    ArrayList<User> regs = selectUsers(conn);
                    JsonSerializer s = new JsonSerializer();
                    return s.serialize(regs);
                }
        );

        Spark.post(
                "/user",
                (request, response) -> {
                        String body = request.body();
                        JsonParser p = new JsonParser();
                        User reg = p.parse(body, User.class);
                        insertUser(conn, reg);
                        return "";
                }
        );

        Spark.put(
                "/user",
                (request, response) -> { //update user in database
                    String body = request.body();
                    JsonParser p = new JsonParser();
                    User reg = p.parse(body, User.class);
                    updateUser(conn, reg);
                    return "";
                }
        );

        Spark.delete(
                "/user/:id",
                (request, response) -> { //delete user in database
                    int id = Integer.valueOf(request.params("id"));
                    deleteUser(conn, id);
                    return"";
                }
        );


    }
}
