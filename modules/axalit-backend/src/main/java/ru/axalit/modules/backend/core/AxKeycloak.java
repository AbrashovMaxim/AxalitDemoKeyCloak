package ru.axalit.modules.backend.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

import static ru.axalit.modules.backend.core.AxHelper.convertJsonMapToString;

public class AxKeycloak {

    private static final String SERVER_URL = "http://localhost:8080";
    private static final String REALM = "master";
    private static final String CLIENT_ID = "client-id02";
    private static final String CLIENT_SECRET = "zDTih8v3RUj777m4rEM03AweWgShF1y8";

    private static final String SERVER_URL_TOKEN = SERVER_URL + "/realms/" + REALM + "/protocol/openid-connect/token";
    private static final String SERVER_URL_TOKEN_INTROSPECT = SERVER_URL + "/realms/" + REALM + "/protocol/openid-connect/token/introspect";
    private static final String SERVER_URL_LOGOUT = SERVER_URL + "/realms/" + REALM + "/protocol/openid-connect/logout";


    public static JSONObject createToken(String username, String password) {
        HttpClient client = HttpClient.newBuilder().build();

        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("client_id", CLIENT_ID);
        parameters.put("client_secret", CLIENT_SECRET);
        parameters.put("grant_type", "password");
        parameters.put("username", username);
        parameters.put("password", password);

        String form = convertJsonMapToString(parameters);

        HttpRequest request = sendRequest(SERVER_URL_TOKEN, form);

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JSONObject result = new JSONObject(new ObjectMapper().readValue(response.body(), Map.class));
            return result;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            client.close();
        }

        JSONObject errorJson = new JSONObject();
        errorJson.put("error", "Ошибка при создании токена");
        return errorJson;
    }

    public static JSONObject introspectToken(String token) {
        HttpClient client = HttpClient.newBuilder().build();

        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("client_id", CLIENT_ID);
        parameters.put("client_secret", CLIENT_SECRET);
        parameters.put("token", token);

        String form = convertJsonMapToString(parameters);

        HttpRequest request = sendRequest(SERVER_URL_TOKEN_INTROSPECT, form);

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JSONObject result = new JSONObject(new ObjectMapper().readValue(response.body(), Map.class));
            return result;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            client.close();
        }

        JSONObject errorJson = new JSONObject();
        errorJson.put("error", "Ошибка при проверки токена");
        return errorJson;
    }

    public static JSONObject refreshToken(String refreshToken) {
        HttpClient client = HttpClient.newBuilder().build();

        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("client_id", CLIENT_ID);
        parameters.put("client_secret", CLIENT_SECRET);
        parameters.put("grant_type", "refresh_token");
        parameters.put("refresh_token", refreshToken);

        String form = convertJsonMapToString(parameters);

        HttpRequest request = sendRequest(SERVER_URL_TOKEN, form);

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JSONObject result = new JSONObject(new ObjectMapper().readValue(response.body(), Map.class));
            return result;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            client.close();
        }

        JSONObject errorJson = new JSONObject();
        errorJson.put("error", "Ошибка при обновление токена");
        return errorJson;
    }

    public static JSONObject logout(String refreshToken) {
        HttpClient client = HttpClient.newBuilder().build();

        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("client_id", CLIENT_ID);
        parameters.put("client_secret", CLIENT_SECRET);
        parameters.put("refresh_token", refreshToken);

        String form = convertJsonMapToString(parameters);

        HttpRequest request = sendRequest(SERVER_URL_LOGOUT, form);

        JSONObject errorJson = new JSONObject();
        try {
            client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            errorJson.put("error", "Ошибка при выходе");
        } finally {
            client.close();
        }
        return errorJson;
    }


    private static HttpRequest sendRequest(String url, String form) {
        return HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(form))
                .build();
    }


}
