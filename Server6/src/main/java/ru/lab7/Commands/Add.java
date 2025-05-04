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

/**
 *
 * Класс `Add` реализует команду добавления нового элемента `Route` в коллекцию `Deque`.
 */
public class Add extends Command {
    /**
     * Имя маршрута. Поле не может быть null, строка не может быть пустой.
     */
    private String name;
    /**
     * Координаты маршрута. Поле не может быть null.
     */
    private Coordinates coordinates;
    /**
     * Дата создания маршрута. Поле не может быть null, значение этого поля должно генерироваться автоматически.
     */
    private LocalDate creationDate;
    /**
     * Начальная локация маршрута. Поле может быть null.
     */
    private Location from;
    /**
     * Конечная локация маршрута. Поле может быть null.
     */
    private Location to;
    /**
     * Дистанция маршрута.
     */
    private Float distance;
    /**
     * Коллекция `Deque`, в которую добавляется маршрут.
     */
    private Deque deque;

    /**
     * Конструктор класса `Add`.
     *
     */
    public Add(RouteCollection routeCollection, DBUsersHandler usersHandler, DBRouteHandler routeHandler){
        super(routeCollection, usersHandler, routeHandler);
    }

    /**
     * Выполняет команду добавления нового элемента `Route` в коллекцию.
     * Запрашивает у пользователя данные для создания объекта `Route` и добавляет его в коллекцию `Deque`.
     */

    @Override
    public Response execute(Request request, RequestReader requestReader, ResponseWriter responseWriter) throws IOException, ClassNotFoundException {
        // id
        //int id = route.getId();

        //  name

        if (!request.getArg().isEmpty()){
            this.name = request.getArg();
        }else{
            this.name = getValidName(request.isScript(), requestReader, responseWriter);
        }

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

        deque.addRoute(name, coordinates, creationDate, from, to, distance);

        return new Response("Маршрут добавлен" + "\n", true);

    }
}
