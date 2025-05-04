package ru.lab7.Service;

import ru.lab7.Model.Route;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayDeque;

/**
 * Класс `XMLWriter` предназначен для записи данных Route в XML-файл.
 */
public class XMLWriter {

    /**
     * Имя файла, в который будет производиться запись данных.
     */
    private String fileName;

    /**
     * Конструктор класса `XMLWriter`.
     *
     * @param fileName Путь к XML-файлу.
     */
    public XMLWriter(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Записывает данные о маршрутах из переданной коллекции в XML-файл.
     *
     * @param routes Коллекция `ArrayDeque` объектов `Route`, которые будут записаны в файл.
     */
    public void writeRoutesToXML(ArrayDeque<Route> routes) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            // Запись начала XML-документа
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            writer.write("<routes>\n");

            // Запись каждого маршрута
            for (Route route : routes) {
                writer.write("  <route>\n");

                writer.write("    <id>" + route.getId() + "</id>\n");
                writer.write("    <name>" + route.getName() + "</name>\n");

                // Запись координат
                writer.write("    <coordinates>\n");
                writer.write("      <latitude>" + route.getCoordinates().getLatitude() + "</latitude>\n");
                writer.write("      <longitude>" + route.getCoordinates().getLongitude() + "</longitude>\n");
                writer.write("    </coordinates>\n");

                // Запись даты создания
                writer.write("    <creationDate>" + route.getCreationDate() + "</creationDate>\n");

                // Запись локации "from" (если не null)
                if (route.getFrom() != null) {
                    writer.write("    <from>\n");
                    writer.write("      <x>" + route.getFrom().getX() + "</x>\n");
                    writer.write("      <y>" + route.getFrom().getY() + "</y>\n");
                    writer.write("      <z>" + route.getFrom().getZ() + "</z>\n");
                    writer.write("    </from>\n");
                } else {
                    writer.write("    <from/>\n"); // Пустой элемент, если from равен null
                }

                // Запись локации "to" (если не null)
                if (route.getTo() != null) {
                    writer.write("    <to>\n");
                    writer.write("      <x>" + route.getTo().getX() + "</x>\n");
                    writer.write("      <y>" + route.getTo().getY() + "</y>\n");
                    writer.write("      <z>" + route.getTo().getZ() + "</z>\n");
                    writer.write("    </to>\n");
                } else {
                    writer.write("    <to/>\n"); // Пустой элемент, если to равен null
                }

                // Запись расстояния (если не null)
                if (route.getDistance() != null) {
                    writer.write("    <distance>" + route.getDistance() + "</distance>\n");
                } else {
                    writer.write("    <distance/>\n"); // Пустой элемент, если distance равен null
                }

                writer.write("  </route>\n");
            }

            // Запись конца XML-документа
            writer.write("</routes>\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}