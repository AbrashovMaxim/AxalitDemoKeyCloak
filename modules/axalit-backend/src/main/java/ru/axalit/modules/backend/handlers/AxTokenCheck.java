package ru.axalit.modules.backend.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONObject;
import ru.axalit.modules.backend.core.AxKeycloak;

import java.io.IOException;
import java.io.OutputStream;

import static ru.axalit.modules.backend.core.AxHelper.convertToJsonObject;

public class AxTokenCheck implements HttpHandler {


    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!exchange.getRequestMethod().equals("POST")) {
            throw new UnsupportedOperationException();
        }

        // Преобразуем строку в JSONObject
        JSONObject responseJson = convertToJsonObject(exchange.getRequestBody());

        JSONObject requestJson;
        if (!responseJson.has("token")) {
            requestJson = new JSONObject();
            requestJson.put("error", "Не хватает полей token");
        } else {
            requestJson = AxKeycloak.introspectToken(responseJson.getString("token"));
            if (requestJson.keySet().contains("active")) {
                boolean check = requestJson.getBoolean("active");
                requestJson.clear();
                requestJson.put("active", check);
            }
        }

        String responseString = requestJson.toString();

        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, responseString.getBytes().length);
        OutputStream output = exchange.getResponseBody();
        output.write(responseString.getBytes());
        output.flush();
        exchange.close();

    }
}
