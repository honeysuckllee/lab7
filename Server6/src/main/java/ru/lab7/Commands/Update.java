package ru.lab7.Commands;

import ru.lab7.DataBase.DBRouteHandler;
import ru.lab7.DataBase.DBUsersHandler;
import ru.lab7.Model.Coordinates;
import ru.lab7.Model.Deque;
import ru.lab7.Model.Location;
import ru.lab7.Model.RouteCollection;
import ru.lab7.Requests.Request;
import ru.lab7.Requests.RequestReader;
import ru.lab7.Response;
import ru.lab7.ResponseWriter;

import java.io.IOException;
import java.time.LocalDate;

import static ru.lab7.Service.Utilites.*;

public class Update extends Command {
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private LocalDate creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private Location from; //Поле может быть null
    private Location to; //Поле может быть null
    private Float distance;
    private Deque deque;
    private Integer id;

    public Update(RouteCollection routeCollection, DBUsersHandler usersHandler, DBRouteHandler routeHandler) {
        super(routeCollection, usersHandler, routeHandler);
    }

    public Response execute(Request request, RequestReader requestReader, ResponseWriter responseWriter) throws IOException, ClassNotFoundException {
        // id
        id = null;
        if (!request.getArg().isEmpty()){
            id = integerConverter(request.getArg());
        }
        if (id == null)
        {
            id = getValidInt(request.isScript(), requestReader, responseWriter, "Введите id : ");
        }

        //  name
        this.name = getValidName(request.isScript(), requestReader, responseWriter);

        //  coordinates
        this.coordinates = getValidCoordinates(request.isScript(), requestReader, responseWriter);

        //  creationDate
        this.creationDate = LocalDate.now();

        //  from
        this.from = getValidLocation(request.isScript(), requestReader, responseWriter, "Введите значение to: \n");

        //  to
        this.to = getValidLocation(request.isScript(), requestReader, responseWriter, "Введите значение from: \n");

        //  distance
        this.distance = getValidFloatDistance(request.isScript(), requestReader, responseWriter);

        deque.updateRoute(id, name, coordinates, creationDate, from, to, distance);

        return new Response("Маршрут обновлен" + "\n", true);

    }
}