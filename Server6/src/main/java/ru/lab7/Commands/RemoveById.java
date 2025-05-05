package ru.lab7.Commands;

import ru.lab7.DataBase.DBRouteHandler;
import ru.lab7.DataBase.DBUsersHandler;
import ru.lab7.Model.Deque;
import ru.lab7.Model.RouteCollection;
import ru.lab7.Requests.Request;
import ru.lab7.Response;
import ru.lab7.ResponseWriter;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.SQLException;

import static ru.lab7.Service.Utilites.getValidInt;
import static ru.lab7.Service.Utilites.integerConverter;

/**
 * Класс `RemoveById` реализует интерфейс `Command` и представляет команду удаления элемента из коллекции по его идентификатору.
 * Если идентификатор не был передан, он запрашивается у пользователя.
 */
public class RemoveById extends Command {
    /**
     * Коллекция `Deque`, из которой удаляется элемент.
     */
    private Deque deque;

    /**
     * Идентификатор элемента, который необходимо удалить.
     */
    private Integer id;

    /**
     * Конструктор класса `RemoveById`.
     *
     */
    public RemoveById(RouteCollection routeCollection, DBUsersHandler usersHandler, DBRouteHandler routeHandler) {
        super(routeCollection, usersHandler, routeHandler);
    }

    /**
     * Метод `execute` удаляет элемент из коллекции по его идентификатору.
     * Если идентификатор не был передан, он запрашивается у пользователя.
     */

    @Override
    public Response execute(Request request, ObjectInputStream requestReader, ResponseWriter responseWriter) throws IOException, ClassNotFoundException {
        if (request.getArg().isEmpty()){
            id = getValidInt( request.isScript(),  requestReader, responseWriter, "Введите id:");
        }
        else {
            id = integerConverter(request.getArg());
        }


        try {
            int userId = usersHandler.getUserId(request.getLogin(), request.getPassword());
            if (routeHandler.isRouteOwnedByUser(id, userId)) {
                routeHandler.remove(id);
                collection.removeRoute(id);
                return new Response("Элемент успешно удален ", true);
            } else {
                return new Response("Ошибка при удалении. Элемент не принадлежит пользователю.", true);
            }
        }
        catch(SQLException e){
            return new Response( "Ошибка при удалении\n", true);
        }
    }
}