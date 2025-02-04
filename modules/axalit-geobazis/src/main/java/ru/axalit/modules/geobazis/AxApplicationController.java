package ru.axalit.modules.geobazis;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.json.JSONObject;
import ru.axalit.modules.geobazis.core.AxKeycloak;
import ru.axalit.modules.geobazis.core.AxWorkWithApi;
import ru.axalit.modules.geobazis.display.AxActionPane;
import ru.axalit.modules.geobazis.display.AxAuthPane;
import ru.axalit.modules.geobazis.display.AxDisplayCallback;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class AxApplicationController extends Application implements AxDisplayCallback {

    private AxAuthPane authPane;
    private AxActionPane actionPane;

    private Pane primaryPane;

    private JSONObject resultAuth;


    public void start(Stage primaryStage) {

        primaryPane = new StackPane();

        authPane = new AxAuthPane(this);
        actionPane = new AxActionPane(this);

        primaryPane.getChildren().add(authPane);

        Scene scene = new Scene(primaryPane, 500, 400);

        primaryStage.setTitle("ГЕОБАЗИС");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    @Override
    public void changeToAuth() {
        primaryPane.getChildren().setAll(authPane);
    }

    @Override
    public void changeToAction() {
        primaryPane.getChildren().setAll(actionPane);

    }

    @Override
    public void saveResultAuth(JSONObject resultAuth) {
        this.resultAuth = resultAuth;
        actionPane.setName(resultAuth.getString("name"));
    }

    @Override
    public void openKinest() {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("\"jre\\bin\\java\" -jar " + System.getProperty("user.dir") + File.separator + "KINEST.jar" +
                    " --token=\"" + resultAuth.getString("access_token") + "\" --refreshToken=\"" + resultAuth.getString("refresh_token") + "\""
            );
            processBuilder.redirectErrorStream(true);
            processBuilder.redirectOutput();
            Process process = processBuilder.start();
            try (final BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line = null;
                while ((line = br.readLine()) != null) {
                    System.out.println("[KINEST]: " + line);
                }
            } catch (Exception exc) {
                exc.printStackTrace();
            }
            int exitValue = process.waitFor();
            process.destroy();
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }

    @Override
    public void logout() {
        AxKeycloak.logout(resultAuth.getString("refresh_token"));
        changeToAuth();
        resultAuth.clear();
    }
}
