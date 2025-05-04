package ru.lab7.Commands;

import ru.lab7.Model.RouteCollection;
import ru.lab7.Requests.Request;
import ru.lab7.Requests.RequestReader;
import ru.lab7.Response;
import ru.lab7.ResponseWriter;
import ru.lab7.DataBase.*;

import java.io.IOException;

public abstract class Command {
    RouteCollection collection;
    DBRouteHandler routeHandler;
    DBUsersHandler usersHandler;

    public Command(RouteCollection routeCollection, DBUsersHandler usersHandler, DBRouteHandler routeHandler) {
        collection = routeCollection;
        this.usersHandler = usersHandler;
        this.routeHandler = routeHandler;
    }

    public abstract Response execute(Request request, RequestReader requestReader,
                                     ResponseWriter responseWriter) throws IOException, ClassNotFoundException;

}