package ru.lab7.Model;

import lombok.Data;
import ru.lab7.Service.XMLReader;
import ru.lab7.Service.XMLWriter;
import ru.lab7.DataBase.DBRouteHandler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

/**
 * Класс `Deque` представляет собой коллекцию `Route` и предоставляет методы для управления ею.
 */
@Data
public class Deque {
    /**
     * Имя переменной окружения, содержащей путь к файлу с данными о маршрутах.
     */
    private String routeVariable;
    /**
     * Максимальный идентификатор `id` среди всех объектов `Route` в коллекции.
     */
    private int maxId;
    private boolean error;
    /**
     * Статическая коллекция `ArrayDeque`, хранящая объекты `Route`.
     */
    public static ArrayDeque<Route> routes = new ArrayDeque<>();

    private DBRouteHandler dbRouteHandler;

    /**
     * Возвращает имя файла коллекции, полученное из переменной окружения "ROUTE_COLLECTION".
     *
     * @return Имя файла коллекции.
     */
    public String getCollectionFileName() {
        return System.getenv("ROUTE_COLLECTION");
    }

    /**
     * Возвращает дату создания файла коллекции в формате "yyyy-MM-dd HH:mm:ss".
     *
     * @return Дата создания файла коллекции в виде строки.
     * @throws IOException Если не удается получить атрибуты файла.
     */
    public String getCollectionFileCreationDate() throws IOException {
        Path path = Paths.get(getCollectionFileName());
        BasicFileAttributes attrs = Files.readAttributes(path, BasicFileAttributes.class);
        // Получаем дату создания файла
        FileTime creationTime = attrs.creationTime();

        // Преобразуем FileTime в строку
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(new Date(creationTime.toMillis()));
    }

    /**
     * Конструктор класса `Deque`. Инициализирует коллекцию, читая данные из XML-файла,
     * путь к которому берется из переменной окружения "ROUTE_COLLECTION".
     */
    public Deque() {
        /*
        routeVariable = getCollectionFileName();
        //System.out.println(routeVariable);

        if (routeVariable == null){
            System.out.println("Ошибка чтения имени файла коллекции");
        }
        else
        {
            XMLReader xmlReader = new XMLReader(routeVariable);
            xmlReader.readRoutesFromXML(routes);
            maxId = xmlReader.getMaxId();
            error = xmlReader.isError();
        }
        */
    }

    /**
     * Возвращает коллекцию `ArrayDeque` с объектами `Route`.
     *
     * @return Коллекция `ArrayDeque` с объектами `Route`.
     */
    public ArrayDeque<Route> getDeque() {
        return routes;
    }

    public boolean isEmpty(){
        return routes.isEmpty();
    }

    /**
     * Добавляет новый объект `Route` в коллекцию с автоматически генерируемым `id`.
     *
     * @param name          Название маршрута.
     * @param coordinates   Координаты маршрута.
     * @param creationDate  Дата создания маршрута.
     * @param from          Начальная локация маршрута.
     * @param to            Конечная локация маршрута.
     * @param distance      Расстояние маршрута.
     */
    public void addRoute(String name, Coordinates coordinates, LocalDate creationDate, Location from, Location to, Float distance) {
        maxId += 1;
        Route route = new Route(maxId, name, coordinates, creationDate, from, to, distance);
        routes.add(route);
    }

    /**
     * Очищает коллекцию `routes`.
     */
    public void clear(){
        routes.clear();
    }

    /**
     * Сохраняет коллекцию `routes` в XML-файл.
     *
     * @param fileName Имя файла для сохранения данных.
     */
    public void save(String fileName){
        XMLWriter writer = new XMLWriter(fileName);
        writer.writeRoutesToXML(routes);
    }

    /**
     * Обновляет данные существующего объекта `Route` в коллекции по его `id`.
     * Если объект с указанным `id` не найден, он добавляется в коллекцию.
     *
     * @param id            Идентификатор `id` объекта `Route` для обновления.
     * @param name          Новое название маршрута.
     * @param coordinates   Новые координаты маршрута.
     * @param creationDate  Новая дата создания маршрута.
     * @param from          Новая начальная локация маршрута.
     * @param to            Новая конечная локация маршрута.
     * @param distance      Новое расстояние маршрута.
     */
    public String updateRoute(int id, String name, Coordinates coordinates, LocalDate creationDate, Location from, Location to, Float distance){
        StringBuilder rez = new StringBuilder();
        boolean flag = false;
        for (Route route : routes) {
            if (route.getId() == id){
                route.setName(name);
                route.setCoordinates(coordinates);
                route.setCreationDate(creationDate);
                route.setFrom(from);
                route.setTo(to);
                route.setDistance(distance);
                flag = true;
                rez.append("Элемент обновлен\n");
                break;
            }
        }
        if (!flag){
            rez.append("Элемент для update не найден\nЭлемент добавлен в коллекцию\n");
            Route route = new Route(id, name, coordinates, creationDate, from, to, distance);
            routes.add(route);
            if (maxId < id){
                maxId = id;
            }
        }
        return rez.toString();
    }

    /**
     * Удаляет первый элемент из коллекции `routes`.
     */
    public boolean removeFirst(){
        if (!routes.isEmpty()){
            routes.pollFirst();
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * Добавляет новый объект `Route` в коллекцию с указанным `id`.
     *
     * @param id            Идентификатор `id` объекта `Route`.
     * @param name          Название маршрута.
     * @param coordinates   Координаты маршрута.
     * @param creationDate  Дата создания маршрута.
     * @param from          Начальная локация маршрута.
     * @param to            Конечная локация маршрута.
     * @param distance      Расстояние маршрута.
     */
    public void addRoute(int id, String name, Coordinates coordinates, LocalDate creationDate, Location from, Location to, Float distance) {
        Route route = new Route(id, name, coordinates, creationDate, from, to, distance);
        routes.add(route);
        if (id > maxId){
            maxId = id;
        }
    }

    /**
     * Удаляет из коллекции все объекты `Route`, `id` которых меньше указанного.
     *
     * @param id Идентификатор `id`, меньше которого будут удалены элементы.
     */
    public int removeLower(int id){
        int counterDell = 0;
        for (Route route : routes){
            if (route.getId() < id){
                routes.remove(route);
                counterDell += 1;
            }
        }
        return counterDell;
    }

    /**
     * Удаляет объект `Route` из коллекции по указанному `id`.
     *
     * @param id Идентификатор `id` объекта `Route` для удаления.
     */
    public boolean removeById(int id){
        if (id > maxId){
            return false;
            //System.out.println("Такого id нет в коллекции");
        }
        else {
            boolean dell = false;
            for (Route route : routes){
                if (route.getId() == id){
                    routes.remove(route);
                    dell = true;
                    break;
                }
            }
            return dell;
        }
    }

    /**
     * Выводит в консоль информацию о всех объектах `Route`, название которых начинается с указанного префикса.
     *
     * @param prefix Префикс для поиска объектов `Route` по названию.
     */
    public String filterStartsWithName(String prefix){
        int countName = 0;
        StringBuilder rez = new StringBuilder("");
        for (Route route : routes){
            if (route.getName().startsWith(prefix)){
                countName += 1;
                rez.append(route).append("\n");            }
        }
        if (countName == 0){
            rez.append("Элементов, начинающихся с "+ prefix+" нет!");
        }
        return rez.toString();
    }

    /**
     * Выводит в консоль уникальные значения расстояний (`distance`) для всех объектов `Route` в коллекции.
     */
    public String printUniqueDistance(){
        StringBuilder rez = new StringBuilder("");
        if (routes.isEmpty()){
            rez.append("Коллекция пуста(");
        }
        else{
            Set<Float> set = new TreeSet<>();
            for (Route route : routes){
                set.add(route.getDistance());
            }
            rez.append(set);
        }
        return rez.toString();
    }

    /**
     * Выводит в консоль информацию о всех объектах `Route`, отсортированных по убыванию расстояния (`distance`).
     */
    public String printFieldDescendingDistance(){
        StringBuilder rez = new StringBuilder("");
        if (routes.isEmpty()){
            rez.append("Коллекция пуста(");
        }
        else{
            List<Route> routeList = new ArrayList<>(routes);

            // Сортируем список по убыванию distance с помощью компаратора
            Collections.sort(routeList, new Comparator<Route>() {
                @Override
                public int compare(Route r1, Route r2) {
                    // Сравниваем в обратном порядке для сортировки по убыванию
                    return Float.compare(r2.getDistance(), r1.getDistance());
                }
            });
            // Выводим отсортированные элементы
            for (Route route : routeList) {
                rez.append(route.getDistance().toString() + ", ");
            }
        }
        String distance = rez.toString();
        distance = distance.substring(0, distance.length() - 2);
        return distance;
    }

    /**
     * Возвращает имя класса коллекции `routes` (в данном случае, "ArrayDeque").
     *
     * @return Имя класса коллекции.
     */
    public static String getCollectionType() {
        return routes.getClass().getSimpleName(); // Получаем имя класса коллекции
    }

}
