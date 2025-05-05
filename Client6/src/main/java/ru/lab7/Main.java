package ru.lab7;

import ru.lab7.Service.ConfigReader;

public class Main {
    public static void main(String[] args) {
        Client client = new Client(ConfigReader.getPortFromConfig("C:\\Users\\Светлана\\IdeaProjects\\lab7\\Server6\\src\\main\\java\\ru\\lab7\\Service\\config.json"));
        client.run();
    }
}