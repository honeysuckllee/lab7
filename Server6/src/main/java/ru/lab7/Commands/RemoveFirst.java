package ru.lab7.Commands;

import ru.lab7.DataBase.DBRouteHandler;
import ru.lab7.DataBase.DBUsersHandler;
import ru.lab7.Model.RouteCollection;
import ru.lab7.Requests.Request;
import ru.lab7.Response;
import ru.lab7.ResponseWriter;

import java.io.IOException;
import java.io.ObjectInputStream;
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
    public Response execute(Request request, ObjectInputStream requestReader, ResponseWriter responseWriter) throws IOException, ClassNotFoundException, SQLException {
        try {
            int userId = usersHandler.getUserId(request.getLogin(), request.getPassword());
            int id  = collection.getMinId();
            if (routeHandler.isRouteOwnedByUser(id, userId)) {
                routeHandler.remove(id);
                collection.removeRoute(id);
                return new Response("Успешно удален первый элемент", true);
            }
            else {
                return new Response("Ошибка при удалении. Элемент не принадлежит пользователю.", true);
            }
        }
        catch(SQLException e){
            return new Response( "Ошибка при удалении\n", true);
        }
    }
}