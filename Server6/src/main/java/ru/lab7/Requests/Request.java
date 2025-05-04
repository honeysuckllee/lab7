package ru.lab7.Requests;
import lombok.Data;

import java.io.Serializable;
@Data

/**
 * Базовый класс для запроса
 */
public class Request implements Serializable {
    private static final long serialVersionUID = 1L;

    private String command;
    private String arg;
    private boolean isScript;
    private String login;
    private String password;


    public Request(String command,String arg, boolean isScript, String login, String password) {
        this.command = command;
        this.arg = arg;
        this.isScript = isScript;
        this.login = login;
        this.password = password;
    }

    public Request() {
        this.command = "";
        this.arg = "";
        this.isScript = false;
    }

    public boolean isEmpty(){
        if (command == null){
            return true;
        }
        return command.isEmpty();
    }
}
