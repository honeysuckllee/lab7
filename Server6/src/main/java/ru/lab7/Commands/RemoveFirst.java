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
 * Класс `RemoveFirst` реализует интерфейс `Command` и представляет команду удаления первого элемента из коллекции.
 * При выполнении команды удаляется первый элемент коллекции.
 */
public class RemoveFirst extends Command {
    /**
     * Коллекция `Deque`, из которой удаляется первый элемент.
     */
    private Deque deque;

    /**
     * Конструктор класса `RemoveFirst`.
     *
     */
    public RemoveFirst(RouteCollection routeCollection, DBUsersHandler usersHandler, DBRouteHandler routeHandler) {
        super(routeCollection, usersHandler, routeHandler);
    }

    /**
     * Метод `execute` удаляет первый элемент из коллекции.
     */

    @Override
    public Response execute(Request request, RequestReader requestReader, ResponseWriter responseWriter) throws IOException, ClassNotFoundException {
        StringBuilder rez = new StringBuilder("\n");

        if (deque.removeFirst()){
            rez.append("Успешно удален первый элемент").append("\n");
        }
        else{
            rez.append("Коллекция пуста").append("\n");
        }
        return new Response(rez.toString(), true);
    }
}