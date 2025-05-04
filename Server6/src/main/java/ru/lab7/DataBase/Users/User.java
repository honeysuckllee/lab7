package ru.lab7.DataBase.Users;

import java.io.Serializable;

public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String username;
    private final String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }

}