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

import static ru.lab7.Service.Utilites.*;

/**
 * Класс `FilterStartsWithName` реализует интерфейс `Command` и представляет команду фильтрации элементов коллекции,
 * которые начинаются с указанного имени.
 */
public class FilterStartsWithName extends Command {
    /**
     * Коллекция `Deque`, с которой работает команда.
     */
    private Deque deque;

    /**
     * Имя, с которого должны начинаться элементы коллекции.
     */
    private String name;

    /**
     * Конструктор класса `FilterStartsWithName`.
     *
     */
    public FilterStartsWithName(RouteCollection routeCollection, DBUsersHandler usersHandler, DBRouteHandler routeHandler) {
        super(routeCollection, usersHandler, routeHandler);
    }

    /**
     * Метод `execute` выполняет команду фильтрации элементов коллекции, которые начинаются с указанного имени.
     * Если имя не было передано, запрашивает его у пользователя.
     */

    @Override
    public Response execute(Request request, ObjectInputStream requestReader, ResponseWriter responseWriter)
                            throws IOException, ClassNotFoundException
    {
        if (request.getArg().isEmpty()){
            name = getValidName( request.isScript(),  requestReader, responseWriter);
        }
        else {
            name = request.getArg();
        }
        if (!name.isEmpty())
        {
            return new Response(collection.filterStartsWithName(name), true);
        }
        return new Response("Неверный формат команды \n", true);
    }
}