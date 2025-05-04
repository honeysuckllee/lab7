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
 * Команда для печати элементов коллекции в порядке убывания расстояния до указанного поля.
 */
public class PrintFieldDescendingDistance extends Command {
    /**
     * Конструктор команды.
     *
     */
    public PrintFieldDescendingDistance(RouteCollection routeCollection, DBUsersHandler usersHandler, DBRouteHandler routeHandler) {
        super(routeCollection, usersHandler, routeHandler);
    }

    /**
     * Выполняет команду печати элементов коллекции  в порядке убывания.
     */

    @Override
    public Response execute(Request request, RequestReader requestReader, ResponseWriter responseWriter)
                            throws IOException, ClassNotFoundException {
        return new Response(collection.printFieldDescendingDistance() + "\n", true);
    }
}