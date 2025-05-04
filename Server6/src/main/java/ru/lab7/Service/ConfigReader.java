package ru.lab7.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.io.File;

@Data

//читает config и достает номер порта
public class ConfigReader {
    private int port;
    public static int getPortFromConfig(String filePath) {
        try {
            // Создаем ObjectMapper для работы с JSON
            ObjectMapper objectMapper = new ObjectMapper();

            // Читаем JSON-файл
            File file = new File(filePath);

            // Преобразуем JSON в объект ConfigReader
            ConfigReader config = objectMapper.readValue(file, ConfigReader.class);

            // Возвращаем номер порта
            return config.getPort();
        } catch (Exception e) {
            e.printStackTrace();
            return -1; // Возвращаем -1 в случае ошибки
        }
    }
}
