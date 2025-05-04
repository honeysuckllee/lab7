package ru.lab7.Model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Класс, представляющий маршрут.
 * Объекты этого класса помещаются в коллекцию для хранения и обработки.
 */
@Getter
@Setter
public class Route implements  Comparable<Route>, Serializable {
    /**
     * Уникальный идентификатор маршрута.
     * Значение должно быть больше 0, уникальным и генерироваться автоматически.
     */
    private int id;

    /**
     * Название маршрута.
     * Поле не может быть null, строка не может быть пустой.
     */
    private String name;

    /**
     * Координаты маршрута.
     * Поле не может быть null.
     */
    private Coordinates coordinates;

    /**
     * Дата создания маршрута.
     * Поле не может быть null, значение должно генерироваться автоматически.
     */
    private LocalDate creationDate;

    /**
     * Начальная точка маршрута.
     * Поле может быть null.
     */
    private Location from;

    /**
     * Конечная точка маршрута.
     * Поле может быть null.
     */
    private Location to;

    /**
     * Расстояние маршрута.
     * Поле может быть null, значение должно быть больше 1.
     */
    private Float distance;

    /**
     * Конструктор для создания объекта маршрута.
     *
     * @param id           Уникальный идентификатор маршрута.
     * @param name         Название маршрута.
     * @param coordinates  Координаты маршрута.
     * @param creationDate Дата создания маршрута.
     * @param from         Начальная точка маршрута.
     * @param to           Конечная точка маршрута.
     * @param distance     Расстояние маршрута.
     */
    public Route(int id, String name, Coordinates coordinates, LocalDate creationDate,
                 Location from, Location to, Float distance) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.from = from;
        this.to = to;
        this.distance = distance;
    }
    public Float getDistance(){
        if (this.distance == null){
            return 0.f;
        }
        return distance;
    }

    @Override
    public int compareTo(Route other) {
        return this.name.compareTo(other.name);
    }
    /**
     * Возвращает строковое представление объекта маршрута.
     *
     * @return Строка, содержащая информацию о маршруте.
     */
    @Override
    public String toString() {
        String out =  "Route{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", coordinates=" + " X:" + coordinates.getX() + " Y:" + coordinates.getY() +
                ", creationDate=" + creationDate ;
        if (from == null){
            out += ", from=null";
        } else {
            out += ", from=" + from.getX() + " " + from.getY() + " " + from.getZ();
        }
        if (to == null){
            out += ", to=null";
        } else {
            out += ", to=" + to.getX() + " " + to.getY() + " " + to.getZ();
        }
        if (distance == null){
            out += ", distance=null";
        } else {
            out += ", distance=" + distance;
        }
        out += '}';
        return out;
    }
}