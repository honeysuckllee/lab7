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
 * Класс `PrintUniqueDistance` реализует интерфейс `Command` и представляет команду вывода уникальных значений поля `distance` из коллекции.
 * При выполнении команды выводятся все уникальные значения поля `distance` элементов коллекции.
 */
public class PrintUniqueDistance extends Command {
    /**
     * Коллекция `Deque`, из которой извлекаются уникальные значения поля `distance`.
     */
    private Deque deque;

    /**
     * Конструктор класса `PrintUniqueDistance`.
     *
     */
    public PrintUniqueDistance(RouteCollection routeCollection, DBUsersHandler usersHandler, DBRouteHandler routeHandler) {
        super(routeCollection, usersHandler, routeHandler);
    }

    /**
     * Метод `execute` выводит уникальные значения поля `distance` из коллекции.
     */

    @Override
    public Response execute(Request request, RequestReader requestReader, ResponseWriter responseWriter)
                            throws IOException, ClassNotFoundException {
        return new Response(collection.printUniqueDistance(), true);
    }
}