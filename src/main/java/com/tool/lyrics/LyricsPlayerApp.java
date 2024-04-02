package com.tool.lyrics;

import com.tool.lyrics.controller.DisplayConfigController;
import com.tool.lyrics.controller.LyricsController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

import static com.tool.lyrics.controller.LevitateLyricsController.initLevitateLyricsStage;

public class LyricsPlayerApp extends Application {
    public static Stage displayConfigStage;
    public static Stage levitateLyricsStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("LyricsPlayer.fxml"));
        Parent root = loader.load();
        LyricsController controller = loader.getController();
        controller.setPrimaryStage(primaryStage);

        initDisplayConfigStage();
        initLevitateLyricsStage();

        primaryStage.setTitle("歌詞播放器");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root, 750, 420));
        primaryStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icons/logo.png"))));
        primaryStage.show();

        primaryStage.setOnCloseRequest(event -> {
            if (displayConfigStage != null) {
                displayConfigStage.close();
            }
            if (levitateLyricsStage != null) {
                levitateLyricsStage.close(); // 单独关闭这个窗口
            }
        });
    }

    public void initDisplayConfigStage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("./controller/DisplayConfig.fxml"));
            Parent root = loader.load();
            DisplayConfigController controller = loader.getController();
            controller.initializeConfig();

            if (root != null) {
                displayConfigStage = new Stage();
                displayConfigStage.setTitle("版面設定");
                displayConfigStage.setResizable(false);
                displayConfigStage.setScene(new Scene(root));
                displayConfigStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icons/logo.png"))));

            } else {
                System.err.println("Failed to load DisplayConfigController.fxml. Root is null.");
            }
        } catch (IOException ignored) {

        }
    }
}
