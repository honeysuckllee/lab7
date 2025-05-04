package ru.lab7.Commands;

import ru.lab7.DataBase.DBRouteHandler;
import ru.lab7.DataBase.DBUsersHandler;
import ru.lab7.Model.Deque;
import ru.lab7.Model.Route;
import ru.lab7.Model.RouteCollection;
import ru.lab7.Requests.Request;
import ru.lab7.Requests.RequestReader;
import ru.lab7.Response;
import ru.lab7.ResponseWriter;

public class Show extends Command {

    public Show(RouteCollection routeCollection, DBUsersHandler usersHandler, DBRouteHandler routeHandler) {
        super(routeCollection, usersHandler, routeHandler);
    }

    @Override
    public Response execute(Request request, RequestReader requestReader, ResponseWriter responseWriter) {
        StringBuilder rez = new StringBuilder("Коллекция:\n");
        if (collection.getLength() > 0) {
            for (Route route : collection.sortedRoute()) {
                rez.append(route.toString()).append("\n");
            }
        }
        else {
            rez.append("пуста").append("\n");
        }
        return new Response(rez.toString(), true);
    }
}
