package ru.lab7.DataBase;

import ru.lab7.Model.Coordinates;
import ru.lab7.Model.Location;
import ru.lab7.Model.Route;

import java.sql.Connection;
import java.util.HashSet;
import java.sql.*;

public class DBRouteHandler {
    private Connection connection;
    public DBRouteHandler(Connection connection) {
        this.connection = connection;
    }
    public void add(Route route) throws SQLException {
        // Вставка координат
        String insertCoordinatesSQL = "INSERT INTO coordinates (x, y) VALUES (?, ?) RETURNING id";
        long coordinatesId;
        try (PreparedStatement ps = connection.prepareStatement(insertCoordinatesSQL)) {
            ps.setDouble(1, route.getCoordinates().getX());
            ps.setFloat(2, route.getCoordinates().getY());  // Используем setFloat для y
            ResultSet rs = ps.executeQuery();
            rs.next();
            coordinatesId = rs.getLong(1);
        }

        // Вставка начальной локации (from)
        String insertLocationSQL = "INSERT INTO locations (x, y, z) VALUES (?, ?, ?) RETURNING id";
        Long fromId = null;
        if (route.getFrom() != null) {
            try (PreparedStatement ps = connection.prepareStatement(insertLocationSQL)) {
                ps.setInt(1, route.getFrom().getX());
                ps.setFloat(2, route.getFrom().getY());  // Используем setFloat для y
                ps.setInt(3, route.getFrom().getZ());
                ResultSet rs = ps.executeQuery();
                rs.next();
                fromId = rs.getLong(1);
            }
        }

        // Вставка конечной локации (to)
        Long toId = null;
        if (route.getTo() != null) {
            try (PreparedStatement ps = connection.prepareStatement(insertLocationSQL)) {
                ps.setInt(1, route.getTo().getX());
                ps.setFloat(2, route.getTo().getY());  // Используем setFloat для y
                ps.setInt(3, route.getTo().getZ());
                ResultSet rs = ps.executeQuery();
                rs.next();
                toId = rs.getLong(1);
            }
        }

        // Вставка маршрута
        String insertRouteSQL = """
        INSERT INTO routes (name, coordinates_id, creation_date, from_id, to_id, distance)
        VALUES (?, ?, ?, ?, ?, ?)
        """;
        try (PreparedStatement ps = connection.prepareStatement(insertRouteSQL)) {
            ps.setString(1, route.getName());
            ps.setLong(2, coordinatesId);
            ps.setDate(3, Date.valueOf(route.getCreationDate()));
            ps.setObject(4, fromId);
            ps.setObject(5, toId);
            ps.setObject(6, route.getDistance());
            ps.executeUpdate();
        }
    }
    public void remove(int routeId) throws SQLException {
        connection.setAutoCommit(false); // Начинаем транзакцию

        try (
                PreparedStatement getRefsStmt = connection.prepareStatement("""
            SELECT coordinates_id, from_id, to_id
            FROM routes
            WHERE id = ?
        """);

                PreparedStatement deleteRouteStmt = connection.prepareStatement("""
            DELETE FROM routes WHERE id = ?
        """);

                PreparedStatement deleteCoordinatesStmt = connection.prepareStatement("""
            DELETE FROM coordinates WHERE id = ?
        """);

                PreparedStatement deleteLocationStmt = connection.prepareStatement("""
            DELETE FROM locations WHERE id = ?
        """)
        ) {
            Integer coordinatesId = null;
            Integer fromId = null;
            Integer toId = null;

            // Получаем ссылки на связанные объекты
            getRefsStmt.setInt(1, routeId);
            try (ResultSet rs = getRefsStmt.executeQuery()) {
                if (rs.next()) {
                    coordinatesId = rs.getInt("coordinates_id");
                    fromId = rs.getInt("from_id");
                    toId = rs.getInt("to_id");
                    if (rs.wasNull()) {  // Обработка NULL значений
                        fromId = null;
                        toId = null;
                    }
                }
            }

            // Удаляем маршрут
            deleteRouteStmt.setInt(1, routeId);
            deleteRouteStmt.executeUpdate();

            // Удаляем координаты
            if (coordinatesId != null) {
                deleteCoordinatesStmt.setInt(1, coordinatesId);
                deleteCoordinatesStmt.executeUpdate();
            }

            // Удаляем локации (если есть)
            if (fromId != null) {
                deleteLocationStmt.setInt(1, fromId);
                deleteLocationStmt.executeUpdate();
            }

            if (toId != null && !toId.equals(fromId)) { // Проверяем, чтобы не удалить дважды одну локацию
                deleteLocationStmt.setInt(1, toId);
                deleteLocationStmt.executeUpdate();
            }

            connection.commit(); // Фиксируем транзакцию
        } catch (SQLException e) {
            connection.rollback(); // Откатываем в случае ошибки
            throw e;
        } finally {
            connection.setAutoCommit(true); // Возвращаем авто-коммит
        }
    }
    public void update(int id, Route route) throws SQLException {
        // Запросы для обновления
        String updateRouteQuery = """
        UPDATE routes
        SET name = ?, distance = ?
        WHERE id = ?;
        """;

        String updateCoordinatesQuery = """
        UPDATE coordinates
        SET x = ?, y = ?
        WHERE id = (
            SELECT coordinates_id FROM routes WHERE id = ?
        );
        """;

        String updateFromLocationQuery = """
        UPDATE locations
        SET x = ?, y = ?, z = ?
        WHERE id = (
            SELECT from_id FROM routes WHERE id = ?
        );
        """;

        String updateToLocationQuery = """
        UPDATE locations
        SET x = ?, y = ?, z = ?
        WHERE id = (
            SELECT to_id FROM routes WHERE id = ?
        );
        """;

        String insertFromLocationQuery = """
        INSERT INTO locations (x, y, z)
        VALUES (?, ?, ?)
        RETURNING id;
        """;

        String insertToLocationQuery = """
        INSERT INTO locations (x, y, z)
        VALUES (?, ?, ?)
        RETURNING id;
        """;

        String setFromLocationQuery = """
        UPDATE routes SET from_id = ? WHERE id = ?;
        """;

        String setToLocationQuery = """
        UPDATE routes SET to_id = ? WHERE id = ?;
        """;

        connection.setAutoCommit(false); // Начинаем транзакцию

        try (
                PreparedStatement routeStmt = connection.prepareStatement(updateRouteQuery);
                PreparedStatement coordStmt = connection.prepareStatement(updateCoordinatesQuery);
                PreparedStatement updateFromStmt = connection.prepareStatement(updateFromLocationQuery);
                PreparedStatement updateToStmt = connection.prepareStatement(updateToLocationQuery);
                PreparedStatement insertFromStmt = connection.prepareStatement(insertFromLocationQuery);
                PreparedStatement insertToStmt = connection.prepareStatement(insertToLocationQuery);
                PreparedStatement setFromStmt = connection.prepareStatement(setFromLocationQuery);
                PreparedStatement setToStmt = connection.prepareStatement(setToLocationQuery)
        ) {
            // Обновляем основную информацию о маршруте
            routeStmt.setString(1, route.getName());
            if (route.getDistance() != null) {
                routeStmt.setFloat(2, route.getDistance());
            } else {
                routeStmt.setNull(2, java.sql.Types.FLOAT);
            }
            routeStmt.setInt(3, id);
            routeStmt.executeUpdate();

            // Обновляем координаты
            coordStmt.setDouble(1, route.getCoordinates().getX());
            coordStmt.setFloat(2, route.getCoordinates().getY());
            coordStmt.setInt(3, id);
            coordStmt.executeUpdate();

            // Обрабатываем from location
            if (route.getFrom() != null) {
                // Проверяем, есть ли уже локация
                boolean hasFromLocation = false;
                try (PreparedStatement checkStmt = connection.prepareStatement(
                        "SELECT from_id FROM routes WHERE id = ?")) {
                    checkStmt.setInt(1, id);
                    try (ResultSet rs = checkStmt.executeQuery()) {
                        if (rs.next()) {
                            hasFromLocation = !rs.wasNull();
                        }
                    }
                }

                if (hasFromLocation) {
                    // Обновляем существующую локацию
                    updateFromStmt.setInt(1, route.getFrom().getX());
                    updateFromStmt.setFloat(2, route.getFrom().getY());
                    updateFromStmt.setInt(3, route.getFrom().getZ());
                    updateFromStmt.setInt(4, id);
                    updateFromStmt.executeUpdate();
                } else {
                    // Создаем новую локацию
                    insertFromStmt.setInt(1, route.getFrom().getX());
                    insertFromStmt.setFloat(2, route.getFrom().getY());
                    insertFromStmt.setInt(3, route.getFrom().getZ());
                    try (ResultSet rs = insertFromStmt.executeQuery()) {
                        if (rs.next()) {
                            int newFromId = rs.getInt(1);
                            setFromStmt.setInt(1, newFromId);
                            setFromStmt.setInt(2, id);
                            setFromStmt.executeUpdate();
                        }
                    }
                }
            } else {
                // Удаляем ссылку на from location
                setFromStmt.setNull(1, java.sql.Types.INTEGER);
                setFromStmt.setInt(2, id);
                setFromStmt.executeUpdate();
            }

            // Обрабатываем to location (аналогично from)
            if (route.getTo() != null) {
                boolean hasToLocation = false;
                try (PreparedStatement checkStmt = connection.prepareStatement(
                        "SELECT to_id FROM routes WHERE id = ?")) {
                    checkStmt.setInt(1, id);
                    try (ResultSet rs = checkStmt.executeQuery()) {
                        if (rs.next()) {
                            hasToLocation = !rs.wasNull();
                        }
                    }
                }

                if (hasToLocation) {
                    updateToStmt.setInt(1, route.getTo().getX());
                    updateToStmt.setFloat(2, route.getTo().getY());
                    updateToStmt.setInt(3, route.getTo().getZ());
                    updateToStmt.setInt(4, id);
                    updateToStmt.executeUpdate();
                } else {
                    insertToStmt.setInt(1, route.getTo().getX());
                    insertToStmt.setFloat(2, route.getTo().getY());
                    insertToStmt.setInt(3, route.getTo().getZ());
                    try (ResultSet rs = insertToStmt.executeQuery()) {
                        if (rs.next()) {
                            int newToId = rs.getInt(1);
                            setToStmt.setInt(1, newToId);
                            setToStmt.setInt(2, id);
                            setToStmt.executeUpdate();
                        }
                    }
                }
            } else {
                setToStmt.setNull(1, java.sql.Types.INTEGER);
                setToStmt.setInt(2, id);
                setToStmt.executeUpdate();
            }

            connection.commit(); // Фиксируем транзакцию
        } catch (SQLException e) {
            connection.rollback(); // Откатываем в случае ошибки
            throw e;
        } finally {
            connection.setAutoCommit(true); // Возвращаем авто-коммит
        }
    }
    public HashSet<Route> load() throws SQLException {
        HashSet<Route> routes = new HashSet<>();
        String query = """
        SELECT r.id, r.name, r.creation_date, r.distance,
               c.x AS coord_x, c.y AS coord_y,
               f.x AS from_x, f.y AS from_y, f.z AS from_z,
               t.x AS to_x, t.y AS to_y, t.z AS to_z
        FROM routes r
        JOIN coordinates c ON r.coordinates_id = c.id
        LEFT JOIN locations f ON r.from_id = f.id
        LEFT JOIN locations t ON r.to_id = t.id
        """;

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                // Создаем объект Coordinates
                Coordinates coordinates = new Coordinates(
                        rs.getDouble("coord_x"),
                        rs.getFloat("coord_y")  // Используем getFloat для y
                );

                // Создаем объект Location для from (может быть null)
                Location from = null;
                if (rs.getObject("from_x") != null) {
                    from = new Location(
                            rs.getInt("from_x"),
                            rs.getFloat("from_y"),  // Используем getFloat для y
                            rs.getInt("from_z")
                    );
                }

                // Создаем объект Location для to (может быть null)
                Location to = null;
                if (rs.getObject("to_x") != null) {
                    to = new Location(
                            rs.getInt("to_x"),
                            rs.getFloat("to_y"),  // Используем getFloat для y
                            rs.getInt("to_z")
                    );
                }

                // Создаем объект Route
                Route route = new Route(
                        rs.getInt("id"),
                        rs.getString("name"),
                        coordinates,
                        rs.getDate("creation_date").toLocalDate(),
                        from,
                        to,
                        (Float) rs.getObject("distance")  // может быть null
                );

                routes.add(route);
            }
        }

        return routes;
    }
}
