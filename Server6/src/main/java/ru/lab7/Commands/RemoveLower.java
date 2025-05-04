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

import static ru.lab7.Service.Utilites.getValidInt;
import static ru.lab7.Service.Utilites.integerConverter;

public class RemoveLower extends Command {
    private Deque deque;
    private Integer id;

    public RemoveLower(RouteCollection routeCollection, DBUsersHandler usersHandler, DBRouteHandler routeHandler) {
        super(routeCollection, usersHandler, routeHandler);
    }

    @Override
    public Response execute(Request request, RequestReader requestReader, ResponseWriter responseWriter) throws IOException, ClassNotFoundException {
        if (request.getArg().isEmpty()){
            id = getValidInt( request.isScript(),  requestReader, responseWriter, "Введите id:");
        }
        else {
            id = integerConverter(request.getArg());
        }
        if (id != null)
        {
            if (!deque.getDeque().isEmpty()) {
                int counterDell = deque.removeLower(id);
                return new Response("Удалено " + counterDell + " элементов" + "\n", true);
            }
            return new Response("Коллекция пуста" + "\n", true);
        }
        else
        {
            return new Response("Неверный формат команды \n", true);
        }
    }
}
