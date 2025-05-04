package ru.lab7.DataBase;

import java.sql.Connection;
        import java.sql.PreparedStatement;
        import java.sql.ResultSet;
        import java.sql.SQLException;

public class IDCreator {

    private final Connection connection;

    public IDCreator(Connection connection) {
        this.connection = connection;

    }

    /**
     * Метод для получения текущего значения из последовательности.
     * Требуется, чтобы сначала был вызван nextval.
     *
     * @return текущее значение последовательности
     * @throws SQLException
     */
    public long getCurrentIdFromSequence() throws SQLException {
        String sql = "SELECT MAX(id) FROM route";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getLong(1);  // Возвращаем максимальный id
            } else {
                throw new SQLException("Не удалось получить последний id из таблицы route");
            }
        }
    }

}