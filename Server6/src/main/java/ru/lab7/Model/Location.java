package ru.lab7.Model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Класс, представляющий местоположение.
 * Используется для хранения координат в трёхмерном пространстве.
 */
@Getter
@Setter
public class Location implements Serializable {
    /**
     * Координата X.
     */
    private int x;

    /**
     * Координата Y.
     * Поле не может быть null.
     */
    private Float y;

    /**
     * Координата Z.
     */
    private int z;

    /**
     * Конструктор для создания объекта местоположения.
     *
     * @param x Координата X.
     * @param y Координата Y. Не может быть null.
     * @param z Координата Z.
     * @throws IllegalArgumentException Если координата Y равна null.
     */
    public Location(int x, Float y, int z) {
        if (y == null) {
            throw new IllegalArgumentException("Координата Y не может быть null");
        }
        this.x = x;
        this.y = y;
        this.z = z;
    }
}