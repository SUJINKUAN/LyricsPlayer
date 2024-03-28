package com.tool.lyrics.controller;

import com.tool.lyrics.model.DisplayConfig;
import javafx.beans.binding.Bindings;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class LevitateLyricsController {
    private static Stage stage;
    private static Text lyrics;
    private static final double RESIZE_MARGIN = 10;

    private static double xOffset = 0;
    private static double yOffset = 0;

    public static void initLevitateLyrics() {
        lyrics = new Text();
        StackPane root = new StackPane();
        root.getChildren().add(lyrics);
        root.setBackground(Background.EMPTY);
        root.setStyle("-fx-background-color: transparent; -fx-padding: 10;");
        root.setOnMouseEntered(event -> {
            // 显示边框
            root.setBorder(new Border(new BorderStroke(Color.BLUE,
                    BorderStrokeStyle.SOLID,
                    null,
                    new BorderWidths(2))));
        });

        // 监听鼠标离开事件
        root.setOnMouseExited(event -> {
            // 隐藏边框
            root.setBorder(Border.EMPTY);
        });

        Scene scene = new Scene(root, 173, 445);
        scene.setFill(null);

        stage = new Stage();
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setScene(scene);

        initSceneEvent(scene);

        stage.setAlwaysOnTop(true);

        bindProperties();
    }

    private static void initSceneEvent(Scene scene) {
        scene.setOnMouseMoved(event -> {
            double x = event.getX();
            double y = event.getY();
            double width = scene.getWidth();
            double height = scene.getHeight();

            Cursor cursorType = Cursor.DEFAULT;
            boolean resizeH = x < RESIZE_MARGIN || x > width - RESIZE_MARGIN;
            boolean resizeV = y < RESIZE_MARGIN || y > height - RESIZE_MARGIN;

            if (resizeH && resizeV) {
                cursorType = Cursor.SE_RESIZE;
            } else if (resizeH) {
                cursorType = Cursor.E_RESIZE;
            } else if (resizeV) {
                cursorType = Cursor.N_RESIZE;
            }
            scene.setCursor(cursorType);
        });

        scene.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        scene.setOnMouseDragged(event -> {
            double x = event.getX();
            double y = event.getY();
            double width = x + xOffset;
            double height = y + yOffset;

            if (scene.getCursor() == Cursor.SE_RESIZE) {
                stage.setWidth(width);
                stage.setHeight(height);
            } else if (scene.getCursor() == Cursor.E_RESIZE) {
                stage.setWidth(width);
            } else if (scene.getCursor() == Cursor.N_RESIZE) {
                stage.setHeight(height);
            }
        });
    }

    private static void bindProperties() {
        DisplayConfig appConfig = DisplayConfig.getInstance();
        lyrics.fillProperty().bind(Bindings.createObjectBinding(() -> Color.web(appConfig.getTextColor().getValue()), appConfig.getTextColor()));
        lyrics.fontProperty().bind(Bindings.createObjectBinding(() -> Font.font(appConfig.getFontStyle().getValue(), appConfig.getFontSize().getValue()), appConfig.getFontSize(), appConfig.getFontStyle()));
        lyrics.textAlignmentProperty().bind(appConfig.getTextAlignment());
    }

    public static void displayLevitateLyrics(Boolean display) {
        if (display) {
            stage.show();
        } else {
            stage.close();
        }
    }

    public static void updateLyrics(String text) {
        lyrics.setText(text);
    }
}
