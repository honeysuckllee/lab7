package ru.lab7.Service;


import lombok.Getter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXParseException;
import ru.lab7.Model.Coordinates;
import ru.lab7.Model.Location;
import ru.lab7.Model.Route;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayDeque;


/**
 * Класс `XMLReader` предназначен для чтения данных класса Route из XML-файла.
 */
@Getter
public class XMLReader {
    /**
     * Имя файла, из которого будет производиться чтение данных.
     */
    private String fileName;
    /**
     * Максимальный ID, найденный в прочитанных данных.
     */
    private int maxId;
    private boolean error;
    /**
     * Конструктор класса `XMLReader`.
     *
     * @param filePath Путь к XML-файлу.
     */
    public XMLReader(String filePath) {
        fileName = filePath;
        maxId = 0;
        error = false;
    }


    /**
     * Читает данные классов Route из XML-файла и добавляет их в переданную коллекцию.
     *
     * @param routes Коллекция `ArrayDeque`, в которую будут добавлены прочитанные классы.
     */
    public void readRoutesFromXML(ArrayDeque<Route> routes) {
        try (FileInputStream fis = new FileInputStream(fileName);
             InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8)) {

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fis);

            doc.getDocumentElement().normalize();

            NodeList nodeList = doc.getElementsByTagName("route");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    int id = Integer.parseInt(element.getElementsByTagName("id").item(0).getTextContent());
                    if (id > maxId) {
                        maxId = id;
                    }
                    String name = element.getElementsByTagName("name").item(0).getTextContent();

                    Element coordinatesElement = (Element) element.getElementsByTagName("coordinates").item(0);
                    double latitude = Double.parseDouble(coordinatesElement.getElementsByTagName("latitude").item(0).getTextContent());
                    float longitude = Float.parseFloat(coordinatesElement.getElementsByTagName("longitude").item(0).getTextContent());
                    Coordinates coordinates = new Coordinates(latitude, longitude);

                    LocalDate creationDate = LocalDate.parse(element.getElementsByTagName("creationDate").item(0).getTextContent());

                    // Обработка from
                    Location from = null;
                    Node fromNode = element.getElementsByTagName("from").item(0);
                    if (fromNode != null && fromNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element fromElement = (Element) fromNode;
                        if (fromElement.hasChildNodes()) {
                            int fromX = Integer.parseInt(fromElement.getElementsByTagName("x").item(0).getTextContent());
                            Float fromY = Float.parseFloat(fromElement.getElementsByTagName("y").item(0).getTextContent());
                            int fromZ = Integer.parseInt(fromElement.getElementsByTagName("z").item(0).getTextContent());
                            from = new Location(fromX, fromY, fromZ);
                        }
                    }

                    // Обработка to
                    Location to = null;
                    Node toNode = element.getElementsByTagName("to").item(0);
                    if (toNode != null && toNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element toElement = (Element) toNode;
                        if (toElement.hasChildNodes()) {
                            int toX = Integer.parseInt(toElement.getElementsByTagName("x").item(0).getTextContent());
                            Float toY = Float.parseFloat(toElement.getElementsByTagName("y").item(0).getTextContent());
                            int toZ = Integer.parseInt(toElement.getElementsByTagName("z").item(0).getTextContent());
                            to = new Location(toX, toY, toZ);
                        }
                    }

                    // Обработка distance
                    Float distance = null;
                    Node distanceNode = element.getElementsByTagName("distance").item(0);
                    if (distanceNode != null) {
                        if (!distanceNode.getTextContent().isEmpty()) {
                            distance = Float.parseFloat(distanceNode.getTextContent());
                        }
                    }
                    Route route = new Route(id, name, coordinates, creationDate, from, to, distance);
                    routes.add(route);
                }
            }
        } catch (SAXParseException | IOException | NullPointerException e){
            error = true;
            System.out.println("Общая ошибка:" + e.getMessage());
        } catch (Exception e){
            error = true;
            System.out.println("Общая ошибка:" + e.getMessage());
        }

    }
}
