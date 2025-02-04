package ru.axalit.modules.kinest;

import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.json.JSONObject;
import ru.axalit.modules.kinest.core.AxWorkWithApi;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AxApplicationController extends Application {

    private String token;
    private String refreshToken;


    public void start(Stage primaryStage) throws Exception {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss");

        Button justDoItButton = new Button("Выполнить какое-то действие");
        justDoItButton.setOnAction(event -> {
            if (AxWorkWithApi.checkToken(token)) {
                System.out.println("\n\n=====================================");
                System.out.println(LocalDateTime.now().format(dateTimeFormatter) + " - ACTION");
            } else {
                JSONObject json = AxWorkWithApi.refreshToken(refreshToken);
                if (!json.keySet().contains("error")) {
                    token = json.getString("access_token");
                    System.out.println("UPDATE TOKEN");
                } else {
                    System.out.println("ERROR!!! ERROR!!! ERROR!!!");
                    System.out.println(json.getString("error_description"));
                    System.exit(0);
                }
            }
        });
        AnchorPane justDoItAnchorPane = new AnchorPane(justDoItButton);
        setAnchors(justDoItButton, 0);

        Button sendTokenButton = new Button("Вывести token и refreshToken");
        sendTokenButton.setOnAction(event -> {
            System.out.println("\n\n=====================================");
            System.out.println(LocalDateTime.now().format(dateTimeFormatter) + " - TOKEN AND REFRESH");
            System.out.println(token);
            System.out.println(refreshToken);
        });
        AnchorPane sendTokenAnchorPane = new AnchorPane(sendTokenButton);
        setAnchors(sendTokenButton, 0);

        VBox vBox = new VBox();
        vBox.setSpacing(5);
        vBox.getChildren().addAll(justDoItAnchorPane, sendTokenAnchorPane);

        Parameters params = getParameters();
        Map<String, String> map = params.getNamed();
        if (!map.containsKey("token") || !map.containsKey("refreshToken")) {
            throw new Exception("Not found Token or RefreshToken");
        } else {
            token = map.get("token");
            refreshToken = map.get("refreshToken");
        }

        Scene scene = new Scene(vBox, 500, 400);

        primaryStage.setTitle("KINEST");
        primaryStage.setScene(scene);
        primaryStage.show();
    }



    private void setAnchors(Node node, Number topRightBottomLeft) {
        AnchorPane.setTopAnchor(node, topRightBottomLeft.doubleValue());
        AnchorPane.setRightAnchor(node, topRightBottomLeft.doubleValue());
        AnchorPane.setBottomAnchor(node, topRightBottomLeft.doubleValue());
        AnchorPane.setLeftAnchor(node, topRightBottomLeft.doubleValue());
    }

}
