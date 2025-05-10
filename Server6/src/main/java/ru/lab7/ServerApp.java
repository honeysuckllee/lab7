package ru.lab7;

import ru.lab7.Commands.*;
import  ru.lab7.Commands.Command;
import ru.lab7.DataBase.DBRouteHandler;
import ru.lab7.DataBase.DBUsersHandler;
import ru.lab7.DataBase.IDCreator;
import ru.lab7.DataBase.Users.UserManager;
import ru.lab7.Model.RouteCollection;
import ru.lab7.Connection.ConnectionHandler;
import ru.lab7.Model.Deque;
import lombok.Data;
import ru.lab7.Requests.RequestHandler;

import java.io.*;
import java.nio.channels.SocketChannel;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.concurrent.*;



@Data
public class ServerApp {
    private static int port;
    private static HashMap<String, Command> commandHashMap;
    public static final Logger logger = Logger.getLogger("Server");
    // Пулы потоков
    private static final ExecutorService readPool = Executors.newFixedThreadPool(10); // Чтение
    private static final ExecutorService processPool = Executors.newCachedThreadPool(); // Обработка
    private static final ExecutorService responsePool = Executors.newCachedThreadPool(); // Ответы

    //static final UserManager userManager = new UserManager();
    static String dbUrl = "jdbc:postgresql://localhost:5432/studs";
    static String dbUser = "s467922";
    static String dbPassword = "Xyn617glEZRF363L";

    public static DBUsersHandler usersHandler = new DBUsersHandler(dbUrl, dbUser, dbPassword);
    public static DBRouteHandler routeHandler = new DBRouteHandler(usersHandler.getConnection());
    //public static IDCreator idCreator = new IDCreator(usersHandler.getConnection());

    public static UserManager allUsers;
    public ServerApp(int port) throws IOException {
        this.port = port;
        Handler handler = new FileHandler("log.txt");
        logger.addHandler(handler);
        logger.setUseParentHandlers(false);
    }


    public static void initCommandsMap(RouteCollection routeCollection, DBUsersHandler usersHandler, DBRouteHandler routeHandler){
        commandHashMap = new HashMap<String, Command>();
        commandHashMap.put("help", new Help(routeCollection, usersHandler, routeHandler));
        commandHashMap.put("add", new Add(routeCollection, usersHandler, routeHandler));
        commandHashMap.put("show", new Show(routeCollection, usersHandler, routeHandler));
        commandHashMap.put("info", new Info(routeCollection, usersHandler, routeHandler));
        commandHashMap.put("remove_first", new RemoveFirst(routeCollection, usersHandler, routeHandler));
        commandHashMap.put("remove_lower", new RemoveLower(routeCollection, usersHandler, routeHandler));
        commandHashMap.put("remove_by_id", new RemoveById(routeCollection, usersHandler, routeHandler));
        commandHashMap.put("add_if_max", new AddIfMax(routeCollection, usersHandler, routeHandler));
        commandHashMap.put("filter_starts_with_name", new FilterStartsWithName(routeCollection, usersHandler, routeHandler));
        commandHashMap.put("print_unique_distance", new PrintUniqueDistance(routeCollection, usersHandler, routeHandler));
        commandHashMap.put("print_field_descending_distance", new PrintFieldDescendingDistance(routeCollection, usersHandler, routeHandler));
        commandHashMap.put("update", new Update(routeCollection, usersHandler, routeHandler));
    }

    public static void run() throws IOException, ClassNotFoundException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Сервер начал работу.");
        logger.info("Сервер начал работу.");

        // Создаем серверный сокет и привязываем его к порту
        // System.out.println("Сервер ожидает подключения на порту " + port);

        // Поток для обработки команд "exit"
        Thread exitThread = new Thread(() -> {
            while (true) {
                try {
                    String line = scanner.nextLine();
                    if (line.equals("exit")) {
                        logger.info("Сервер завершил работу.");
                        System.exit(0); //программа завершилась
                    }
                } catch (NoSuchElementException e) {
                    System.err.println("Не тыкай Ctrl+D\n");
                }
            }
        });
        exitThread.start(); //запуск потока
        try {
            try {
                RouteCollection routes = new RouteCollection(routeHandler.load());// загрузка коллекции из бд в память
                System.out.println("Загружена коллекция " + routes.getLength()+ " элементов");
                allUsers = new UserManager(usersHandler.loadAllUsers());

                ConnectionHandler connectionHandler = new ConnectionHandler(port); //обработчик подключения клиента
                initCommandsMap(routes, usersHandler, routeHandler);
                while (true) {
                    SocketChannel clientChannel = connectionHandler.acceptClient(); // ждем подключения клиента
                    if (clientChannel != null) {
                        System.out.println("Подключился новый клиент.");
                        logger.info("Подключился новый клиент: " + clientChannel.getRemoteAddress());

                        readPool.execute(() -> {
                            try {
                                RequestHandler handler = new RequestHandler(clientChannel, processPool, responsePool,
                                                                commandHashMap, routes, usersHandler, routeHandler);
                                handler.handleClient();
                            } catch (IOException | SQLException e) {
                                logger.warning("Ошибка при создании RequestHandler: " + e.getMessage());
                            }
                        });
                    }
                }
            } catch (SQLException e) {
                System.out.println("Ошибка при загрузке данных из БД: "+  e.getMessage());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

