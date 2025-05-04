package ru.lab7.Connection;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class ConnectionHandler {
    private ServerSocketChannel serverChannel;

    public ConnectionHandler(int port) throws IOException {
        serverChannel = ServerSocketChannel.open();
        serverChannel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
        serverChannel.bind(new InetSocketAddress(port));
        System.out.println("Сервер запущен на порту " + port);
    }

    public SocketChannel acceptClient() throws IOException {
        return serverChannel.accept();

    }
}
