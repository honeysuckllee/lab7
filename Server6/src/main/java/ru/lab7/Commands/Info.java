package ru.lab7.Commands;

import ru.lab7.DataBase.DBRouteHandler;
import ru.lab7.DataBase.DBUsersHandler;
import ru.lab7.Model.Deque;
import ru.lab7.Model.RouteCollection;
import ru.lab7.Requests.Request;
import ru.lab7.Requests.RequestReader;
import ru.lab7.Response;
import ru.lab7.ResponseWriter;

/**
 * Класс `Info` реализует интерфейс `Command` и представляет команду вывода информации о коллекции.
 * При выполнении команды выводятся тип коллекции, время создания и количество элементов.
 */
public class Info extends Command {


    /**
     * Конструктор класса `Info`.
     *
     */
    public Info(RouteCollection routeCollection, DBUsersHandler usersHandler, DBRouteHandler routeHandler) {
        super(routeCollection, usersHandler, routeHandler);
    }

    /**
     * Метод `execute` выводит информацию о коллекции, включая тип коллекции,
     * время создания и количество элементов.
     * Если возникает ошибка при получении времени создания, выбрасывается исключение `RuntimeException`.
     */
    @Override
    public Response execute(Request request, RequestReader requestReader, ResponseWriter responseWriter) {
        String rez = "Тип коллекции:" + collection.getType() + "\n";
        rez += "Время создания коллекции:" + collection.getInitializationdate() + "\n";
        rez += "Количество элементов в коллекции:" + collection.getLength() + "\n";
        return new Response(rez, true);
    }
}