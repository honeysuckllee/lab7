package ru.lab7.DataBase;

import java.sql.*;
import java.util.concurrent.ConcurrentHashMap;

import lombok.Getter;
import ru.lab7.DataBase.Users.PasswordUtilites;

@Getter
public class DBUsersHandler{
    private Connection connection;

    public DBUsersHandler(String url, String user, String password) {
        try {
            connection = DriverManager.getConnection(url, user, password);
            if (connection != null) {
                System.out.println("Соединение с базой данных установлено.");
            }
        } catch (SQLException e) {
            System.out.println("Ошибка при подключении к базе данных: " + e.getMessage());
        }
    }


    public boolean registerUser(String username, String password) throws SQLException {
        String hash = PasswordUtilites.hash(password);
        String query = "INSERT INTO users (username, password) VALUES (?, ?) ON CONFLICT DO NOTHING";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, username);
            ps.setString(2, hash);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean authenticate(String username, String password) throws SQLException {
        String query = "SELECT password FROM users WHERE username = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String storedHash = rs.getString("password");
                return PasswordUtilites.hash(password).equals(storedHash);
            }
        }
        return false;
    }


    public ConcurrentHashMap<String, String> loadAllUsers() throws SQLException{
        ConcurrentHashMap<String, String> usersFromBD = new ConcurrentHashMap<String, String>();
        String query =
                "Select username AS name, password AS password"
                        + " FROM users";
        try(Statement stmt = connection.createStatement()){
            ResultSet rs = stmt.executeQuery(query);
            while(rs.next()) {
                usersFromBD.put(rs.getString("name"), rs.getString("password"));
            }
        }

        return usersFromBD;

    }
}
