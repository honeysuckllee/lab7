package ru.lab7.Requests;

import ru.lab7.DataBase.DBRouteHandler;
import ru.lab7.DataBase.DBUsersHandler;
import ru.lab7.Model.RouteCollection;
import ru.lab7.Commands.Command;
import ru.lab7.Response;
import ru.lab7.ResponseWriter;
import ru.lab7.ServerApp;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.channels.SocketChannel;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;



public class RequestHandler {
    //private ObjectInputStream in;
    private ResponseWriter responseWriter;
    private final ExecutorService processPool;
    private final ExecutorService responsePool;
    private HashMap<String, Command> commandHashMap;
    private Map<String, Boolean> additionalInputForCommand;
    private SocketChannel myChannel;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private RequestReader requestReader;
    private RequestInputStream requestInputStream;
    private RouteCollection routeCollection;
    private DBUsersHandler usersHandler;
    private DBRouteHandler routeHandler;

    public RequestHandler(SocketChannel clientChannel, ExecutorService processPool,
                          ExecutorService responsePool, HashMap<String, Command> commandHashMap,
                          RouteCollection routeCollection, DBUsersHandler usersHandler,
                          DBRouteHandler routeHandler) throws IOException {
        myChannel = clientChannel;
        this.processPool = processPool;
        this.responsePool = responsePool;
        this.commandHashMap = commandHashMap;
        this.routeCollection = routeCollection;
        out = new ObjectOutputStream(clientChannel.socket().getOutputStream());
        in = new ObjectInputStream(clientChannel.socket().getInputStream());
        this.usersHandler = usersHandler;
        this.routeHandler = routeHandler;
        requestInputStream = new RequestInputStream(in);
        responseWriter = new ResponseWriter(out);
        //requestReader = new RequestReader(requestInputStream);
    }

    public void handleClient() throws SQLException {
        try {

            while(true) {
                Request entrance = (Request) in.readObject();
                if (entrance.getCommand().equals("join")) {
                    responseWriter.sendResponse("Введите логин и пароль");
                    Request credentials = (Request) in.readObject();
                    String login = credentials.getLogin();
                    String password = credentials.getPassword();

                    if (ServerApp.allUsers.authenticate(login, password)) {
                        responseWriter.sendResponse("OK");
                        break;
                    } else {
                        responseWriter.sendResponse("Неверный логин или пароль");
                    }
                }
                else if (entrance.getCommand().equals("register")){
                    responseWriter.sendResponse("Введите логин и пароль");
                    Request credentials = (Request) in.readObject();
                    String login = credentials.getLogin();
                    String password = credentials.getPassword();
                    if (ServerApp.allUsers.register(login, password)) {
                        ServerApp.usersHandler.registerUser(login, password);
                        responseWriter.sendResponse("OK");
                        break;
                    } else {
                        responseWriter.sendResponse("Пользователь уже существует");
                    }

                }
                else {
                    responseWriter.sendResponse("Введена неверная команда");
                }

            }

            while (true) {
                    // Выполняем команду
                    try {
                        Request command = (Request) in.readObject();

                        if (!commandHashMap.containsKey(command.getCommand())){
                            responseWriter.sendResponse("Команда не найдена", true);
                        }
                        else {
                            Response response = commandHashMap.get(command.getCommand()).execute(command,
                                                                    in, responseWriter);

                            try {
                                responseWriter.sendResponse(response);
                            } catch (IOException e) {
                                System.out.println("Клиент отключился.");
                                break;
                            }
                        }
                    } catch (Exception e) {
                        System.out.println("Клиент отключился.");
                        break;
                    }
                }
            }
        catch (ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        } catch (IOException e) {
            System.out.println("Клиент отключился.");
        }
    }
}