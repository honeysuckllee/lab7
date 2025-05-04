package ru.lab7.DataBase.Users;

import java.util.concurrent.ConcurrentHashMap;

public class UserManager {

    public ConcurrentHashMap<String, String> userMap = new ConcurrentHashMap<>();

    public UserManager(ConcurrentHashMap<String, String> users) {
        this.userMap = users;
    }

    public boolean register(String username, String stringPassword) {
        String hash = PasswordUtilites.hash(stringPassword);
        return (userMap.putIfAbsent(username, hash) == null);
    }

    public boolean authenticate(String username, String stringPassword) {
        String hash = PasswordUtilites.hash(stringPassword);
        return hash.equals(userMap.get(username));
    }
}

