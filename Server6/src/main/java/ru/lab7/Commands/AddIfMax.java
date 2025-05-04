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
 * Класс `AddIfMax` реализует команду добавления нового элемента `Route` в коллекцию `Deque`,
 * только если его `id` больше, чем текущий максимальный `id` в коллекции.
 */
public class AddIfMax extends Command {
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
     * Идентификатор маршрута.
     */
    private Integer id;

    /**
     * Конструктор класса `AddIfMax`.
     *
     * @param deque   Коллекция `Deque`, в которую добавляется маршрут.
     */
    public AddIfMax(RouteCollection routeCollection, DBUsersHandler usersHandler, DBRouteHandler routeHandler) {
        super(routeCollection, usersHandler, routeHandler);
    }

    /**
     * Выполняет команду добавления нового элемента `Route` в коллекцию, если его `id` больше максимального.
     * Запрашивает у пользователя данные для создания объекта `Route` и добавляет его в коллекцию `Deque`,
     * только если `id` больше, чем `maxId` в коллекции.
     */
    public Response execute(Request request, RequestReader requestReader, ResponseWriter responseWriter) throws IOException, ClassNotFoundException {
        id = null;
        if (!request.getArg().isEmpty()){
            id = integerConverter(request.getArg());
        }
        if (id == null)
        {
            id = getValidInt(request.isScript(), requestReader, responseWriter, "Введите id : ");
        }

        if (id > deque.getMaxId()) {
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

            deque.addRoute(id, name, coordinates, creationDate, from, to, distance);

            return new Response("Маршрут добавлен\n", true);
        }
        else{
            return new Response("Введенное значение меньше максимального id коллекции\n ", true);
        }
    }
}
