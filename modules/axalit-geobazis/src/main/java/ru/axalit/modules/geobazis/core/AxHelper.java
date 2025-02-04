package ru.axalit.modules.geobazis.core;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;

public class AxHelper {

    public static JSONObject convertToJsonObject(InputStream inputStream) {

        StringBuilder stringBuilder = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new JSONObject();
        }

        String string = stringBuilder.toString();

        try {
            return new JSONObject(string);
        } catch (Exception e) {
            if (string.contains("=")) {
                string = string.replace("=", ":");
                string = string.replace("&", ",");
                string = "{" + string + "}";
                try {
                    return new JSONObject(string);
                } catch (Exception ignore) {
                    return new JSONObject();
                }
            } else {
                return new JSONObject();
            }
        }
    }

    public static String convertJsonMapToString(Map<String, String> map) {
        return map.entrySet()
                .stream()
                .map(e -> e.getKey() + "=" + URLEncoder.encode(e.getValue(), StandardCharsets.UTF_8))
                .collect(Collectors.joining("&"));
    }

}
