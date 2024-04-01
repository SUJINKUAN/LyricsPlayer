package com.tool.lyrics.controller;

import com.tool.lyrics.model.DisplayConfig;
import javafx.beans.binding.Bindings;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import static javafx.scene.Cursor.*;


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
        root.setStyle("-fx-background-color: rgba(255, 255, 255, 0.01);");

        root.setOnMouseEntered(event -> root.setStyle("-fx-background-color: rgba(255, 255, 255, 0.5);"));
        root.setOnMouseExited(event -> root.setStyle("-fx-background-color: rgba(255, 255, 255, 0.01);"));


        Scene scene = new Scene(root, 445, 173);
        scene.setFill(null);
        initSceneEvent(scene);

        stage = new Stage();
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setScene(scene);


        stage.setAlwaysOnTop(true);

        bindProperties();
    }

    private static void initSceneEvent(Scene scene) {
        final double[] originSize = {scene.getWidth(), scene.getHeight()};

        scene.setOnMouseMoved(event -> {
            double x = event.getX();
            double y = event.getY();
            double width = scene.getWidth();
            double height = scene.getHeight();

            Cursor cursorType = Cursor.DEFAULT;
            boolean inLeft = x < RESIZE_MARGIN;
            boolean inRight = x > width - RESIZE_MARGIN;
            boolean inTop = y < RESIZE_MARGIN;
            boolean inBottom = y > height - RESIZE_MARGIN;

            if (inTop && inLeft) {
                cursorType = NW_RESIZE;
            } else if (inTop && inRight) {
                cursorType = Cursor.NE_RESIZE;
            } else if (inBottom && inLeft) {
                cursorType = SW_RESIZE;
            } else if (inBottom && inRight) {
                cursorType = Cursor.SE_RESIZE;
            } else if (inRight || inLeft) {
                cursorType = Cursor.H_RESIZE;
            } else if (inTop || inBottom) {
                cursorType = V_RESIZE;
            }
            scene.setCursor(cursorType);
        });

        scene.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
            // 记录按下时的窗口尺寸
            originSize[0] = stage.getWidth();
            originSize[1] = stage.getHeight();
        });

        scene.setOnMouseDragged(event -> {
            if (scene.getCursor() != Cursor.DEFAULT) {
                double deltaX = event.getScreenX() - xOffset - stage.getX();
                double deltaY = event.getScreenY() - yOffset - stage.getY();

                Cursor cursor = scene.getCursor();
                if (cursor.equals(V_RESIZE)) {
                    stage.setHeight(Math.max(originSize[1] + deltaY, originSize[1]));
                } else if (cursor.equals(H_RESIZE)) {
                    stage.setWidth(Math.max(originSize[0] + deltaX, originSize[0]));
                } else if (cursor.equals(SE_RESIZE)) {
                    stage.setWidth(Math.max(originSize[0] + deltaX, originSize[0]));
                    stage.setHeight(Math.max(originSize[1] + deltaY, originSize[1]));
                } else if (cursor.equals(NE_RESIZE)) {
                    stage.setWidth(Math.max(originSize[0] + deltaX, originSize[0]));
                    stage.setHeight(Math.max(deltaY + originSize[1], originSize[1]));
                } else if (cursor.equals(SW_RESIZE)) {
                    stage.setWidth(Math.max(deltaX + originSize[0], originSize[0]));
                    stage.setHeight(Math.max(originSize[1] + deltaY, originSize[1]));
                } else if (cursor.equals(NW_RESIZE)) {
                    stage.setWidth(Math.max(deltaX + originSize[0], originSize[0]));
                    stage.setHeight(Math.max(deltaY + originSize[1], originSize[1]));
                }
            } else {
                // 拖动窗口
                stage.setX(event.getScreenX() - xOffset);
                stage.setY(event.getScreenY() - yOffset);
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
