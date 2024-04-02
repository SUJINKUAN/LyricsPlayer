package com.tool.lyrics.controller;

import com.tool.lyrics.model.LevitateDisplayConfig;
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

        Scene scene = new Scene(root, 445, 175);
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
                    } else if (inRight) {
                        cursorType = E_RESIZE;
                    } else if (inLeft) {
                        cursorType = W_RESIZE;
                    } else if (inTop) {
                        cursorType = N_RESIZE;
                    } else if (inBottom) {
                        cursorType = S_RESIZE;
                    }
                    scene.setCursor(cursorType);
                }
        );

        scene.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        scene.setOnMouseDragged(event -> {
            double mouseX = event.getScreenX();
            double mouseY = event.getScreenY();

            if (scene.getCursor() != Cursor.DEFAULT) {
                double newWidth = stage.getWidth();
                double newHeight = stage.getHeight();
                double newX = stage.getX();
                double newY = stage.getY();

                Cursor cursor = scene.getCursor();
                if (cursor.equals(N_RESIZE)) {
                    newHeight = newY + newHeight - mouseY;
                    newY = mouseY;
                } else if (cursor.equals(S_RESIZE)) {
                    newHeight = mouseY - newY;
                } else if (cursor.equals(E_RESIZE)) {
                    newWidth = mouseX - newX;
                } else if (cursor.equals(W_RESIZE)) {
                    newWidth = newX + newWidth - mouseX;
                    newX = mouseX;
                } else if (cursor.equals(NE_RESIZE)) {
                    newHeight = newY + newHeight - mouseY;
                    newY = mouseY;
                    newWidth = mouseX - newX;
                } else if (cursor.equals(NW_RESIZE)) {
                    newHeight = newY + newHeight - mouseY;
                    newY = mouseY;
                    newWidth = newX + newWidth - mouseX;
                    newX = mouseX;
                } else if (cursor.equals(SE_RESIZE)) {
                    newHeight = mouseY - newY;
                    newWidth = mouseX - newX;
                } else if (cursor.equals(SW_RESIZE)) {
                    newHeight = mouseY - newY;
                    newWidth = newX + newWidth - mouseX;
                    newX = mouseX;
                }

                if (newWidth >= originSize[0]) {
                    stage.setWidth(newWidth);
                    stage.setX(newX);
                }
                if (newHeight >= originSize[1]) {
                    stage.setHeight(newHeight);
                    stage.setY(newY);
                }

            } else {
                stage.setX(mouseX - xOffset);
                stage.setY(mouseY - yOffset);
            }
        });
    }


    private static void bindProperties() {
        LevitateDisplayConfig config = LevitateDisplayConfig.getInstance();
        lyrics.fillProperty().bind(Bindings.createObjectBinding(() -> Color.web(config.getTextColor().getValue()), config.getTextColor()));
        lyrics.fontProperty().bind(Bindings.createObjectBinding(() -> Font.font(config.getFontStyle().getValue(), config.getFontSize().getValue()), config.getFontSize(), config.getFontStyle()));
        lyrics.textAlignmentProperty().bind(config.getTextAlignment());
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
