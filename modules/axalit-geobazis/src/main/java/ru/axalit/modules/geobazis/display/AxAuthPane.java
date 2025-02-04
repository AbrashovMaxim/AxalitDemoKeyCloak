package ru.axalit.modules.geobazis.display;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import org.json.JSONObject;
import ru.axalit.modules.geobazis.core.AxKeycloak;

import java.util.HashMap;
import java.util.Map;

import static ru.axalit.modules.geobazis.core.AxJavaFX.setAnchors;

public class AxAuthPane extends VBox {

    public AxAuthPane(AxDisplayCallback callback) {
        Label nameLabel = new Label("Username:");
        TextField nameTextField = new TextField();
        setAnchors(nameTextField, 0.);
        AnchorPane nameTextFieldAnchorPane = new AnchorPane(nameTextField);
        VBox nameVBox = new VBox();
        nameVBox.getChildren().addAll(nameLabel, nameTextFieldAnchorPane);
        nameVBox.setSpacing(5);

        Label passwordLabel = new Label("Password:");
        TextField passwordTextField = new TextField();
        setAnchors(passwordTextField, 0.);
        AnchorPane passwordTextFieldAnchorPane = new AnchorPane(passwordTextField);
        VBox passwordVBox = new VBox();
        passwordVBox.getChildren().addAll(passwordLabel, passwordTextFieldAnchorPane);
        passwordVBox.setSpacing(5);

        Label errorLabel = new Label();

        Button button = new Button("Авторизоваться");
        button.setOnAction(event -> {
            this.getChildren().remove(errorLabel);
            JSONObject result = AxKeycloak.createToken(nameTextField.getText(), passwordTextField.getText());
            if (result.keySet().contains("error_description")) {
                errorLabel.setText(result.getString("error_description"));
                this.getChildren().add(errorLabel);
            } else if(result.keySet().contains("error")) {
                errorLabel.setText(result.getString("error"));
                this.getChildren().add(errorLabel);
            } else {
                result.put("name", nameTextField.getText());
                callback.saveResultAuth(result);
                callback.changeToAction();
            }
        });

        this.getChildren().addAll(nameVBox, passwordVBox, button);
        this.setSpacing(5);
    }

}
