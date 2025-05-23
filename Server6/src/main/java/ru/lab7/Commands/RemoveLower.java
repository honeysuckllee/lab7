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
import java.util.List;

import static ru.lab7.Service.Utilites.getValidInt;
import static ru.lab7.Service.Utilites.integerConverter;

public class RemoveLower extends Command {
    private Integer id;

    public RemoveLower(RouteCollection routeCollection, DBUsersHandler usersHandler, DBRouteHandler routeHandler) {
        super(routeCollection, usersHandler, routeHandler);
    }

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
            List<Integer> removeIds = RouteCollection.idToRemove(id);
            for (Integer removeId : removeIds){
                if (routeHandler.isRouteOwnedByUser(removeId, userId)) {
                    routeHandler.remove(removeId);
                    collection.removeRoute(removeId);
                }
            }
        }
        catch (SQLException e){
            return new Response( "Ошибка при удалении\n, true");
        }
        return new Response("Элементы успешно удалены ", true);
    }
}
