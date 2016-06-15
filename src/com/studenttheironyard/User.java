package com.studenttheironyard;

/**
 * Created by hoseasandstrom on 6/15/16.
 */

public class User {
    Integer id;
    String address;
    String username;
    String email;

    public User() {
    }

    public User(Integer id, String address, String username, String email) {
        this.id = id;
        this.address = address;
        this.username = username;
        this.email = email;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}