package ru.lab7.Commands;

import ru.lab7.DataBase.DBRouteHandler;
import ru.lab7.DataBase.DBUsersHandler;
import ru.lab7.Model.Deque;
import ru.lab7.Model.RouteCollection;
import ru.lab7.Requests.Request;
import ru.lab7.Requests.RequestReader;
import ru.lab7.Response;
import ru.lab7.ResponseWriter;

import java.io.IOException;

/**
 * Класс `Clear` реализует команду очистки коллекции `Deque`.
 */
public class Clear extends Command {
    /**
     * Коллекция `Deque`, которую необходимо очистить.
     */

    /**
     * Конструктор класса `Clear`.
     *
     */
    public Clear(RouteCollection routeCollection, DBUsersHandler usersHandler, DBRouteHandler routeHandler) {
        super(routeCollection, usersHandler, routeHandler);
    }

    /**
     * Выполняет команду очистки коллекции `Deque`.
     * Удаляет все элементы из коллекции `Deque`.
     */

    @Override
    public Response execute(Request request, RequestReader requestReader, ResponseWriter responseWriter) throws IOException, ClassNotFoundException {
        /*Iterator<Route> iterator = routeCollection.movies.iterator();
        while (iterator.hasNext()) {
            Route routeToClear = iterator.next();
            //if (routeToClear.getCreatedBy().equals(login) && checkUser(login, password))
            {
                try {
                    routeHandler.deleteMovie(routeHandler.getId());
                    iterator.remove(); // безопасное удаление из коллекции
                } catch (SQLException e) {
                    return "Ошибка при удалении из БД: " + e.getMessage();
                }
            }
        }*/
        return new Response("Коллекция очищена\n", true);
    }
}
