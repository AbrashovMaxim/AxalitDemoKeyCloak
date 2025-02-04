package ru.axalit.modules.geobazis.core;

import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

public class AxJavaFX {

    public static void setAnchors(Node node, Number topRightBottomLeft) {
        AnchorPane.setTopAnchor(node, topRightBottomLeft.doubleValue());
        AnchorPane.setRightAnchor(node, topRightBottomLeft.doubleValue());
        AnchorPane.setBottomAnchor(node, topRightBottomLeft.doubleValue());
        AnchorPane.setLeftAnchor(node, topRightBottomLeft.doubleValue());
    }

}
