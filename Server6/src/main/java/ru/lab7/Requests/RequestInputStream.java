package ru.lab7.Requests;

import java.io.*;

public class RequestInputStream extends InputStream {
    private final InputStream wrappedStream;

    // Конструктор принимает базовый поток
    public RequestInputStream(InputStream wrappedStream) {
        this.wrappedStream = wrappedStream;
    }

    @Override
    public int read() throws IOException {
        return wrappedStream.read();
    }

    @Override
    public int read(byte[] data) throws IOException {
        return wrappedStream.read(data);
    }

    @Override
    public void close() throws IOException {// не закрываем поток

    }
}