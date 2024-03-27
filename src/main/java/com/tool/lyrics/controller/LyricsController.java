package com.tool.lyrics.controller;

import com.tool.lyrics.model.DisplayConfig;
import com.tool.lyrics.model.Lyric;
import com.tool.lyrics.model.Song;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

@Getter
@Setter
public class LyricsController implements Initializable {
    private final int INTERVAL_MILLS = 200;
    private final int DEFAULT_SECOND = 1;
    private boolean clickLyric = true;


    public Text currentText;
    public Label currentTimeLabel;
    public Label totalTimeLabel;
    public Button displayConfigBtn;
    public Label visible;
    public CheckBox visibleCheckBox;
    public Button closeBtn;
    public Button quickPlayBtn;
    public Button backPlayBtn;
    public Spinner<Double> secondSpinner;
    @FXML
    private TextArea lyricsArea;
    private final ObservableList<Song> songs = FXCollections.observableArrayList();
    private final ObservableList<Lyric> lyrics = FXCollections.observableArrayList();
    @FXML
    private ListView<Song> songViewList = new ListView<>();
    @FXML
    private ListView<Lyric> lyricsViewList = new ListView<>();
    @FXML
    private Button uploadLyricsBtn;
    @FXML
    public Button pauseBtn;
    @FXML
    public Button playBtn;
    @FXML
    private Slider timeSlider;
    private Timeline timeline;
    private Song currentSong;
    private long currentMillis;
    private DisplayConfig appConfig = new DisplayConfig();
    private int currentLyricIndex = -1;
    private Tooltip tooltip = new Tooltip();
    private Stage primaryStage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeListViews();
    }

    private void initializeListViews() {
        setDefaultConfig();
        bindProperties();
        setSongViewList();
        setLyricsViewList();
        setTimeConfig();
        setTimeSliderConfig();
        visibleCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> currentText.setVisible(newValue));
    }

    private void bindProperties() {
        currentText.fillProperty().bind(Bindings.createObjectBinding(() -> Color.web(appConfig.getTextColor().getValue()), appConfig.getTextColor()));
        lyricsArea.styleProperty().bind(Bindings.createStringBinding(() -> MessageFormat.format("-fx-control-inner-background:{0};", appConfig.getBgColor().getValue()), appConfig.getBgColor()));
        currentText.fontProperty().bind(Bindings.createObjectBinding(() -> Font.font(appConfig.getFontStyle().getValue(), appConfig.getFontSize().getValue()), appConfig.getFontSize(), appConfig.getFontStyle()));
        currentText.textAlignmentProperty().bind(appConfig.getTextAlignment());
    }

    private void setTimeConfig() {
        timeline = new Timeline(new KeyFrame(Duration.millis(INTERVAL_MILLS), e -> handleTimeAction()));
        timeline.setCycleCount(Timeline.INDEFINITE);
    }

    private void setTimeSliderConfig() {
        timeSlider.setMax(0);
        timeSlider.valueProperty().addListener((observable, oldValue, newValue) -> handleTimeSliderAction(newValue));

        timeSlider.setOnMouseMoved(event -> {
            double mouseX = event.getX();
            double totalWidth = timeSlider.getWidth();
            double sliderMax = timeSlider.getMax();
            double sliderValue = (mouseX / totalWidth) * sliderMax;

            String formattedDuration = formatDuration(sliderValue);
            tooltip.setText(formattedDuration);
            tooltip.show(timeSlider, event.getScreenX(), event.getScreenY() + 20);
        });
        timeSlider.setOnMouseExited(event -> tooltip.hide());

        tooltip.setShowDelay(Duration.ZERO);
        tooltip.setHideDelay(Duration.ZERO);
    }

    private void handleTimeSliderAction(Number newValue) {
        if (Objects.nonNull(currentSong)) {
            currentMillis = (long) newValue.doubleValue();
            currentTimeLabel.setText(formatDuration(currentMillis));
            selectLyrics(Duration.millis(currentMillis));
            updateCurrentLyricIndex();
        }
    }

    private void setDefaultConfig() {
        appConfig.getFontSize().setValue(20);
        appConfig.getFontStyle().setValue("Verdana");
        appConfig.getTextAlignment().setValue(TextAlignment.RIGHT);
        appConfig.getTextColor().setValue("#ffffff");
        appConfig.getBgColor().setValue("#000000");
    }

    private void handleTimeAction() {
        currentMillis += INTERVAL_MILLS;
        whenCurrentTimeChange();
        updateCurrentLyricIndex();
    }

    private void updateCurrentLyricIndex() {
        List<Lyric> lyrics = currentSong.getLyrics();
        for (int i = 0; i < lyrics.size() - 1; i++) {
            double startTime = lyrics.get(i).getTime();
            double endTime = lyrics.get(i + 1).getTime();

            if (currentMillis >= startTime && currentMillis < endTime) {
                currentLyricIndex = i;
                break;
            }
        }
        lyricsViewList.getSelectionModel().select(currentLyricIndex);
    }

    private void whenCurrentTimeChange() {
        updateSlider();
        currentTimeLabel.setText(formatDuration(currentMillis));
        selectLyrics(Duration.millis(currentMillis));
    }

    private void setLyricsViewList() {
        lyricsViewList.setItems(lyrics);
        lyricsViewList.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Lyric item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item.getText());
            }
        });
        lyricsViewList.getSelectionModel().selectedItemProperty().addListener((observable, originLyric, chooseLyric) -> handleSelectLyrics(chooseLyric));
    }

    private void handleSelectLyrics(Lyric chooseLyric) {
        if (Objects.nonNull(chooseLyric) && clickLyric) {
            double time = chooseLyric.getTime();
            currentMillis = (long) Duration.millis(time).toMillis();
            whenCurrentTimeChange();
        }
    }

    private void setSongViewList() {
        songViewList.setItems(songs);
        songViewList.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Song item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(item.getName() + "        ");
                    Button deleteButton = new Button("删除");
                    deleteButton.setOnAction(event -> handleDeleteSong(item));
                    setGraphic(deleteButton);
                    setContentDisplay(ContentDisplay.RIGHT);
                }
            }
        });

        songViewList.getSelectionModel().selectedItemProperty().addListener((observable, originSong, chooseSong) -> handleSelectSong(chooseSong));
    }

    private void handleDeleteSong(Song song) {
        if (song.equals(currentSong)) {
            stopPlay();
            currentText.setText(null);
            lyrics.clear();

            totalTimeLabel.setText(formatDuration(0));
            timeSlider.setMax(0);
            currentSong = null;
        }
        songs.remove(song);
    }

    private void handleSelectSong(Song chooseSong) {
        if (Objects.nonNull(chooseSong)) {
            currentSong = chooseSong;
            List<Lyric> lyrics = currentSong.getLyrics();
            double time = lyrics.get(lyrics.size() - 1).getTime();
            timeSlider.setMax(time);
            totalTimeLabel.setText(formatDuration(time));
            displayLyrics(chooseSong);
        }
    }

    @FXML
    void uploadLyricsAction() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("選擇歌詞文件", "*.txt", "*.lrc"));
        List<File> files = fileChooser.showOpenMultipleDialog(primaryStage);
        if (files != null && !files.isEmpty()) {
            files.forEach(file -> {
                if (file != null) {
                    try {
                        Song newSong = Song.valueOf(file);
                        songs.add(newSong);
                    } catch (RuntimeException e) {
                        displayWarnAlert(e.getMessage());
                    }
                }
            });
        }
    }

    private void displayLyrics(Song chooseSong) {
        if (chooseSong != null) {
            lyrics.clear();
            lyrics.addAll(chooseSong.getLyrics());
        }
    }

    @FXML
    private void handlePlayPauseAction() {
        if (currentSong == null) {
            displayWarnAlert("請先選擇歌曲");
            return;
        }

        if (timeline != null && timeline.getStatus() == Timeline.Status.RUNNING) {
            timeline.pause();
            playBtn.setText("播放");
        } else {
            timeline.play();
            playBtn.setText("暫停");
        }

    }

    private void displayWarnAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("提示");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void updateSlider() {
        double duration = timeSlider.getMax();
        if (currentMillis < duration) {
            timeSlider.setValue(currentMillis);
        } else {
            timeline.stop();
        }
    }

    private void selectLyrics(Duration currentTime) {
        double currentTimeSecs = currentTime.toMillis();
        List<Lyric> lyrics = currentSong.getLyrics();

        for (int i = 0; i < lyrics.size() - 1; i++) {
            double startTime = lyrics.get(i).getTime();
            double endTime = lyrics.get(i + 1).getTime();

            if (currentTimeSecs >= startTime && currentTimeSecs < endTime) {
                String currentLyricText = lyrics.get(i).getText();
                currentText.setText(currentLyricText);
                break;
            }
        }
    }


    private String formatDuration(double durationInMilliseconds) {
        int totalSeconds = (int) (durationInMilliseconds / 1000);
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;

        NumberFormat formatter = new DecimalFormat("00");
        return formatter.format(minutes) + ":" + formatter.format(seconds);
    }

    public void showDisplayConfigWindow(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("DisplayConfig.fxml"));
            Parent root = loader.load();
            DisplayConfigController controller = loader.getController();
            controller.initializeWithConfig(appConfig);
            controller.setLyricsPlayer(this);

            if (root != null) {
                Stage stage = new Stage();
                stage.setTitle("版面設定");
                stage.setResizable(false);
                stage.setScene(new Scene(root, 350, 300));
                stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icons/logo.png"))));

                if (primaryStage != null) {
                    stage.initOwner(primaryStage);
                }

                stage.show();
            } else {
                System.err.println("Failed to load DisplayConfigController.fxml. Root is null.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void doQuickPlay() {
        if (currentSong == null) {
            displayWarnAlert("請先選擇歌曲");
            return;
        }
        clickLyric = false;
        double quickMillis = secondSpinner.getValue() * 1000;
        timeSlider.setValue(currentMillis + quickMillis);
        clickLyric = true;
    }

    public void doBackPlay() {
        if (currentSong == null) {
            displayWarnAlert("請先選擇歌曲");
            return;
        }
        clickLyric = false;
        double backMillis = secondSpinner.getValue() * 1000;
        timeSlider.setValue(currentMillis - backMillis);
        clickLyric = true;
    }

    public void stopPlay() {
        timeSlider.setValue(0);
        timeline.pause();
        playBtn.setText("播放");
    }

}


