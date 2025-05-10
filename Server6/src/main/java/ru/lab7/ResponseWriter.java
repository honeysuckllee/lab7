package ru.lab7;

import java.io.IOException;
        import java.io.ObjectOutputStream;

public class ResponseWriter {
    private final ObjectOutputStream out;

    public ResponseWriter(ObjectOutputStream out) {
        this.out = out;
    }

    public void sendResponse(String command) throws IOException {
        out.writeObject(new Response(command));
        out.flush();
    }

    public void sendResponse(String command, boolean isEndCommand) throws IOException {
        out.writeObject(new Response(command, isEndCommand));
        out.flush();
    }

    public void sendResponse(Response response) throws IOException {
        out.writeObject(response);
        out.flush();
    }
}
