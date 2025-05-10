package ru.lab7.Model;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import lombok.Data;

@Data
public class RouteCollection {

    public static Set<Route> routes = Collections.synchronizedSet(new HashSet<>());
    private static final LocalDateTime initializationDate = LocalDateTime.now();


    public RouteCollection(HashSet<Route> route) {
        RouteCollection.routes = route;
    }
    /**
     * Сортирует коллекцию фильмов по общим кассовым сборам в порядке возрастания.
     *
     * @return Отсортированный список фильмов.
     */
    public ArrayList<Route> sortedRoute()
    {
        ArrayList<Route> sortedList = new ArrayList<>(routes);
        Collections.sort(sortedList);
        return sortedList;
    }

    public int getLength(){
        return (new ArrayList<>(routes)).size();
    }

    public LocalDateTime getInitializationdate() {
        return initializationDate;
    }

    public String getType(){
        return routes.getClass().getSimpleName();
    }

    public int getMaxId() {
        int maxId = 0;
        for (Route route : routes) {
            if (route.getId() > maxId) {
                maxId = route.getId();
            }
        }
        return maxId;
    }

    public int getMinId() {
        int minId = getMaxId();
        for (Route route : routes) {
            if (route.getId() < minId) {
                minId = route.getId();
            }
        }
        return minId;
    }

    public void addRoute(int id, String name, Coordinates coordinates, LocalDate creationDate, Location from,
                         Location to, Float distance, int userId){
        routes.add(new Route(id, name, coordinates, creationDate, from, to, distance, userId));
    }

    public void update(int id, String name, Coordinates coordinates, LocalDate creationDate, Location from,
                       Location to, Float distance, int userId) {
        synchronized(routes) {
            for(Route route : routes) {
                if(route.getId() == id) {
                    route.setName(name);
                    route.setCoordinates(coordinates);
                    route.setCreationDate(creationDate);
                    route.setFrom(from);
                    route.setTo(to);
                    route.setDistance(distance);
                    break;
                }
            }
        }
    }


    public void removeRoute(int id) {
        synchronized(routes) {
            for(Route route : routes) {
                if(route.getId() == id) {
                    routes.remove(route);
                    break;
                }
            }
        }
    }

    public static List<Integer> idToRemove(int maxId) {
        List<Integer> result = new ArrayList<>();
        for (Route route : routes) {
            int id = route.getId();
            if (id < maxId) {
                result.add(id);
            }
        }
        return result;
    }



    /**
     * Выводит в консоль уникальные значения расстояний (`distance`) для всех объектов `Route` в коллекции.
     */
    public String printUniqueDistance(){
        StringBuilder rez = new StringBuilder();
        if (getLength()  == 0){
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
     * Выводит в консоль информацию о всех объектах `Route`, название которых начинается с указанного префикса.
     *
     * @param prefix Префикс для поиска объектов `Route` по названию.
     */
    public String filterStartsWithName(String prefix){
        int countName = 0;
        StringBuilder rez = new StringBuilder();
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
     * Выводит в консоль информацию о всех объектах `Route`, отсортированных по убыванию расстояния (`distance`).
     */
    public String printFieldDescendingDistance(){
        StringBuilder rez = new StringBuilder();
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

}