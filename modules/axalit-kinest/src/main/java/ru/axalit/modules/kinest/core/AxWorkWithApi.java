package ru.axalit.modules.kinest.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

import static ru.axalit.modules.kinest.core.AxHelper.convertJsonMapToString;

public class AxWorkWithApi {

    private static final String SERVER_URL = "http://localhost:8000";

    private static final String API_TOKEN_CHECK = SERVER_URL + "/api/token/check";
    private static final String API_TOKEN_REFRESH = SERVER_URL + "/api/token/refresh";
    private static final String API_TOKEN_LOGOUT = SERVER_URL + "/api/token/logout";



    public static boolean checkToken(String token) {
        HttpClient client = HttpClient.newBuilder().build();

        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("token", token);

        String form = convertJsonMapToString(parameters);

        HttpRequest request = sendRequest(API_TOKEN_CHECK, form);

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JSONObject result = new JSONObject(new ObjectMapper().readValue(response.body(), Map.class));
            if (result.keySet().contains("active")) {
                return result.getBoolean("active");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            client.close();
        }
        return false;
    }

    public static JSONObject refreshToken(String refreshToken) {
        HttpClient client = HttpClient.newBuilder().build();

        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("refreshToken", refreshToken);

        String form = convertJsonMapToString(parameters);

        HttpRequest request = sendRequest(API_TOKEN_REFRESH, form);

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JSONObject result = new JSONObject(new ObjectMapper().readValue(response.body(), Map.class));
            return result;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            client.close();
        }

        return new JSONObject();
    }


    private static HttpRequest sendRequest(String url, String form) {
        return HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(form))
                .build();
    }

}
