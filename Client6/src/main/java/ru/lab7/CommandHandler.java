package ru.lab7;

import lombok.Data;
import ru.lab7.Requests.Request;

import java.util.Scanner;


@Data
public class CommandHandler {
    private String command;
    private String[] commandSplit;
    public Scanner scanner;
    private String arg;
    private boolean isScript;
    private String login;
    private String password;



    public CommandHandler() {
    }

    /**
     * Launches the command processor and enters the interactive mode.
     * @return 0 if the program is finished properly.
     */
    public void readCommand() {
        String inputStr = scanner.nextLine();
        commandSplit = inputStr.trim().split(" ");
        command = commandSplit[0];
        arg = (commandSplit.length > 1) ? commandSplit[1]:""; //укороченный if
    }
    public Request generateRequest(String command, String arg, boolean isScript, String login, String password) {
        if (command.isEmpty() & arg.isEmpty()){
            return new Request(null,null, false, null, null);
        }
        this.command = command;
        this.arg = arg;
        this.isScript = isScript;
        return new Request(command, arg, isScript, login, password);
    }
    public Request generateRequestLoginPassword(String command, String login, String password){
        this.command = command;
        this.login = login;
        this.password = password;
        return new Request(command, arg, false, login, password);
    }
}
