package com.tool.lyrics;

import com.tool.lyrics.controller.LyricsController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class LyricsPlayerApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("LyricsPlayer.fxml"));
        Parent root = loader.load();
        LyricsController controller = loader.getController();
        controller.setPrimaryStage(primaryStage);

        primaryStage.setTitle("歌詞播放器");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root, 750, 420));
        primaryStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icons/logo.png"))));

        primaryStage.show();
    }
}
