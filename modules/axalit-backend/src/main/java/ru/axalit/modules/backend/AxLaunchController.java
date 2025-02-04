package ru.axalit.modules.backend;

import com.sun.net.httpserver.HttpServer;
import ru.axalit.modules.backend.handlers.AxTokenCheck;
import ru.axalit.modules.backend.handlers.AxTokenCreate;
import ru.axalit.modules.backend.handlers.AxTokenLogout;
import ru.axalit.modules.backend.handlers.AxTokenRefresh;

import java.io.IOException;
import java.net.InetSocketAddress;

public class AxLaunchController {

    public static void main(String[] args) throws IOException {
        int serverPort = 8000;
        HttpServer server = HttpServer.create(new InetSocketAddress(serverPort), 0);
        server.createContext("/api/token/create", new AxTokenCreate());
        server.createContext("/api/token/check", new AxTokenCheck());
        server.createContext("/api/token/refresh", new AxTokenRefresh());
        server.createContext("/api/token/logout", new AxTokenLogout());
        server.setExecutor(null); // creates a default executor
        server.start();
    }
}
