package ru.lab7.Requests;

import java.io.IOException;
import java.io.ObjectInputStream;

public class RequestReader {
    private  RequestInputStream in;
    public RequestReader(RequestInputStream in) {
        this.in = in;
    }

    public Request read() throws IOException, ClassNotFoundException {
        ObjectInputStream inputStream = new ObjectInputStream(in);
        Request entrance = (Request) inputStream.readObject();
        return entrance;
    }
}
