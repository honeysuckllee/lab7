package ru.lab7;


import ru.lab7.Requests.Request;

import java.io.*;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Stack;

/**
 * Класс клиента.
 * Используется для подключения к серверу.
 */
public class Client {
    /**
     * Конструктор
     *
     * @param port - порт подключения
     */
    public Client(int port) {
        this.port = port;
        useScript = new Stack<>();
        useScanners = new Stack<>();
        commandHandler = new CommandHandler();
    }

    /**
     * port - порт подключения
     */
    private static int port;
    /**
     * useScript - список выполняемых скриптов
     */
    private static Stack<String> useScript;
    /**
     * useScanners - список используемых сканеров
     */
    private static Stack<Scanner> useScanners;

    /**
     * commandHandler - менеджер комманд
     */
    static CommandHandler commandHandler;

    /**
     * метод для взаимодействия с сервером
     */
    public static void run() {
        Request request = null;
        Response response = null;

        boolean success = false;
        while (!success) {
            try {
                Scanner scn;
                Scanner scanner = new Scanner(System.in);
                // текущий сканер
                commandHandler.scanner = scanner;
                useScanners.push(scanner);

                System.out.println("Клиентское приложение для управления коллекцией. ");

                // Создаем TCP-сокет и подключаемся к серверу
                Socket socket = new Socket("localhost", port);
                System.out.println("Подключение к серверу установлено.");

                String login = "";
                String password = "";
                boolean isSuccessLogin = false;

                // Получаем входной и выходной потоки для обмена данными с сервером
                ObjectInputStream objectInputStreamCommand = new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream objectOutputStreamCommand = new ObjectOutputStream(socket.getOutputStream());

                System.out.println("Для авторизации введите команду join, для регистрации - команду register или 'exit' для завершения работы");
                while (!isSuccessLogin) {
                    commandHandler.readCommand();
                    try {
                        if (commandHandler.getCommand().equals("exit")) {
                            System.out.println("Завершение клиента.");
                            // Закрываем соединение с сервером
                            socket.close();
                            return;
                        }
                        if (commandHandler.getCommand().equals("join") || commandHandler.getCommand().equals("register"))
                        {
                            Request requestJoinRegister = commandHandler.generateRequest(commandHandler.getCommand(), "", false, login, password);
                            // Отправка данных на сервер
                            objectOutputStreamCommand.writeObject(requestJoinRegister);
                            objectOutputStreamCommand.flush();

                            String currentCommand = commandHandler.getCommand();
                            Response inputContainerCommand = (Response) objectInputStreamCommand.readObject();
                            System.out.println(inputContainerCommand.getResponseInfo());

                            System.out.println("Введите логин: ");
                            commandHandler.readCommand();
                            login = commandHandler.getCommand();
                            System.out.println("Введите пароль: ");
                            commandHandler.readCommand();
                            password = commandHandler.getCommand();
                            if (!login.isEmpty() && !password.isEmpty()){

                                Request requestCommand = commandHandler.generateRequest(currentCommand, "", false, login, password);
                                // Отправка данных на сервер
                                objectOutputStreamCommand.writeObject(requestCommand);
                                objectOutputStreamCommand.flush();

                                Response inputContainer = (Response) objectInputStreamCommand.readObject();
                                if (inputContainer.getResponseInfo().equals("OK") && commandHandler.getCommand().equals("join")){
                                    System.out.println("Авторизация прошла успешно");
                                    isSuccessLogin = true;
                                }
                                else {
                                    System.out.println(inputContainer.getResponseInfo());
                                }
                                if (inputContainer.getResponseInfo().equals("OK") && commandHandler.getCommand().equals("register")){
                                    System.out.println("Регистрация пользователя " + login + " прошла успешно, можете авторизоваться");
                                    isSuccessLogin = true;
                                }
                                else {
                                    System.out.println(inputContainer.getResponseInfo());
                                }
                            }
                        }
                        else{
                            System.out.println("Такой команды нет(");
                        }

                    } catch (Exception e) {
                            e.printStackTrace();
                            return;
                    }
                }


                System.out.println("Введите 'help' чтобы увидеть доступные команды или 'exit' для завершения работы.");

                while (true) {
                    commandHandler.readCommand();
                    request = commandHandler.generateRequest(commandHandler.getCommand(), commandHandler.getArg(), false, login, password);

                    try {
                        if (commandHandler.getCommand().equals("exit")) {
                            System.out.println("Завершение клиента.");
                            success = true;
                            // Закрываем соединение с сервером
                            socket.close();
                            break;
                        }
                        if (commandHandler.getCommand().equals("execute_script")) {

                            if (commandHandler.getArg().isEmpty()) {
                                System.out.println("Скрипт не задан");
                                continue;
                            }
                            if (useScanners.empty()) {
                                useScanners.push(commandHandler.scanner);
                            }
                            String scriptsPath = System.getenv("ROUTE_SCRIPTS");
                            // создание сканера для скрипта
                            Scanner scriptScanner = new Scanner(new FileReader(scriptsPath + commandHandler.getArg()));
                            commandHandler.scanner = scriptScanner;
                            useScanners.push(scriptScanner);
                            useScript.push(scriptsPath + commandHandler.getArg());
                            // делаем созданный сканер текущим
                            scn = scriptScanner;

                            while (useScanners.size() > 1) {
                                while (scn.hasNextLine()) {
                                    commandHandler.readCommand();
                                    if (!commandHandler.generateRequest(commandHandler.getCommand(), commandHandler.getArg(),true, login, password).isEmpty()) // commandHashMap.containsKey(scriptInput[0])
                                    {
                                        if (commandHandler.getCommand().equals("execute_script")) {
                                            if (useScript.contains(scriptsPath + commandHandler.getArg())) {
                                                // убираем зацикливание
                                                continue;
                                            }
                                            // создаем новый сканер
                                            Scanner embedScanner = new Scanner(new FileReader(scriptsPath + commandHandler.getArg()));
                                            commandHandler.scanner = embedScanner;
                                            useScript.push(scriptsPath + commandHandler.getArg());
                                            useScanners.push(embedScanner);
                                            scn = embedScanner;

                                        } else {
                                            Request requestScript = commandHandler.generateRequest(commandHandler.getCommand(), commandHandler.getArg(), true, login, password);

                                            boolean lastInput = false;
                                            while (!lastInput) {
                                                // Отправка данных на сервер
                                                objectOutputStreamCommand.writeObject(requestScript);


                                                // Получение ответа от сервера
                                                Response inputContainer = (Response) objectInputStreamCommand.readObject();
                                                if (!inputContainer.getResponseInfo().isEmpty() & !inputContainer.isEndCommand()) {
                                                    System.out.println(inputContainer.getResponseInfo());
                                                }
                                                if (inputContainer.isEndCommand()){
                                                    lastInput = true;
                                                    System.out.println(inputContainer.getResponseInfo());
                                                }else{
                                                    if (!inputContainer.getResponseInfo().isEmpty()) { System.out.println(inputContainer.getResponseInfo());}
                                                    commandHandler.readCommand();
                                                    requestScript = commandHandler.generateRequest(commandHandler.getCommand(), commandHandler.getArg(), true, login, password);
                                                }
                                            }
                                        }
                                    } else if (commandHandler.getCommand().equals("exit")) {
                                        break;
                                    }
                                }
                                if (!useScript.empty()) {
                                    useScript.pop();// убираем выполненный скрипт из списка
                                }
                                if (!useScanners.empty()) {
                                    commandHandler.scanner = useScanners.pop(); // делаем предыдущий сканер текущим
                                }
                            }

                            if (!useScript.empty()) {
                                useScript.pop();
                            }
                            if (!useScanners.empty()) {
                                commandHandler.scanner = useScanners.pop();
                            }
                            System.out.println("Выполнение скрипта завершено.\n");
                            request = new Request();//очистка после скрипта
                        }
                        if (!request.isEmpty()) {
                            boolean lastInput = false;
                            while (!lastInput) {

                                // Отправка данных на сервер
                                objectOutputStreamCommand.writeObject(request);
                                objectOutputStreamCommand.flush();

                                // Получение ответа от сервера
                                Response inputContainer = (Response) objectInputStreamCommand.readObject();
                                if (inputContainer.isEndCommand()){
                                    lastInput = true;
                                    if (request.getCommand().equals("exit")){
                                        System.out.println("Завершение клиента.");
                                        return;
                                    }
                                    System.out.println(inputContainer.getResponseInfo());
                                }else{
                                    System.out.println(inputContainer.getResponseInfo());
                                    commandHandler.readCommand();
                                    request = commandHandler.generateRequest(commandHandler.getCommand(), commandHandler.getArg(), false, login, password);
                                }

                            }
                        }
                    } catch (FileNotFoundException e1) {
                        System.err.println("Файл не найден");
                    }
                }

            } catch (NoSuchElementException e) {
                System.err.println("Не тыкай Ctrl+D\n");
                break;
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }
    }
}