package ru.axalit.modules.geobazis.display;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import static ru.axalit.modules.geobazis.core.AxJavaFX.setAnchors;

public class AxActionPane extends SplitPane {

    private final Label nameLabel;


    public AxActionPane(AxDisplayCallback callback) {
        nameLabel = new Label();
        Button exitButton = new Button("Выйти");
        exitButton.setOnAction(event -> callback.logout());

        HBox userHBox = new HBox();
        userHBox.setAlignment(Pos.TOP_RIGHT);
        userHBox.getChildren().addAll(nameLabel, exitButton);


        Button openKinestButton = new Button("Открыть кинест");
        openKinestButton.setOnAction(event -> callback.openKinest());
        AnchorPane openKinestAnchorPane = new AnchorPane(openKinestButton);
        setAnchors(openKinestButton, 0);

        VBox vBox = new VBox();
        vBox.setSpacing(5);
        vBox.getChildren().addAll(openKinestAnchorPane);

        this.getItems().addAll(vBox, userHBox);
        this.setDividerPositions(.5f, .5f);
    }

    public void setName(String name) {
        nameLabel.setText(name);
    }

}
