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
import java.sql.SQLException;

/**
 * Класс `RemoveFirst` реализует интерфейс `Command` и представляет команду удаления первого элемента из коллекции.
 * При выполнении команды удаляется первый элемент коллекции.
 */
public class RemoveFirst extends Command {

    /**
     * Конструктор класса `RemoveFirst`.
     *
     */
    public RemoveFirst(RouteCollection routeCollection, DBUsersHandler usersHandler, DBRouteHandler routeHandler) {
        super(routeCollection, usersHandler, routeHandler);
    }

    /**
     * Метод `execute` удаляет первый элемент из коллекции.
     */

    @Override
    public Response execute(Request request, RequestReader requestReader, ResponseWriter responseWriter) throws IOException, ClassNotFoundException, SQLException {
        try {
            routeHandler.remove(collection.getMinId());
            collection.removeRoute(collection.getMinId());
            return new Response("Успешно удален первый элемент", true);
        }
        catch(SQLException e){
            return new Response( "Ошибка при удалении\n");
        }
    }
}